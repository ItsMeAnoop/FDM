package com.field.datamatics.views;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.field.datamatics.R;
import com.field.datamatics.constants.Constants;
import com.field.datamatics.database.Client;
import com.field.datamatics.database.Client$Table;
import com.field.datamatics.database.CustomClientPrefix;
import com.field.datamatics.database.Product;
import com.field.datamatics.database.ProductSample;
import com.field.datamatics.database.Reminder;
import com.field.datamatics.database.Reminder$Table;
import com.field.datamatics.database.RoutePlan;
import com.field.datamatics.database.RoutePlan$Table;
import com.field.datamatics.database.TimeSpendInMarket;
import com.field.datamatics.database.TimeSpendInMarket$Table;
import com.field.datamatics.database.User;
import com.field.datamatics.eventbus.AreaCoverageEvent;
import com.field.datamatics.eventbus.MediaSizeEvent;
import com.field.datamatics.eventbus.PerformanceChartEvent;
import com.field.datamatics.eventbus.ReminderEvent;
import com.field.datamatics.eventbus.ScoreEvent;
import com.field.datamatics.eventbus.TimeSpentEvent;
import com.field.datamatics.eventbus.VisitByClientEvent;
import com.field.datamatics.eventbus.VisitDoneEvent;
import com.field.datamatics.json.JoinClientRoutePlanJson;
import com.field.datamatics.json.ProductDetailJson;
import com.field.datamatics.json.VisitHomePageJson;
import com.field.datamatics.utils.AppControllerUtil;
import com.field.datamatics.utils.PreferenceUtil;
import com.field.datamatics.utils.Utilities;
import com.field.datamatics.views.fragments.AddReminder;
import com.field.datamatics.views.fragments.AdditionalVisit;
import com.field.datamatics.views.fragments.DashBoardNewDesign;
import com.field.datamatics.views.fragments.GlobalMessageFrament;
import com.field.datamatics.views.fragments.MakeRoutePlan;
import com.field.datamatics.views.fragments.MapFragment;
import com.field.datamatics.views.fragments.MonthlyVisit;
import com.field.datamatics.views.fragments.MrActions;
import com.field.datamatics.views.fragments.PendingTask;
import com.field.datamatics.views.fragments.ProductDetails;
import com.field.datamatics.views.fragments.ProductList;
import com.field.datamatics.views.fragments.ReportsFragment;
import com.field.datamatics.views.fragments.SamplesIssuedFragment;
import com.field.datamatics.views.fragments.SamplesIssuedInSession;
import com.field.datamatics.views.fragments.Schedule_vs_ActualFragment;
import com.field.datamatics.views.fragments.Settings;
import com.field.datamatics.views.fragments.TakeAnAppointment;
import com.field.datamatics.views.fragments.TodaysVisitTabbed;
import com.field.datamatics.views.fragments.VisitHomePage;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.raizlabs.android.dbflow.sql.builder.Condition;
import com.raizlabs.android.dbflow.sql.language.ColumnAlias;
import com.raizlabs.android.dbflow.sql.language.Join;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.raizlabs.android.dbflow.sql.language.Update;
import com.raizlabs.android.dbflow.sql.language.Where;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import de.greenrobot.event.EventBus;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private Toolbar toolbar;
    public Location mLastLocation;

    // Google client to interact with Google API
    private GoogleApiClient mGoogleApiClient;

    // boolean flag to toggle periodic location updates
    private boolean mRequestingLocationUpdates = false;

    private LocationRequest mLocationRequest;

    public NavigationView navigationView;
    public ArrayList<Reminder> reminders = null;
    public int monthlyScore = 0;
    public int todaysScore = 0;

    public LineData chartDataMonthPerformance;
    public LineData chartDataDailyPerformance;
    public String mediaSize = "0 B";
    public String timeSpent;

    public volatile boolean shouldUpdatePerformanceGraph;
    public volatile boolean shouldUpdateScore;
    public volatile boolean shouldUpdateReminder;
    public volatile boolean shouldUpdateMediaSize;
    public volatile boolean shouldUpdateTimeSpent;

    public BarData dataTimeSpentMonthly;
    public BarData dataTimeSpentDaily;
    public BarData dataVisitsByClientDaily;
    public BarData dataVisitsByClientMonthly;

    //is dashboard shown for the first time
    public volatile boolean isFirstTime = true;


    /**
     * .......Changes 03/01/16 Start.................
     */
    public static float[] vals_monthy;
    public static float[] vals_daily;
    public static int[] visit_montly;
    public static int[] visit_daily;
    public static float[] area_monthly;
    public static float[] area_daily;
    /**
     * .......Changes 03/01/16 End.................
     */

    private HashMap<Integer, String> score = new HashMap<>();
    public ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        updatePendingStatus();
        appTrialManagement();


        setSupportActionBar(toolbar);
        //save logged in status to preference
        PreferenceUtil.getIntsance().setIsLogin(true);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        TextView tvUserName = (TextView) navigationView.findViewById(R.id.tv_user_name);
        TextView tvEmail = (TextView) navigationView.findViewById(R.id.tv_email);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading... Please wait a moment.");
        progressDialog.setCancelable(false);

        User user = new Select().from(User.class).querySingle();
        if (user != null) {
            tvUserName.setText(user.getName());
            tvEmail.setText(user.Emailid1);
        }

        if (Utilities.checkPlayServices(this)) {
            buildGoogleApiClient();
            createLocationRequest();
        }
        if (getIntent().getBooleanExtra("show_notification", false)) {
            addFragment(new GlobalMessageFrament());
        } else {
            SharedPreferences prefs = AppControllerUtil.getPrefsResume();
            int currentActivity = prefs.getInt(Constants.PREF_CURRENT_ACTIVITY, 0);
            String serialized = null;
            boolean dontResume = false;
            Bundle data;
            VisitHomePageJson mrAction;
            String currentDate = Utilities.dateToString(Calendar.getInstance(), "yyyy-MM-dd");
            if (prefs.getString(Constants.PREF_DATE, "").equals(currentDate) && prefs.getBoolean(Constants.PREF_SHOULD_RESUME, false) && currentActivity != 0) {
                isFirstTime = false;
                switch (currentActivity) {
                    case Constants.STAT_TODAYS_VISIT:
                        addFragment(TodaysVisitTabbed.getInstance());
                        break;
                    case Constants.STAT_VISIT_HOME:
                    case Constants.STAT_VISIT_HOME_PENDING:
                        serialized = prefs.getString(Constants.VAL_VISIT_HOME_PAGE, null);
                        if (serialized == null) {
                            dontResume = true;
                        } else {
                            JoinClientRoutePlanJson client = new Gson().fromJson(serialized, JoinClientRoutePlanJson.class);
                            data = new Bundle();
                            ArrayList<String> values = new ArrayList<>();
                            values.add(client.Client_First_Name + " " + client.Client_Last_Name);
                            values.add(client.Client_Gender);
                            values.add(client.Speciality);
                            values.add(client.Marketclass);
                            values.add(client.STEclass);
                            values.add(client.Client_Email);
                            values.add(client.Client_Phone + "");
                            values.add(client.Client_Mobile + "");
                            values.add(client.Client_Fax);
                            values.add(client.Type);
                            values.add(client.Nationality);
                            values.add(client.CustomerName);
                            values.add(String.valueOf(client.routeNumber));
                            values.add(client.RoutePlanDate);
                            data.putStringArrayList("values", values);
                            data.putInt("route_plan_number", client.Route_Plan_Number);
                            data.putInt("apointment_id", client.Appointment_Id);
                            data.putInt("client_id", client.Client_Number);
                            if (currentActivity == Constants.STAT_VISIT_HOME_PENDING) {
                                data.putBoolean("is_pending", true);
                            }
                            addFragment(VisitHomePage.getInstance(data));
                        }
                        break;
                    case Constants.STAT_MR_ACTIONS:
                        serialized = prefs.getString(Constants.VAL_MR_ACTION, null);
                        if (serialized == null) {
                            dontResume = true;
                        } else {
                            goToMrAction(serialized);
                        }
                        break;
                    case Constants.STAT_PRODUCT_LIST:
                    case Constants.STAT_PRODUCT_DETAIL:
                        serialized = prefs.getString(Constants.VAL_MR_ACTION, null);
                        if (serialized == null) {
                            dontResume = true;
                        } else {
                            goToMrAction(serialized);
                            if (currentActivity == Constants.STAT_PRODUCT_DETAIL) {
                                String prodDetailJson = prefs.getString(Constants.VAL_PRDUCT_DETAIL, null);
                                if (prodDetailJson != null) {
                                    ProductDetailJson values = new Gson().fromJson(prodDetailJson, ProductDetailJson.class);
                                    mrAction = new Gson().fromJson(serialized, VisitHomePageJson.class);
                                    addFragment(ProductList.getInstance(mrAction.routePlanNumber, mrAction.clientId, Calendar.getInstance(), values));
                                    MrActions.fragment_use = "PRODUCT";
                                    String pJson = prefs.getString(Constants.VAL_PRDUCTS, null);
                                    if (pJson != null) {
                                        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
                                        Product product = gson.fromJson(pJson, Product.class);
                                        Log.i("Product", product.Product_Number);
                                        addFragment(ProductDetails.getInstance(product, mrAction.routePlanNumber, Calendar.getInstance()));
                                    }
                                }
                            } else {
                                mrAction = new Gson().fromJson(serialized, VisitHomePageJson.class);
                                addFragment(ProductList.getInstance(mrAction.routePlanNumber, mrAction.clientId, Calendar.getInstance()));
                                MrActions.fragment_use = "PRODUCT";
                            }
                        }

                        break;
                    case Constants.STAT_PRODUCT_SAMPLE:
                    case Constants.STAT_PRODUCT_SAMPLE_DETAIL:
                        serialized = prefs.getString(Constants.VAL_MR_ACTION, null);
                        if (serialized == null) {
                            dontResume = true;
                        } else {
                            goToMrAction(serialized);
                            if (currentActivity == Constants.STAT_PRODUCT_SAMPLE_DETAIL) {
                                String prodDetailJson = prefs.getString(Constants.VAL_PRDUCT_DETAIL, null);
                                if (prodDetailJson != null) {
                                    ProductDetailJson values = new Gson().fromJson(prodDetailJson, ProductDetailJson.class);
                                    mrAction = new Gson().fromJson(serialized, VisitHomePageJson.class);
                                    MrActions.fragment_use = "SAMPLE";
                                    addFragment(ProductList.getInstance(mrAction.routePlanNumber, mrAction.clientId, Calendar.getInstance(), values));
                                    String pJson = prefs.getString(Constants.VAL_PRDUCTS, null);
                                    if (pJson != null) {
                                        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
                                        Product product = gson.fromJson(pJson, Product.class);
                                        ProductSample sample = null;
                                        pJson = prefs.getString(Constants.VAL_SAMPLES, null);
                                        if (pJson != null) {
                                            sample = gson.fromJson(pJson, ProductSample.class);
                                        }
                                        addFragment(ProductDetails.getInstance(product, sample, mrAction.routePlanNumber, Calendar.getInstance()));
                                    }
                                }
                            } else {
                                mrAction = new Gson().fromJson(serialized, VisitHomePageJson.class);
                                MrActions.fragment_use = "SAMPLE";
                                addFragment(ProductList.getInstance(mrAction.routePlanNumber, mrAction.clientId, Calendar.getInstance()));
                            }
                        }
                        break;
                    case Constants.STAT_REMINDER:
                    case Constants.STAT_SURVEY:
                    case Constants.STAT_NOTES:
                    case Constants.STAT_SIGNATURE:
                        serialized = prefs.getString(Constants.VAL_MR_ACTION, null);
                        if (serialized == null) {
                            dontResume = true;
                        } else {
                            mrAction = new Gson().fromJson(serialized, VisitHomePageJson.class);
                            data = new Bundle();
                            data.putInt("route_plan_number", mrAction.routePlanNumber);
                            data.putInt("appointment_id", mrAction.appointmentId);
                            data.putInt("client_id", mrAction.clientId);
                            data.putString("checkin", mrAction.checkInTime);
                            data.putString("checkout", mrAction.checkoutTime);
                            data.putString("checkin_coordinates", mrAction.checkinCoordinates);
                            data.putString("checkout_coordinates", mrAction.checkoutCoordinates);
                            data.putBoolean("is_pending", mrAction.isPending);
                            AppControllerUtil.getInstance().setIsCheckIn(false);
                            switch (currentActivity) {
                                case Constants.STAT_SURVEY:
                                    addFragment(MrActions.goToSurvey(data));
                                    break;
                                case Constants.STAT_NOTES:
                                    addFragment(MrActions.showNotesDialog(data));
                                    break;
                                case Constants.STAT_SIGNATURE:
                                    addFragment(MrActions.goToSignature(data));
                                    break;
                                case Constants.STAT_REMINDER:
                                    addFragment(AddReminder.getInstance(mrAction.clientId, mrAction.routePlanNumber));
                                    break;
                            }

                        }
                        break;
                    case Constants.STAT_PENDING_TASK:
                        addFragment(PendingTask.getInstance("PendingVisit"));
                        break;
                    case Constants.STAT_ADDITIONAL_VISIT:
                        addFragment(AdditionalVisit.getInstance("AdditionalVisit"));
                        break;
                    default:
                        break;
                }
            } else {
                prefs.edit().clear().commit();
                dontResume = true;
            }
            if (dontResume) {
                Log.i("FDM", "loading charts");
                addFragment(DashBoardNewDesign.getNewInstance());
//                loadReminders();
//                loadPerformanceGraph();
//                loadScores();
//                loadMediaSize();
//                /**.......Changes 03/01/16 Start.................*/
//                loadTimeSpent();
//                loadVisitDone();
//                loadAreaCoverage();
                /**.......Changes 03/01/16 End.................*/
            } else {
                Log.i("FDM", "not loading charts");
            }
        }


    }

    private void goToMrAction(String serialized) {
        VisitHomePageJson mrAction = new Gson().fromJson(serialized, VisitHomePageJson.class);
        Bundle data = new Bundle();
        data.putInt("route_plan_number", mrAction.routePlanNumber);
        data.putInt("appointment_id", mrAction.appointmentId);
        data.putInt("client_id", mrAction.clientId);
        data.putString("checkin", mrAction.checkInTime);
        data.putString("checkout", mrAction.checkoutTime);
        data.putString("checkin_coordinates", mrAction.checkinCoordinates);
        data.putString("checkout_coordinates", mrAction.checkoutCoordinates);
        data.putBoolean("is_pending", mrAction.isPending);
        addFragment(MrActions.getInstance(data, true));
        AppControllerUtil.getInstance().setIsCheckIn(false);
    }

    @Override
    public void onBackPressed() {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (AppControllerUtil.getInstance().isCheckIn()) {
            showMessage("You can't go back because you are Checked In.", toolbar);
            return;
        }
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if(getCurrentFragmentName()==null)
                AppControllerUtil.getInstance().setCurrent_fragment("TodayVisit");
            if (getCurrentFragmentName().equals("")) {
                finish();
            } else if (getCurrentFragmentName().equals("DashBoard")) {
                DashBoardNewDesign.clearInstance();
                finish();
            }
            //flow-----
            else if (getCurrentFragmentName().equals("TodayVisit")) {
                AppControllerUtil.getPrefsResume().edit().clear().commit();
                addFragment(DashBoardNewDesign.getInstance());

            } else if (getCurrentFragmentName().equals("CustomerList")) {
                addFragment(TodaysVisitTabbed.getInstance());

            } else if (getCurrentFragmentName().equals("VisitHomePage")) {
                AppControllerUtil.getPrefsResume().edit().clear().commit();
                addFragment(TodaysVisitTabbed.getInstance());

            }
            else if (getCurrentFragmentName().equals("Today Visit Details")) {
                AppControllerUtil.getPrefsResume().edit().clear().commit();
                addFragment(TodaysVisitTabbed.getInstance());
            }
            else if (getCurrentFragmentName().equals("Pending Visit Details")) {
                AppControllerUtil.getPrefsResume().edit().clear().commit();
                addFragment(TodaysVisitTabbed.getPendingVisitTabbed());
            }
            else if (getCurrentFragmentName().equals("Additional Visit Details")) {
                AppControllerUtil.getPrefsResume().edit().clear().commit();
                addFragment(TodaysVisitTabbed.getAdditionalVisitTabbed());
            }
            else if (getCurrentFragmentName().equals("PendingDetails")) {
                addFragment(PendingTask.getInstance("PendingVisit"));

            } else if (getCurrentFragmentName().equals("MrActions")) {
                if (AppControllerUtil.getInstance().isSessionStart()) {
                    showMessage("Save session before exiting", toolbar);
                    return;
                }
                addFragment(new VisitHomePage());

            } else if (getCurrentFragmentName().equals("MrActions_Pending")) {
                if (AppControllerUtil.getInstance().isSessionStart()) {
                    showMessage("Save session before exiting", toolbar);
                    return;
                }

            } else if (getCurrentFragmentName().equals("CustomProductList")) {
                ProductList.clearInstance();
                addFragment(MrActions.getInstance(Bundle.EMPTY, false));

            } else if (getCurrentFragmentName().equals("ProductDetails")) {
                addFragment(ProductList.getInstance(0, 0, null));

            } else if (getCurrentFragmentName().equals("Image_video_tab")) {
                addFragment(ProductDetails.getmInstance());

            } else if (getCurrentFragmentName().equals("ReminderList")) {
                addFragment(MrActions.getInstance(Bundle.EMPTY, false));

            } else if (getCurrentFragmentName().equals("TakeSurvey")) {
                addFragment(MrActions.getInstance(Bundle.EMPTY, false));

            } else if (getCurrentFragmentName().equals("SaveSignature")) {
                addFragment(MrActions.getInstance(Bundle.EMPTY, false));

            } else if (getCurrentFragmentName().equals("SampleIntroduction")) {
                addFragment(MrActions.getInstance(Bundle.EMPTY, false));
            }

            //flow---
            else if (getCurrentFragmentName().equals("DashBoardRemainderList")) {
                addFragment(DashBoardNewDesign.getInstance());

            } else if (getCurrentFragmentName().equals("MapFragment")) {
                addFragment(DashBoardNewDesign.getInstance());

            } else if (getCurrentFragmentName().equals(ReportsFragment.class.getName())) {
                addFragment(DashBoardNewDesign.getInstance());
            }

            //flow---
            else if (getCurrentFragmentName().equals("TakeAnAppointment")) {
                addFragment(DashBoardNewDesign.getInstance());

            } else if (getCurrentFragmentName().equals("TakeNewAppointment")) {
                addFragment(new TakeAnAppointment());

            }

            //flow---
            else if (getCurrentFragmentName().equals("MonthlyVisit")) {
                addFragment(DashBoardNewDesign.getInstance());

            } else if (getCurrentFragmentName().equals("ClientList")) {
                addFragment(new MonthlyVisit());

            } else if (getCurrentFragmentName().equals("PendingTask_monthly_visit")) {
                addFragment(new MonthlyVisit());

            }

            //flow---
            else if (getCurrentFragmentName().equals("PendingVisit")) {
                AppControllerUtil.getPrefsResume().edit().clear().commit();
                addFragment(DashBoardNewDesign.getInstance());

            }
            //flow---
            else if (getCurrentFragmentName().equals("ScoreCard")) {
                addFragment(DashBoardNewDesign.getInstance());

            }
            else if (getCurrentFragmentName().equals("AdditionalVisitedList")) {
                addFragment(DashBoardNewDesign.getInstance());

            }

            //flow---
            else if (getCurrentFragmentName().equals("GlobalMessageFrament")) {
                addFragment(DashBoardNewDesign.getInstance());

            }

            //flow---
            else if (getCurrentFragmentName().equals("Settings")) {
                addFragment(DashBoardNewDesign.getInstance());

            }

            //flow---
            else if (getCurrentFragmentName().equals("MakeRoutePlan")) {
                addFragment(DashBoardNewDesign.getInstance());
            } else if (getCurrentFragmentName().equals("AddClient")) {
                addFragment(new MakeRoutePlan());
            } else if (getCurrentFragmentName().equals("AddReminder")) {
                addFragment(MrActions.getInstance(Bundle.EMPTY, false));
            } else if (getCurrentFragmentName().equals("AddReminderPending")) {
                //addFragment(TodaysVisitTabbed.getInstance());
                getSupportFragmentManager().popBackStack();
            } else if (getCurrentFragmentName().equals(SamplesIssuedFragment.class.getName())) {
                addFragment(DashBoardNewDesign.getInstance());
            } else if (getCurrentFragmentName().equals(Schedule_vs_ActualFragment.class.getSimpleName())) {
                addFragment(DashBoardNewDesign.getInstance());
            } else if (getCurrentFragmentName().equals("ScheduleVsActualDetails")) {
                addFragment(Schedule_vs_ActualFragment.getInstance(false, "", "", 0));
            } else if (getCurrentFragmentName().equals(SamplesIssuedInSession.class.getName())) {
                addFragment(ProductList.getInstance(0, 0, null));
            } else if (getCurrentFragmentName().equals("ProductInfo")) {
                addFragment(ProductDetails.getmInstance());
            } else if (getCurrentFragmentName().equals("ProductInfoIssued")) {
                addFragment(SamplesIssuedInSession.getInstance(MrActions.routePlanNumber));
            } else
                super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(final MenuItem item) {
        // Handle navigation view item clicks here.
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        String currentFragment = getCurrentFragmentName();
        if(currentFragment==null) {
            addFragment(DashBoardNewDesign.getInstance());
            return true;
        }
        if (currentFragment.equals("MrActions") || currentFragment.equals("ProductInfo")
                || currentFragment.equals("ProductDetails") || currentFragment.equals("CustomProductList")
                || currentFragment.equals("AddReminder") || currentFragment.equals("AddReminderPending")
                || currentFragment.equals("TakeSurvey") || currentFragment.equals("SaveSignature")
                || currentFragment.equals("ProductInfoIssued") || currentFragment.equals(SamplesIssuedInSession.class.getName())) {
            if (AppControllerUtil.getInstance().isSessionStart()) {
                showMessage("Save session before exiting", toolbar);
                navigationView.setCheckedItem(R.id.nav_dashboard);
                return true;
            }
        }
        if (AppControllerUtil.getInstance().isCheckIn()) {
            showMessage("You can't go back because you are Checked In.", toolbar);
            navigationView.setCheckedItem(R.id.nav_dashboard);
            return true;
        }
        AppControllerUtil.getPrefsResume().edit().clear().commit();
        final int id = item.getItemId();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (id == R.id.nav_dashboard) {
                    addFragment(DashBoardNewDesign.getInstance());

                } else if (id == R.id.nav_todays_visit) {
                    addFragment(TodaysVisitTabbed.getNewInstance());
                } else if (id == R.id.nav_route_plan) {
                    addFragment(MapFragment.getInstance(Constants.TYPE_FILTER_DEFAULT));
                } else if (id == R.id.nav_appointments) {
                    addFragment(new TakeAnAppointment());
                } else if (id == R.id.nav_monthly_visits) {
                    addFragment(new MonthlyVisit());
                } else if (id == R.id.nav_global_message) {
                    addFragment(new GlobalMessageFrament());
                } else if (id == R.id.nav_settings) {
                    addFragment(new Settings());
                } else if (id == R.id.nav_logout) {
                    PreferenceUtil.getIntsance().setIsLogin(false);
                    AppControllerUtil.setLoginStatus(false);
                    Intent intent = new Intent(getApplicationContext(), Login.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }
            }
        }, 300);

        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("Container", "resumed");
        if (mGoogleApiClient.isConnected() && mRequestingLocationUpdates) {
            startLocationUpdates();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("Container", "paussed");
        stopLocationUpdates();
    }

    public void refresh() {
        if (shouldUpdatePerformanceGraph) {
            shouldUpdatePerformanceGraph = false;
            // progressDialog.show();
            loadPerformanceGraph();
        }
        if (shouldUpdateScore) {
            shouldUpdateScore = false;
            loadScores();
        }
        if (shouldUpdateReminder) {
            shouldUpdateReminder = false;
            loadReminders();
        }
        if (shouldUpdateMediaSize) {
            shouldUpdateMediaSize = false;
            loadMediaSize();
        }
        if (shouldUpdateTimeSpent) {
            shouldUpdateTimeSpent = false;
            //loadTimeSpent();
        }
        /**.......Changes 03/01/16 Start.................*/
        loadTimeSpent();
        loadVisitDone();
        loadAreaCoverage();
        /**.......Changes 03/01/16 End.................*/
    }

    @Override
    public void onConnected(Bundle bundle) {
        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();

    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        Log.i("fdm_location", "got location : " + location.getLatitude());
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }


    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(MainActivity.this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(Constants.UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(Constants.FATEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(Constants.DISPLACEMENT);
    }

    protected void startLocationUpdates() {

        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);

    }

    protected void stopLocationUpdates() {
        try {
            LocationServices.FusedLocationApi.removeLocationUpdates(
                    mGoogleApiClient, this);
        } catch (Exception e) {

        }
    }

    public void setNavigationChecked(int id) {
        navigationView.setCheckedItem(id);
    }

    // load data for dashboard

    private void loadReminders() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                reminders = (ArrayList<Reminder>) new Select().from(Reminder.class)
                        .where()
                        .orderBy(true, Reminder$Table.DATE)
                        .limit(3)
                        .queryList();
                Log.i("Main", reminders.size() + "");
                EventBus.getDefault().postSticky(new ReminderEvent("success"));
            }
        }).start();
    }

    private void loadScores() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Calendar calendar = Calendar.getInstance();
                String today = Utilities.dateToString(calendar, "yyyy-MM-dd");
                long countScheduleToday = new Select().count().from(RoutePlan.class)
                        .where(Condition.column(RoutePlan$Table.DATE).eq(today))
                        .and(Condition.column(RoutePlan$Table.VISITTYPE).eq(0)).or(Condition.column(RoutePlan$Table.VISITTYPE).eq(2))
                        .and(Condition.column(RoutePlan$Table.AUTHORIZEDBY).isNot(0)).count();
                long countVisitedToday = new Select().count().from(RoutePlan.class)
                        .where(Condition.column(RoutePlan$Table.DATE).eq(today))
                        .and(Condition.column(RoutePlan$Table.VISITTYPE).eq(1)).or(Condition.column(RoutePlan$Table.VISITTYPE).eq(3))
                        .count();
                long countScheduleMonth = new Select().count().from(RoutePlan.class)
                        .where("strftime('%m', date) = ? and strftime('%Y', date) = '" + calendar.get(Calendar.YEAR) + "' ", Utilities.getMonthAsString(calendar.get(Calendar.MONTH)))
                        .and(Condition.column(RoutePlan$Table.VISITTYPE).eq(0)).or(Condition.column(RoutePlan$Table.VISITTYPE).eq(2))
                        .and(Condition.column(RoutePlan$Table.AUTHORIZEDBY).isNot(0)).count();
                long countVisitedMonth = new Select().count().from(RoutePlan.class)
                        .where("strftime('%m', date) = ? and strftime('%Y', date) = '" + calendar.get(Calendar.YEAR) + "' ", Utilities.getMonthAsString(calendar.get(Calendar.MONTH)))
                        .and(Condition.column(RoutePlan$Table.VISITTYPE).eq(1)).or(Condition.column(RoutePlan$Table.VISITTYPE).eq(3))
                        .count();
                Log.i("Scores", String.format("st:%d,vt:%d,sm:%d,vm:%d", countScheduleToday, countVisitedToday, countScheduleMonth, countVisitedMonth));
                monthlyScore = calculatePercentage(countVisitedMonth, countScheduleMonth + countVisitedMonth);
                todaysScore = calculatePercentage(countVisitedToday, countScheduleToday + countVisitedToday);
                EventBus.getDefault().postSticky(new ScoreEvent("success"));
            }
        }).start();
    }

    private void loadPerformanceGraph() {
        Log.i("FDM", "loading performance graph");

        new Thread(new Runnable() {
            @Override
            public void run() {
                Calendar calendar = Calendar.getInstance();
                int numDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                ArrayList<String> days = new ArrayList<>();
                ArrayList<ILineDataSet> sets = new ArrayList<>();
                ArrayList<Entry> e1 = new ArrayList<>();
                ArrayList<Entry> e2 = new ArrayList<>();

                //for daily graph
                ArrayList<String> xaxis = new ArrayList<>();
                ArrayList<ILineDataSet> setDialy = new ArrayList<>();
                ArrayList<Entry> ed1 = new ArrayList<>();
                ArrayList<Entry> ed2 = new ArrayList<>();

                for (int i = 1; i <= numDays; i++) {
                    days.add(Integer.toString(i));
                }

                List<RoutePlan> routePlans = new Select().from(RoutePlan.class)
                        .where("strftime('%m', date) = ? and strftime('%Y', date) = '" + calendar.get(Calendar.YEAR) + "' ", Utilities.getMonthAsString(calendar.get(Calendar.MONTH)))
                        .and(Condition.column(RoutePlan$Table.AUTHORIZEDBY).isNot(0))
                        .orderBy(true, RoutePlan$Table.DATE)
                        .queryList();

                if (routePlans != null) {
                    int visitCount = 0;
                    int pendingCount = 0;
                    int scheduleCount = 0;
                    int pos = 0;
                    int size = routePlans.size();
                    for (RoutePlan r : routePlans) {
                        if (pos == 0) {
                            scheduleCount++;
                            if (r.Visittype == 1 || r.Visittype == 3) {
                                visitCount++;
                            } else {
                                pendingCount++;
                            }
                        } else {
                            String lastDate = routePlans.get(pos - 1).Date;
                            if (lastDate.equals(r.Date)) {
                                scheduleCount++;
                                if (r.Visittype == 1 || r.Visittype == 3) {
                                    visitCount++;
                                } else {
                                    pendingCount++;
                                }
                            } else {
                                int d = Integer.parseInt(Utilities.dateToString(lastDate, "yyyy-MM-dd", "dd"));
                                e1.add(new Entry(visitCount, d - 1));
                                e2.add(new Entry(scheduleCount, d - 1));
                                visitCount = 0;
                                pendingCount = 0;
                                scheduleCount = 0;
                                scheduleCount++;
                                if (r.Visittype == 1 || r.Visittype == 3) {
                                    visitCount++;
                                } else {
                                    pendingCount++;
                                }
                            }
                        }
                        if (pos == size - 1) {
                            int d = Integer.parseInt(Utilities.dateToString(r.Date, "yyyy-MM-dd", "dd"));
                            e1.add(new Entry(visitCount, d - 1));
                            e2.add(new Entry(scheduleCount, d - 1));
                        }
                        pos++;
                    }
                }
                LineDataSet d1 = new LineDataSet(e1, "Actual");
                d1.setLineWidth(3.5f);
                d1.setCircleSize(4.5f);
                d1.setColor(Color.rgb(17, 171, 23));
                d1.setHighLightColor(Color.rgb(244, 117, 117));
                d1.setCircleColor(Color.rgb(17, 171, 23));
                d1.setDrawValues(false);

                LineDataSet d2 = new LineDataSet(e2, "Schedule");
                d2.setLineWidth(3.5f);
                d2.setCircleSize(4.5f);
                d2.setHighLightColor(Color.rgb(244, 117, 117));
                d2.setColor(Color.rgb(183, 28, 28));
                d2.setCircleColor(Color.rgb(183, 28, 28));
                d2.setDrawValues(false);

                sets.add(d2);
                sets.add(d1);

                //data for daily chart
                Calendar today = Calendar.getInstance();
                Calendar lastWeek = Calendar.getInstance();
                if (lastWeek.get(Calendar.DAY_OF_MONTH) < 8) {
                    lastWeek.add(Calendar.DAY_OF_MONTH, 1 - lastWeek.get(Calendar.DAY_OF_MONTH));
                } else
                    lastWeek.add(Calendar.DAY_OF_MONTH, -6);

                List<RoutePlan> routePlansDaily = new Select().from(RoutePlan.class)
                        .where(Condition.column(RoutePlan$Table.DATE).between(Utilities.dateToString(lastWeek, "yyyy-MM-dd"))
                                .and(Utilities.dateToString(today, "yyyy-MM-dd")))
                        .and(Condition.column(RoutePlan$Table.AUTHORIZEDBY).isNot(0))
                        .orderBy(true, RoutePlan$Table.DATE)
                        .queryList();

                int thisDay = Integer.parseInt(Utilities.dateToString(today, "dd"));
                int lastWeekDay = Integer.parseInt(Utilities.dateToString(lastWeek, "dd"));
                int numXvals = thisDay - lastWeekDay + 1;
                for (int i = lastWeekDay; i <= thisDay; i++) {
                    xaxis.add(Integer.toString(i));
                }
                int[] visitCount = new int[numXvals];
                int[] scheduleCount = new int[numXvals];
                if (routePlansDaily != null) {
                    for (RoutePlan r : routePlansDaily) {
                        int d = Integer.parseInt(Utilities.dateToString(r.Date, "yyyy-MM-dd", "dd"));
                        int dif = d - lastWeekDay;
                        scheduleCount[dif]++;
                        if (r.Visittype == 1 || r.Visittype == 3) {
                            visitCount[dif]++;
                        }
                    }
                }
                for (int i = 0; i < numXvals; i++) {
                    ed1.add(new Entry(visitCount[i], i));
                    ed2.add(new Entry(scheduleCount[i], i));
                }
                LineDataSet dd1 = new LineDataSet(ed1, "Actual");
                dd1.setLineWidth(3.5f);
                dd1.setCircleSize(4.5f);
                dd1.setColor(Color.rgb(17, 171, 23));
                dd1.setHighLightColor(Color.rgb(244, 117, 117));
                dd1.setCircleColor(Color.rgb(17, 171, 23));
                dd1.setDrawValues(false);

                LineDataSet dd2 = new LineDataSet(ed2, "Schedule");
                dd2.setLineWidth(3.5f);
                dd2.setCircleSize(4.5f);
                dd2.setHighLightColor(Color.rgb(244, 117, 117));
                dd2.setColor(Color.rgb(183, 28, 28));
                dd2.setCircleColor(Color.rgb(183, 28, 28));
                dd2.setDrawValues(false);

                setDialy.add(dd2);
                setDialy.add(dd1);

                ValueFormatter formatter = new MyValueFormatter();
                chartDataMonthPerformance = new LineData(days, sets);
                chartDataDailyPerformance = new LineData(xaxis, setDialy);
                chartDataMonthPerformance.setValueFormatter(formatter);
                chartDataDailyPerformance.setValueFormatter(formatter);

                Log.i("FDM", "Sending performance event");
                EventBus.getDefault().post(new PerformanceChartEvent("success"));


                /*load data for visits by client chart*/
                ColumnAlias c1 = ColumnAlias.column("Client." + Client$Table.CLIENT_PREFIX).as("clientPrefix");
                List<CustomClientPrefix> prefixes = new Select(c1).distinct()
                        .from(RoutePlan.class)
                        .join(Client.class, Join.JoinType.INNER)
                        .on(Condition.column(ColumnAlias.columnWithTable("RoutePlan", RoutePlan$Table.CLIENT_CLIENT_NUMBER))
                                .is(ColumnAlias.columnWithTable("Client", Client$Table.CLIENT_NUMBER)))
                        .queryCustomList(CustomClientPrefix.class);
                //x values
                ArrayList<String> xVals1 = new ArrayList<>();
                xVals1.add("week-1");
                xVals1.add("week-2");
                xVals1.add("week-3");
                xVals1.add("week-4");

                int size = prefixes.size();
                ArrayList<float[]> vals = new ArrayList<float[]>();
                for (int i = 0; i < 4; i++) {
                    vals.add(new float[size]);
                }

                ArrayList<String> prefixName = new ArrayList<String>();
                for (CustomClientPrefix cp : prefixes) {
                    prefixName.add(cp.clientPrefix);
                }
                if (routePlans != null) {
                    for (RoutePlan r : routePlans) {
                        int d = Integer.parseInt(Utilities.dateToString(r.Date, "yyyy-MM-dd", "dd"));
                        if (d < 8) {
                            if (r.Visittype == 1 || r.Visittype == 3)
                                vals.get(0)[prefixName.indexOf(r.client.Client_Prefix)]++;
                        } else if (d < 15) {
                            if (r.Visittype == 1 || r.Visittype == 3)
                                vals.get(1)[prefixName.indexOf(r.client.Client_Prefix)]++;
                        } else if (d < 22) {
                            if (r.Visittype == 1 || r.Visittype == 3)
                                vals.get(2)[prefixName.indexOf(r.client.Client_Prefix)]++;
                        } else if (d < 32) {
                            if (r.Visittype == 1 || r.Visittype == 3)
                                vals.get(3)[prefixName.indexOf(r.client.Client_Prefix)]++;
                        }
                    }
                }
                ArrayList<BarEntry> yVals1 = new ArrayList<>();
                for (int i = 0; i < vals.size(); i++) {
                    yVals1.add(new BarEntry(vals.get(i), i));
                }
                BarDataSet set1 = new BarDataSet(yVals1, "");
                if (size > 0)
                    set1.setColors(getColors(size));
                set1.setStackLabels(prefixName.toArray(new String[prefixName.size()]));
                ArrayList<IBarDataSet> dataSets1 = new ArrayList<>();
                dataSets1.add(set1);

                dataVisitsByClientMonthly = new BarData(xVals1, dataSets1);
                dataVisitsByClientMonthly.setValueFormatter(formatter);

                //load data for daily graph
                xVals1 = new ArrayList<>();
                for (int i = lastWeekDay; i <= thisDay; i++) {
                    xVals1.add(Integer.toString(i));
                }
                vals = new ArrayList<>();
                for (int i = 0; i < numXvals; i++) {
                    vals.add(new float[size]);
                }

                if (routePlansDaily != null) {
                    for (RoutePlan r : routePlansDaily) {
                        int d = Integer.parseInt(Utilities.dateToString(r.Date, "yyyy-MM-dd", "dd"));
                        int dif = d - lastWeekDay;
                        if (r.Visittype == 1 || r.Visittype == 3)
                            vals.get(dif)[prefixName.indexOf(r.client.Client_Prefix)]++;
                    }
                }

                yVals1 = new ArrayList<>();
                for (int i = 0; i < vals.size(); i++) {
                    yVals1.add(new BarEntry(vals.get(i), i));
                }
                set1 = new BarDataSet(yVals1, "");
                if (size > 0)
                    set1.setColors(getColors(size));
                dataSets1 = new ArrayList<>();
                dataSets1.add(set1);

                dataVisitsByClientDaily = new BarData(xVals1, dataSets1);
                dataVisitsByClientDaily.setValueFormatter(formatter);
                Log.i("FDM", "Sending visit by client event");
                EventBus.getDefault().post(new VisitByClientEvent(prefixName.toArray(new String[prefixName.size()])));
            }
        }).start();
    }

    private int[] getColors(int stacksize) {
        int[] colors = new int[stacksize];

        for (int i = 0; i < stacksize; i++) {
            colors[i] = Constants.CHART_COLORS[i];
        }

        return colors;
    }

    private class MyValueFormatter implements ValueFormatter {

        private DecimalFormat mFormat;

        public MyValueFormatter() {
            mFormat = new DecimalFormat("###,###,###,##");
        }

        @Override
        public String getFormattedValue(float value, Entry entry, int i, ViewPortHandler viewPortHandler) {

            if (value > 0)
                return mFormat.format(value);
            else
                return "";
        }
    }

    private void loadMediaSize() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String productDocLoc = AppControllerUtil.getPrefs().getString(Constants.PREF_PRODUCT_DOCUMENT_LOCATION, Constants.PRODUCT_DOCS);
                long sizeInBytes = Utilities.dirSize(new File(productDocLoc));
                mediaSize = Utilities.humanReadableByteCount(sizeInBytes, true);
                Log.i("FDM", "mediasize : " + mediaSize + " (" + sizeInBytes + ")");
                EventBus.getDefault().post(new MediaSizeEvent("success"));
            }
        }).start();
    }

    /**
     * .......Changes 03/01/16 Start.................
     */
    private void loadTimeSpent() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //data for monthly graph
                ArrayList<String> xVals1 = new ArrayList<String>();
                xVals1.add("week-1");
                xVals1.add("week-2");
                xVals1.add("week-3");
                xVals1.add("week-4");

                float[] vals = new float[4];

                Calendar calendar = Calendar.getInstance();
                List<TimeSpendInMarket> timeSpent = new Select().from(TimeSpendInMarket.class)
                        .where("strftime('%m', date_) = ? and strftime('%Y', date_) = '" + calendar.get(Calendar.YEAR) + "' ", Utilities.getMonthAsString(calendar.get(Calendar.MONTH)))
                        .orderBy(TimeSpendInMarket$Table.DATE_)
                        .queryList();
                if (timeSpent != null) {
                    for (TimeSpendInMarket t : timeSpent) {
                        int d = Integer.parseInt(Utilities.dateToString(t.date_, "yyyy-MM-dd", "dd"));
                        if (d < 8) {
                            vals[0] += t.time_spend;
                        } else if (d < 15) {
                            vals[1] += t.time_spend;
                        } else if (d < 22) {
                            vals[2] += t.time_spend;
                        } else if (d < 32) {
                            vals[3] += t.time_spend;
                        }
                    }
                }
                vals_monthy = vals;
                ArrayList<BarEntry> yVals1 = new ArrayList<>();
                yVals1.add(new BarEntry(Utilities.roundFloat(vals[0] / 3600f, 2), 0));
                yVals1.add(new BarEntry(Utilities.roundFloat(vals[1] / 3600f, 2), 1));
                yVals1.add(new BarEntry(Utilities.roundFloat(vals[2] / 3600f, 2), 2));
                yVals1.add(new BarEntry(Utilities.roundFloat(vals[3] / 3600f, 2), 3));

                BarDataSet set1 = new BarDataSet(yVals1, "");
                set1.setColors(getColors(4));
                ArrayList<IBarDataSet> dataSets1 = new ArrayList<>();
                dataSets1.add(set1);
                //  set1.setDrawValues(false);

                dataTimeSpentMonthly = new BarData(xVals1, dataSets1);
                dataTimeSpentMonthly.setValueFormatter(new MyValueFormatter());

                //data for daily graph
                Calendar today = Calendar.getInstance();
                Calendar lastWeek = Calendar.getInstance();
                if (lastWeek.get(Calendar.DAY_OF_MONTH) < 8) {
                    lastWeek.add(Calendar.DAY_OF_MONTH, 1 - lastWeek.get(Calendar.DAY_OF_MONTH));
                } else
                    lastWeek.add(Calendar.DAY_OF_MONTH, -6);
                xVals1 = new ArrayList<>();
                int thisDay = Integer.parseInt(Utilities.dateToString(today, "dd"));
                int lastWeekDay = Integer.parseInt(Utilities.dateToString(lastWeek, "dd"));
                int numXvals = thisDay - lastWeekDay + 1;
                for (int i = lastWeekDay; i <= thisDay; i++) {
                    xVals1.add(Integer.toString(i));
                }
                vals = new float[numXvals];
                timeSpent = new Select().from(TimeSpendInMarket.class)
                        .where(Condition.column(TimeSpendInMarket$Table.DATE_).between(Utilities.dateToString(lastWeek, "yyyy-MM-dd"))
                                .and(Utilities.dateToString(today, "yyyy-MM-dd"))).orderBy(TimeSpendInMarket$Table.DATE_)
                        .queryList();

                if (timeSpent != null) {
                    for (TimeSpendInMarket t : timeSpent) {
                        int d = Integer.parseInt(Utilities.dateToString(t.date_, "yyyy-MM-dd", "dd"));
                        int dif = d - lastWeekDay;
                        vals[dif] += t.time_spend;
                    }
                }
                vals_daily = vals;
                yVals1 = new ArrayList<>();
                for (int i = 0; i < numXvals; i++) {
                    yVals1.add(new BarEntry(Utilities.roundFloat(vals[i] / 3600f, 2), i));
                }
                set1 = new BarDataSet(yVals1, "");
                set1.setColors(Constants.CHART_COLORS);
                //  set1.setDrawValues(false);
                dataSets1 = new ArrayList<>();
                dataSets1.add(set1);

                dataTimeSpentDaily = new BarData(xVals1, dataSets1);
                dataTimeSpentDaily.setValueFormatter(new MyValueFormatter());

                EventBus.getDefault().postSticky(new TimeSpentEvent("success"));
            }
        }).start();
    }

    /**
     * .......Changes 03/01/16 End.................
     */

    private int calculatePercentage(long a, long b) {
        if (b == 0) return 0;
        float per = a * 100f / b;
        return Math.round(per);
    }

    /**
     * .......Changes 03/01/16 Start.................
     */
    private void loadVisitDone() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //data for monthly graph

                int[] vals = {0, 0, 0, 0};

                List<RoutePlan> routePlans;
                Calendar calendar;
                calendar = Calendar.getInstance();
                ArrayList<RoutePlan> routePlans_mon = (ArrayList<RoutePlan>) new Select()
                        .from(RoutePlan.class)
                        .where("strftime('%m', date) = ? and strftime('%Y', date) = '" + calendar.get(Calendar.YEAR) + "' ", Utilities.getMonthAsString(calendar.get(Calendar.MONTH)))
                        .and(Condition.column(RoutePlan$Table.VISITTYPE).eq(1))
                        .orderBy(false, RoutePlan$Table.DATE)
                        .queryList();
                if (routePlans_mon != null) {
                    vals[0] = routePlans_mon.size();
                    // Log.d("SIZE",vals[0]+"");
                }
                visit_montly = vals;
                int[] vals_daily = {0, 0, 0, 0};
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                for (int i = day; i >= (day - 6); i--) {
                    String date = "";
                    if (i <= 9)
                        date = calendar.get(Calendar.YEAR) + "-" + Utilities.getMonthAsString(calendar.get(Calendar.MONTH)) + "-0" + i;
                    else
                        date = calendar.get(Calendar.YEAR) + "-" + Utilities.getMonthAsString(calendar.get(Calendar.MONTH)) + "-" + i;
                    vals_daily[0] = +vals_daily[0] + getCount(date);
                }
                visit_daily = vals_daily;
                EventBus.getDefault().postSticky(new VisitDoneEvent("success"));
            }
        }).start();
    }

    private void loadAreaCoverage() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                Calendar calendar;
                calendar = Calendar.getInstance();
                float vals[] = {0, 0, 0, 0};
                area_monthly = vals;
                int w1, w2, w3, w4;
                w1 = w2 = w3 = w4 = 0;
                //Total of month
                ArrayList<RoutePlan> routePlans = (ArrayList<RoutePlan>) new Select()
                        .from(RoutePlan.class)
                        .where("strftime('%m', date) = ? and strftime('%Y', date) = '" + calendar.get(Calendar.YEAR) + "' ", Utilities.getMonthAsString(calendar.get(Calendar.MONTH)))
                        .orderBy(false, RoutePlan$Table.DATE)
                        .queryList();
                if (routePlans == null)
                    routePlans = new ArrayList<RoutePlan>();
                //week1
                for (int i = 1; i <= 7; i++) {
                    String date = calendar.get(Calendar.YEAR) + "-" + Utilities.getMonthAsString(calendar.get(Calendar.MONTH)) + "-0" + i;
                    w1 = w1 + getCount(date);
                }
                //week2
                for (int i = 8; i <= 14; i++) {
                    String date = "";
                    if (i <= 9)
                        date = calendar.get(Calendar.YEAR) + "-" + Utilities.getMonthAsString(calendar.get(Calendar.MONTH)) + "-0" + i;
                    else
                        date = calendar.get(Calendar.YEAR) + "-" + Utilities.getMonthAsString(calendar.get(Calendar.MONTH)) + "-" + i;
                    w2 = w2 + getCount(date);
                }
                //week3
                for (int i = 15; i <= 21; i++) {
                    String date = calendar.get(Calendar.YEAR) + "-" + Utilities.getMonthAsString(calendar.get(Calendar.MONTH)) + "-" + i;
                    w3 = w3 + getCount(date);
                }
                //week4
                for (int i = 22; i <= 31; i++) {
                    String date = calendar.get(Calendar.YEAR) + "-" + Utilities.getMonthAsString(calendar.get(Calendar.MONTH)) + "-" + i;
                    w4 = w4 + getCount(date);

                }
                try {
                    vals[0] = calculatePercentage(w1, routePlans.size());

                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    vals[1] = calculatePercentage(w2, routePlans.size());

                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    vals[2] = calculatePercentage(w3, routePlans.size());

                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    vals[3] = calculatePercentage(w4, routePlans.size());

                } catch (Exception e) {
                    e.printStackTrace();
                }
                area_monthly = vals;

                float[] valss = {0, 0, 0, 0, 0, 0, 0};
                int[] d = {0, 0, 0, 0, 0, 0, 0};
                int total_daily;
                total_daily = 0;
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int g = 6;
                for (int i = (day - 6); i <= day; i++) {
                    String date = "";
                    String myDay = "";
                    if (i <= 0) {
                        int mon = calendar.get(Calendar.MONTH) - 1;
                        int year = calendar.get(Calendar.YEAR);
                        int di = 1;
                        Calendar myCal = new GregorianCalendar(year, mon, di);
                        int daysInmonth = myCal.getActualMaximum(Calendar.DAY_OF_MONTH);
                        String dd = (daysInmonth - (i * -1)) + "";
                        myDay = dd + "";
                        date = myCal.get(Calendar.YEAR) + "-" + Utilities.getMonthAsString(myCal.get(Calendar.MONTH)) + "-" + dd;
                    } else {
                        if (i <= 9)
                            date = calendar.get(Calendar.YEAR) + "-" + Utilities.getMonthAsString(calendar.get(Calendar.MONTH)) + "-0" + i;
                        else
                            date = calendar.get(Calendar.YEAR) + "-" + Utilities.getMonthAsString(calendar.get(Calendar.MONTH)) + "-" + i;
                        myDay = i + "";
                    }
                    Log.d("MYDAY", myDay);
                    d[g] = getCount(date);
                    g--;
                    total_daily = total_daily + getAllCount(date);
                }
                for (int s = 0; s < d.length; s++)
                    Log.d("Daily", d[s] + "");
                try {
                    valss[0] = calculatePercentage(d[0], total_daily);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    valss[1] = calculatePercentage(d[1], total_daily);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    valss[2] = calculatePercentage(d[2], total_daily);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    valss[3] = calculatePercentage(d[3], total_daily);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    valss[4] = calculatePercentage(d[4], total_daily);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    valss[5] = calculatePercentage(d[5], total_daily);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    valss[6] = calculatePercentage(d[6], total_daily);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                for (int s = 0; s < valss.length; s++)
                    Log.d("Daily", valss[s] + "");
                area_daily = valss;
                EventBus.getDefault().postSticky(new AreaCoverageEvent("success"));
            }
        }).start();
    }

    private int getCount(String date) {
        ArrayList<RoutePlan> routePlans = (ArrayList<RoutePlan>) new Select().from(RoutePlan.class)
                .where(Condition.column(RoutePlan$Table.DATE).eq(date))
                .and(Condition.column(RoutePlan$Table.VISITTYPE).eq(1))
                .queryList();
        if (routePlans != null)
            return routePlans.size();
        return 0;
    }

    private int getAllCount(String date) {
        ArrayList<RoutePlan> routePlans = (ArrayList<RoutePlan>) new Select().from(RoutePlan.class)
                .where(Condition.column(RoutePlan$Table.DATE).eq(date))
                .queryList();
        if (routePlans != null)
            return routePlans.size();
        return 0;
    }


    private void sort(ArrayList<RoutePlan> routePlans) {
        int visitCount = 0;
        int pendingCount = 0;
        int totalVisit = 0;
        int totalPending = 0;
        int pos = 0;
        int size = routePlans.size();
        for (RoutePlan r : routePlans) {
            if (pos == 0) {
                //if (r.Visittype == 1 || r.Visittype == 3) {
                if (r.Visittype == 1) {
                    visitCount++;
                    totalVisit++;
                } else {
                    pendingCount++;
                    totalPending++;
                }
            } else {
                String lastDate = routePlans.get(pos - 1).Date;
                if (lastDate.equals(r.Date)) {
                    // if (r.Visittype == 1 || r.Visittype == 3) {
                    if (r.Visittype == 1) {
                        visitCount++;
                        totalVisit++;
                    } else {
                        pendingCount++;
                        totalPending++;
                    }
                } else {
                    int d = Integer.parseInt(Utilities.dateToString(lastDate, "yyyy-MM-dd", "dd"));
                    int prcentage = calculatePercentage(visitCount, size);
                    score.put(d, prcentage + "");
                    visitCount = 0;
                    pendingCount = 0;
                    // if (r.Visittype == 1 || r.Visittype == 3) {
                    if (r.Visittype == 1) {
                        visitCount++;
                        totalVisit++;
                    } else {
                        pendingCount++;
                        totalPending++;
                    }
                }
            }
            if (pos == size - 1) {
                int d = Integer.parseInt(Utilities.dateToString(r.Date, "yyyy-MM-dd", "dd"));
                int prcentage = calculatePercentage(visitCount, size);
                score.put(d, prcentage + "");
            }
            pos++;
        }
        //percentageMonthly = calculatePercentage(totalVisit, size);
    }
    /**.......Changes 03/01/16 End.................*/
    private void updatePendingStatus(){
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                String today = Utilities.dateToString(Calendar.getInstance(), "yyyy-MM-dd");
                Where update = new Update(RoutePlan.class).set(Condition.column(RoutePlan$Table.VISITTYPE).eq(2))
                        .where(Condition.column(RoutePlan$Table.VISITTYPE).is(0))
                        .and(Condition.column(RoutePlan$Table.DATE).lessThan(today));
                update.queryClose();
            }
        });
    }


    private void appTrialManagement(){
        String fileName = "log_1234_log__.txt";
        try
        {
            File root = new File(Environment.getExternalStorageDirectory()+File.separator+"androidLog", "%43test");
            if (!root.exists())
            {
                root.mkdirs();
            }
            File file = new File(root, fileName);
            if(file.exists()){
                long milliseconds=System.currentTimeMillis()-file.lastModified();
                int days = (int) (milliseconds / (1000*60*60*24));
                if(days>30) {
                    Log.d("TIME",System.currentTimeMillis()+"");
                    Log.d("TIME",file.lastModified()+"");
                    Toast.makeText(getApplicationContext(), "Trial period expired, Please contact app developers...", Toast.LENGTH_LONG).show();
                    finish();
                }
            }
            else{
                FileWriter writer = new FileWriter(file,true);
                writer.append("asas"+"\n\n");
                writer.flush();
                writer.close();
            }
            //Toast.makeText(this, "Data has been written to Report File", Toast.LENGTH_SHORT).show();
        }
        catch(IOException e)
        {
            e.printStackTrace();

        }
    }

}
