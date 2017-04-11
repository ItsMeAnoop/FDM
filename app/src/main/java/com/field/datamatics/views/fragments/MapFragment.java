package com.field.datamatics.views.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.util.Util;
import com.field.datamatics.R;
import com.field.datamatics.constants.Constants;
import com.field.datamatics.database.Client;
import com.field.datamatics.database.Client$Table;
import com.field.datamatics.database.CustomRoutePlan;
import com.field.datamatics.database.Customer;
import com.field.datamatics.database.Customer$Table;
import com.field.datamatics.database.Reminder;
import com.field.datamatics.database.Reminder$Table;
import com.field.datamatics.database.RoutePlan$Table;
import com.field.datamatics.utils.AppControllerUtil;
import com.field.datamatics.utils.Utilities;
import com.field.datamatics.views.MainActivity;
import com.field.datamatics.views.adapters.ClientListAdapter;
import com.field.datamatics.views.adapters.MainRemainderListAdapter;
import com.field.datamatics.views.helper.DirectionsJSONParser;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.raizlabs.android.dbflow.sql.builder.Condition;
import com.raizlabs.android.dbflow.sql.language.ColumnAlias;
import com.raizlabs.android.dbflow.sql.language.Join;
import com.raizlabs.android.dbflow.sql.language.Select;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

/**
 * Created by sreejith on 11/3/2015.
 * Map view logic
 */
public class MapFragment extends BaseFragment implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private AppCompatButton btnPending;
    private TextView lblProgressRoute;
    private RadioGroup radioFilter;
    private TextView lblLocationCategory;
    private GoogleMap mMap;

    private ArrayList<Marker> pendingMarkers;
    private ArrayList<Marker> routeMarkers;

    private List<CustomRoutePlan> routeVisit;
    private List<CustomRoutePlan> pendingVisit;

    private ArrayList<LatLng> routeLatLng;
    ArrayList<ClientsGrouped> groupedClients;
    ArrayList<ClientsGrouped> groupedPendingVisits;

    private Activity activity;
    HashMap<String, ArrayList<CustomRoutePlan>> map;

    private LatLng origin;
    private LatLng homeCoordinates;
    private LatLng officeCoordinates;

    private Toolbar toolbar;


    ProgressDialog pd;
    private Location mLastLocation;
    private GoogleApiClient mGoogleApiClient;
    private boolean showedGpsSettings;
    private int type;

    private boolean isFirstTime;
    private volatile boolean checkedForFistTime = true;

    private ArrayList<Reminder> dataReminder;

    public MapFragment() {
    }

    public static MapFragment getInstance(int type) {
        MapFragment f = new MapFragment();
        f.type = type;
        return f;
    }

    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.activity_maps, container, false);
        setTitle("Route Plan");
        addFragmentTitle("MapFragment");

        activity = getActivity();


        lblProgressRoute = (TextView) view.findViewById(R.id.lbl_route_progress);
        btnPending = (AppCompatButton) view.findViewById(R.id.btn_pending);
        radioFilter = (RadioGroup) view.findViewById(R.id.radio_filter);
        lblLocationCategory = (TextView) view.findViewById(R.id.tv_location_category);

        if (Utilities.checkPlayServices(getContext())) {
            buildGoogleApiClient();
        }

        RadioGroup.OnCheckedChangeListener checkedChangeListener = new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                isFirstTime = false;
                if (checkedForFistTime) {
                    checkedForFistTime = false;
                    return;
                }
                switch (checkedId) {
                    case R.id.rb_home:
                        filterHome();
                        break;
                    case R.id.rb_office:
                        filterOffice();
                        break;
                    case R.id.rb_current_location:
                        filterCurrent();
                        break;
                    default:
                        break;
                }

            }
        };
        radioFilter.setOnCheckedChangeListener(checkedChangeListener);
        pd = new ProgressDialog(getActivity());
        pd.setMessage("Getting location... Please wait!");

        btnPending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPendingVisits();
            }
        });
        SharedPreferences pref = AppControllerUtil.getPrefs();
        String home = pref.getString(Constants.PREF_HOME_COORDINATES, null);
        String office = pref.getString(Constants.PREF_OFFICE_COORDINATES, null);
        if (home == null || !home.contains(",")) {
            view.findViewById(R.id.rb_home).setEnabled(false);
        } else {
            String[] cords = home.split(",");
            double lat = Double.parseDouble(cords[0]);
            double lon = Double.parseDouble(cords[1]);
            homeCoordinates = new LatLng(lat, lon);
        }
        if (office == null || !office.contains(",")) {
            view.findViewById(R.id.rb_office).setEnabled(false);
        } else {
            String[] cords = office.split(",");
            double lat = Double.parseDouble(cords[0]);
            double lon = Double.parseDouble(cords[1]);
            officeCoordinates = new LatLng(lat, lon);
        }
        isFirstTime = true;
        if (type == Constants.TYPE_FILTER_DEFAULT) {
           /* if (home == null) {
                if (office != null) {
                    filterOffice();
                } else {
                    filterCurrent();
                }
            } else {
                filterHome();
            }*/
            filterCurrent();
        } else if (type == Constants.TYPE_FILTER_OFFICE) {
            if (office != null) {
                filterOffice();
            } else {
                Toast.makeText(getContext(), "Office location is not available!", Toast.LENGTH_SHORT).show();
            }
        } else if (type == Constants.TYPE_FILTER_HOME) {
            if (office != null) {
                filterHome();
            } else {
                Toast.makeText(getContext(), "Home location is not available!", Toast.LENGTH_SHORT).show();
            }
        } else {
            filterCurrent();
        }
        return view;
    }

    private void loadMap() {
        log("load map");
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(MapFragment.this);
    }

    private void filterCurrent() {
//        if (isFirstTime) {
        lblLocationCategory.setText("Your location");
        ((RadioButton) view.findViewById(R.id.rb_current_location)).setChecked(true);
        if (!Utilities.isLocationEnabled(getActivity())) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
            dialog.setMessage("Couldn't get the location. " +
                    "Make sure location is enabled on the device");
            dialog.setPositiveButton("Go to Settings", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    Intent myIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    getActivity().startActivity(myIntent);
                    showedGpsSettings = true;
                    getActivity().onBackPressed();
                    //get gps
                }
            });
            dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    getActivity().onBackPressed();
                }
            });
            dialog.show();
        } else {
            if (mGoogleApiClient != null) {
                mGoogleApiClient.connect();
                pd.show();
            }
        }
        // }
       /* else {
            addFragment(MapFragment.getInstance(Constants.TYPE_FILTER_CURRENT));
        }*/
    }

    private void filterHome() {
        if (isFirstTime) {
            lblLocationCategory.setText("Home");
            ((RadioButton) view.findViewById(R.id.rb_home)).setChecked(true);
            origin = homeCoordinates;
            new loadData().execute();
        } else
            addFragment(MapFragment.getInstance(Constants.TYPE_FILTER_HOME));
    }

    private void filterOffice() {
        if (isFirstTime) {
            lblLocationCategory.setText("Office");
            ((RadioButton) view.findViewById(R.id.rb_office)).setChecked(true);
            origin = officeCoordinates;
            new loadData().execute();
        } else
            addFragment(MapFragment.getInstance(Constants.TYPE_FILTER_OFFICE));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.addMarker(new MarkerOptions()
                .position(origin))
                .setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_pin));

        map = new HashMap<>();
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                final ArrayList<CustomRoutePlan> data = map.get(marker.getId());
                if (data == null || data.size() == 0)
                    return false;
                Dialog dialog = new Dialog(getActivity());
                dialog.setContentView(R.layout.listview);
                dialog.setTitle(data.get(0).Customer_Name);
                ListView listView = (ListView) dialog.findViewById(R.id.listView);
                ArrayAdapter<CustomRoutePlan> adapter =
                        new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, data);
                listView.setAdapter(adapter);
                AdapterView.OnItemClickListener markerDialogListClickListener = new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        final CustomRoutePlan item = data.get(position);
                        final Dialog d = new Dialog(getContext());
                        d.setContentView(R.layout.dialog_route_details);
                        d.setTitle("Route Plan Details");
                        TextView tvCustomerName = (TextView) d.findViewById(R.id.tv_customer_name);
                        TextView tvClientName = (TextView) d.findViewById(R.id.tv_client_name);
                        TextView tvRoutePlanNumber = (TextView) d.findViewById(R.id.tv_route_plan_number);
                        TextView tvRoutePlanDate = (TextView) d.findViewById(R.id.tv_route_plan_date);
                        final RecyclerView recyclerView = (RecyclerView) d.findViewById(R.id.rv_list);

                        tvCustomerName.setText(item.Customer_Name);
                        tvClientName.setText(item.toString());
                        tvRoutePlanNumber.setText(String.valueOf(item.RoutePlanNumber));
                        tvRoutePlanDate.setText(Utilities.dateToString(item.RoutePlanDate, "yyyy-MM-dd", "MMM dd, yyyy"));

                        new AsyncTask<Void, Void, Boolean>() {
                            @Override
                            protected void onPreExecute() {
                                super.onPreExecute();
                            }

                            @Override
                            protected Boolean doInBackground(Void... params) {
                                dataReminder = (ArrayList<Reminder>) new Select()
                                        .from(Reminder.class)
                                        .where(Condition.column(Reminder$Table.DATE).greaterThanOrEq(Utilities.dateToString(Calendar.getInstance(), "yyyy-MM-dd HH:mm:ss")))
                                        .and(Condition.column(Reminder$Table.CLIENT_CLIENT_NUMBER).eq(item.Client_Id))
                                        .queryList();
                                return null;
                            }

                            @Override
                            protected void onPostExecute(Boolean s) {
                                super.onPostExecute(s);
                                if (dataReminder == null || dataReminder.size() == 0) {
                                    recyclerView.setVisibility(View.GONE);
                                } else {
                                    d.findViewById(R.id.lbl_reminders).setVisibility(View.VISIBLE);
                                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                                    MainRemainderListAdapter adapter = new MainRemainderListAdapter(getActivity(), dataReminder);
                                    recyclerView.setAdapter(adapter);
                                }
                            }
                        }.execute();

                        d.show();

                    }
                };
                listView.setOnItemClickListener(markerDialogListClickListener);
                dialog.show();

                return false;
            }
        });
        log("adding markers");
        if (Utilities.isConnectingToInternet(getActivity())) {
            if (groupedClients != null && groupedClients.size() > 0) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        routeMarkers = new ArrayList<>();
                        int i = 1;
                        for (ClientsGrouped cg : groupedClients) {
                            Marker marker = mMap.addMarker(new MarkerOptions()
                                    .position(cg.latLng)
                                    .icon(BitmapDescriptorFactory.fromBitmap(writeTextOnDrawable(R.drawable.ic_marker_green, String.valueOf(i++)))));

                            map.put(marker.getId(), cg.clients);
                            routeMarkers.add(marker);
                        }
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(origin, 10.0f));
                        new loadRoutes().execute();
                    }
                });
            }
        } else
            Toast.makeText(getContext(), "You must be connected to internet. Please turn on internet and try again!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnected(Bundle bundle) {
        if (pd.isShowing()) pd.dismiss();
        mLastLocation = LocationServices.FusedLocationApi
                .getLastLocation(mGoogleApiClient);

        if (mLastLocation != null) {
            double latitude = mLastLocation.getLatitude();
            double longitude = mLastLocation.getLongitude();
            origin = new LatLng(latitude, longitude);
            // Obtain the SupportMapFragment and get notified when the map is ready to be used.
            new loadData().execute();

        } else {
            Utilities.showAlertDialog(getContext(),
                    "Couldn't get your location. " +
                            "Make sure location is enabled on the device" +
                            " and try again!");
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (pd.isShowing()) pd.dismiss();
    }

    // loads data from database and draw markers
    private class loadData extends AsyncTask<Void, Void, String> {

        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(activity);
            pDialog.setMessage("Loading data... Please wait!");
            pDialog.setCancelable(false);
            pDialog.setCanceledOnTouchOutside(false);
            pDialog.show();
        }


        @Override
        protected String doInBackground(Void... params) {
            log("loading map data");
            String today = Utilities.dateToString(Calendar.getInstance(), "yyyy-MM-dd");
            ColumnAlias c0 = ColumnAlias.column("RoutePlan." + RoutePlan$Table.VISITTYPE).as("Visittype");
            ColumnAlias c1 = ColumnAlias.column("Customer." + Customer$Table.CUSTOMER_NAME).as("Customer_Name");
            ColumnAlias c2 = ColumnAlias.column("Customer." + Customer$Table.GEO_CORDINATES).as("Geo_Cordinates");
            ColumnAlias c3 = ColumnAlias.column("Client." + Client$Table.CLIENT_FIRST_NAME).as("Client_First_Name");
            ColumnAlias c4 = ColumnAlias.column("Client." + Client$Table.CLIENT_LAST_NAME).as("Client_Last_Name");
            ColumnAlias c5 = ColumnAlias.column("Client." + Client$Table.CLIENT_PHONE).as("Client_Phone");
            ColumnAlias c6 = ColumnAlias.column("Client." + Client$Table.CLIENT_MOBILE).as("Client_Mobile");
            ColumnAlias c7 = ColumnAlias.column("Client." + Client$Table.MARKETCLASS).as("Marketclass");
            ColumnAlias c8 = ColumnAlias.column("Client." + Client$Table.STECLASS).as("STEclass");
            ColumnAlias c9 = ColumnAlias.column("Customer." + Customer$Table.CUSTOMER_ID).as("Customer_Id");
            ColumnAlias c10 = ColumnAlias.column("RoutePlan." + RoutePlan$Table.ROUTENO).as("RoutePlanNumber");
            ColumnAlias c11 = ColumnAlias.column("RoutePlan." + RoutePlan$Table.DATE).as("RoutePlanDate");
            ColumnAlias c12 = ColumnAlias.column("Client." + Client$Table.CLIENT_NUMBER).as("Client_Id");
            routeVisit = new Select(c1, c2, c9, c3, c4, c5, c6, c7, c8, c10, c11, c12)
                    .from(com.field.datamatics.database.RoutePlan.class)
                    .join(Customer.class, Join.JoinType.INNER)
                    .on(Condition.column(ColumnAlias.columnWithTable("RoutePlan", RoutePlan$Table.CUSTOMER_CUSTOMER_ID))
                            .is(ColumnAlias.columnWithTable("Customer", Customer$Table.CUSTOMER_ID)))
                    .join(Client.class, Join.JoinType.INNER)
                    .on(Condition.column(ColumnAlias.columnWithTable("Client", Client$Table.CLIENT_NUMBER))
                            .is(ColumnAlias.columnWithTable("RoutePlan", RoutePlan$Table.CLIENT_CLIENT_NUMBER)))
                    .where(Condition.column(RoutePlan$Table.DATE).eq(today))
                    .and(Condition.column(RoutePlan$Table.VISITTYPE).eq(0))
                    .and(Condition.column(Customer$Table.GEO_CORDINATES).like("%,%"))
                    .queryCustomList(CustomRoutePlan.class);

            pendingVisit = new Select(c1, c2, c9, c3, c4, c5, c6, c7, c8, c10, c11, c12)
                    .from(com.field.datamatics.database.RoutePlan.class)
                    .join(Customer.class, Join.JoinType.INNER)
                    .on(Condition.column(ColumnAlias.columnWithTable("RoutePlan", RoutePlan$Table.CUSTOMER_CUSTOMER_ID))
                            .is(ColumnAlias.columnWithTable("Customer", Customer$Table.CUSTOMER_ID)))
                    .join(Client.class, Join.JoinType.INNER)
                    .on(Condition.column(ColumnAlias.columnWithTable("Client", Client$Table.CLIENT_NUMBER))
                            .is(ColumnAlias.columnWithTable("RoutePlan", RoutePlan$Table.CLIENT_CLIENT_NUMBER)))
                    .where(Condition.column(RoutePlan$Table.VISITTYPE).eq(2))
                    .and(Condition.column(Customer$Table.GEO_CORDINATES).like("%,%"))
                    .queryCustomList(CustomRoutePlan.class);

            log("iterating through map data routevisit");
            for (CustomRoutePlan rp : routeVisit) {
                if (rp.Geo_Cordinates != null && rp.Geo_Cordinates.contains(",")) {
                    String[] cords = rp.Geo_Cordinates.split(",");
                    double lat = Double.parseDouble(cords[0]);
                    double lon = Double.parseDouble(cords[1]);
                    rp.latLng = new LatLng(lat, lon);
                }
            }
            log("iterating through map data pending");
            for (CustomRoutePlan rp : pendingVisit) {
                if (rp.Geo_Cordinates != null && rp.Geo_Cordinates.contains(",")) {
                    String[] cords = rp.Geo_Cordinates.split(",");
                    double lat = (Double.parseDouble(cords[0]));
                    double lon = (Double.parseDouble(cords[1]));
                    rp.latLng = new LatLng(lat - 0.000094D, lon + 0.000097D);
                }
            }
            String status = "failed";
            if (routeVisit != null && routeVisit.size() > 0) {
                groupedClients = new ArrayList<>();
                log("started grouping");
                try {
                    groupClients(routeVisit, groupedClients);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                log("end grouping");
                status = "success";
            }
            if (pendingVisit != null && pendingVisit.size() > 0) {
                groupedPendingVisits = new ArrayList<>();
                try {
                    groupClients(pendingVisit, groupedPendingVisits);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                status = "success";
            }
            log("end loading map data");
            return status;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (pDialog.isShowing())
                pDialog.dismiss();
            if (s.equals("success")) {
                loadMap();
            } else {
                AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                dialog.setMessage("No routes available!");
                dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        getActivity().onBackPressed();
                    }
                });
                dialog.setCancelable(false);
                dialog.show();
            }
        }
    }

    private class loadRoutes extends AsyncTask<Void, Void, ArrayList<List<List<HashMap<String, String>>>>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            lblProgressRoute.setVisibility(View.VISIBLE);
        }


        @Override
        protected ArrayList<List<List<HashMap<String, String>>>> doInBackground(Void... params) {
            ArrayList<List<List<HashMap<String, String>>>> result = new ArrayList<>();
            LatLng o = origin;
            for (ClientsGrouped cg : groupedClients) {
                String url = getDirectionsUrl(o, cg.latLng);
                o = cg.latLng;
                String data = null;
                try {
                    data = downloadUrl(url);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (data != null) {
                    JSONObject jObject;
                    List<List<HashMap<String, String>>> routes = null;
                    try {
                        jObject = new JSONObject(data);
                        DirectionsJSONParser parser = new DirectionsJSONParser();
                        routes = parser.parse(jObject);
                        result.add(routes);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            return result;
        }

        @Override
        protected void onPostExecute(ArrayList<List<List<HashMap<String, String>>>> result) {
            super.onPostExecute(result);
            lblProgressRoute.setVisibility(View.INVISIBLE);
            for (int m = 0; m < result.size(); m++) {
                ArrayList<LatLng> points = null;
                PolylineOptions lineOptions = null;
                MarkerOptions markerOptions = new MarkerOptions();
                List<List<HashMap<String, String>>> routes = result.get(m);
                for (int i = 0; i < routes.size(); i++) {
                    points = new ArrayList<LatLng>();
                    lineOptions = new PolylineOptions();
                    List<HashMap<String, String>> path = routes.get(i);
                    for (int j = 0; j < path.size(); j++) {
                        HashMap<String, String> point = path.get(j);
                        double lat = Double.parseDouble(point.get("lat"));
                        double lng = Double.parseDouble(point.get("lng"));
                        LatLng position = new LatLng(lat, lng);
                        points.add(position);
                    }
                    // Adding all the points in the route to LineOptions
                    lineOptions.addAll(points);
                    lineOptions.width(10);
                    lineOptions.color(Color.BLUE);
                }
                // Drawing polyline in the Google Map for the i-th route
                try {
                    mMap.addPolyline(lineOptions);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void sortRoute(ArrayList<ClientsGrouped> grouped) {
        for (ClientsGrouped cg : grouped) {
            log("started for loop");
            String url = getDirectionsUrl(origin, cg.latLng);
            String data = null;
            try {
                log("downloading url....");
                data = downloadUrl(url);
            } catch (Exception e) {
                Log.d("Get Distance", e.toString());
            }
            if (data != null) {
                log("data is not null");
                JSONObject jObject;
                try {
                    jObject = new JSONObject(data);
                    log("json: " + jObject.toString());
                    DirectionsJSONParser parser = new DirectionsJSONParser();
                    String d = parser.getDistance(jObject);
                    log("distance : " + d);
                    if (d != null)
                        cg.distanceFromOrigin = Float.valueOf(d.replaceAll("[^\\d.]+|\\.(?!\\d)", ""));
                    log("json parsing success");
                } catch (Exception e) {
                    e.printStackTrace();
                    log("exception json");
                }
            }
        }
        log("out of for loop");
        Collections.sort(grouped, new Comparator<ClientsGrouped>() {
            @Override
            public int compare(ClientsGrouped c1, ClientsGrouped c2) {
                if (c1.distanceFromOrigin < c2.distanceFromOrigin) return -1;
                else if (c1.distanceFromOrigin == c2.distanceFromOrigin) return 0;
                return 1;
            }
        });
    }

    // to group clients under similar customers
    private void groupClients(List<CustomRoutePlan> ungrouped, ArrayList<ClientsGrouped> grouped) {
        Collections.sort(ungrouped, new Comparator<CustomRoutePlan>() {
            @Override
            public int compare(CustomRoutePlan customRoutePlan, CustomRoutePlan t1) {
                return customRoutePlan.Customer_Id - t1.Customer_Id;
            }
        });
        int lastCustomerId = 0;
        LatLng lastLatLong = null;
        int count = 0;
        int size = ungrouped.size();
        ArrayList<CustomRoutePlan> clients = new ArrayList<>();
        for (CustomRoutePlan cr : ungrouped) {
            count++;
            if (lastCustomerId == 0) {
                lastCustomerId = cr.Customer_Id;
                lastLatLong = cr.latLng;
            }
            if (lastCustomerId != cr.Customer_Id) {
                ClientsGrouped cg = new ClientsGrouped();
                cg.customerId = lastCustomerId;
                cg.latLng = lastLatLong;
                cg.clients = clients;
                grouped.add(cg);
                clients = new ArrayList<>();
                clients.add(cr);
            } else {
                clients.add(cr);
                if (count == size) {
                    ClientsGrouped cg = new ClientsGrouped();
                    cg.customerId = lastCustomerId;
                    cg.latLng = lastLatLong;
                    cg.clients = clients;
                    grouped.add(cg);
                }
            }
            lastCustomerId = cr.Customer_Id;
            lastLatLong = cr.latLng;
        }
        if (grouped != null)
            sortRoute(grouped);
    }

    private class ClientsGrouped {
        public int customerId;
        public LatLng latLng;
        public ArrayList<CustomRoutePlan> clients;
        public float distanceFromOrigin;
    }

    private Bitmap writeTextOnDrawable(int drawableId, String text) {

        Bitmap bm = BitmapFactory.decodeResource(getResources(), drawableId)
                .copy(Bitmap.Config.ARGB_8888, true);

        Typeface tf = Typeface.create("Helvetica", Typeface.BOLD);

        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);
        paint.setTypeface(tf);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(convertToPixels(getContext(), 11));

        Rect textRect = new Rect();
        paint.getTextBounds(text, 0, text.length(), textRect);

        Canvas canvas = new Canvas(bm);

        //If the text is bigger than the canvas , reduce the font size
        if (textRect.width() >= (canvas.getWidth() - 4))     //the padding on either sides is considered as 4, so as to appropriately fit in the text
            paint.setTextSize(convertToPixels(getContext(), 7));        //Scaling needs to be used for different dpi's

        //Calculate the positions
        int xPos = (canvas.getWidth() / 2) - 2;     //-2 is for regulating the x position offset

        //"- ((paint.descent() + paint.ascent()) / 2)" is the distance from the baseline to the center.
        int yPos = (int) ((canvas.getHeight() / 2) - ((paint.descent() + paint.ascent()) / 2));

        canvas.drawText(text, xPos, yPos, paint);

        return bm;
    }


    public static int convertToPixels(Context context, int nDP) {
        final float conversionScale = context.getResources().getDisplayMetrics().density;

        return (int) ((nDP * conversionScale) + 0.5f);

    }

    public void showPendingVisits() {
        if (groupedPendingVisits == null || groupedPendingVisits.size() == 0)
            return;
        String show = "Show Pending Visits";
        String hide = "Hide Pending Visits";
        if (btnPending.getText().equals(show)) {
            btnPending.setText(hide);
            int i = 1;
            if (pendingMarkers == null) {
                pendingMarkers = new ArrayList<>();
                for (ClientsGrouped cg : groupedPendingVisits) {
                    Marker marker = mMap.addMarker(new MarkerOptions()
                            .position(cg.latLng)
                            .icon(BitmapDescriptorFactory.fromBitmap(writeTextOnDrawable(R.drawable.ic_marker_red, String.valueOf(i++)))));
                    map.put(marker.getId(), cg.clients);
                    pendingMarkers.add(marker);
                }

            } else {
                for (Marker m : pendingMarkers) {
                    m.setVisible(true);
                }
            }
        } else {
            btnPending.setText(show);
            if (pendingMarkers != null) {
                for (Marker m : pendingMarkers) {
                    m.setVisible(false);
                }
            }
        }
    }

    // prepare google api url to get direction between locations
    private String getDirectionsUrl(LatLng origin, LatLng dest) {
        if (origin == null || dest == null) return null;
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        log(str_dest);
        String sensor = "sensor=false";
        String mode = "driving";
        String parameters = str_origin + "&" + str_dest + "&" + mode + "&" + sensor;
        String output = "json";
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;
        return url;
    }

    /**
     * A method to download json data from url
     */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();
            iStream = urlConnection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
            StringBuffer sb = new StringBuffer();
            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            data = sb.toString();
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                iStream.close();
                urlConnection.disconnect();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
        return data;
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
    }

    /**
     * Method to verify google play services on the device
     */


    @Override
    public void onResume() {
        super.onResume();
        Utilities.checkPlayServices(getContext());
    }
}
