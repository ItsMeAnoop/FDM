package com.field.datamatics.views.fragments;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.TextView;

import com.field.datamatics.R;
import com.field.datamatics.Services.ApiService;
import com.field.datamatics.apimodels.CommonSubmitJson;
import com.field.datamatics.apimodels.RoutePlanSubmit;
import com.field.datamatics.apimodels.RoutePlanSubmitJson;
import com.field.datamatics.constants.ApiConstants;
import com.field.datamatics.database.Client;
import com.field.datamatics.database.Client$Table;
import com.field.datamatics.database.Client_work_cal;
import com.field.datamatics.database.Client_work_cal$Table;
import com.field.datamatics.database.Customer;
import com.field.datamatics.database.Customer$Table;
import com.field.datamatics.database.RoutePlanTemp;
import com.field.datamatics.database.RoutePlanTemp$Table;
import com.field.datamatics.database.User;
import com.field.datamatics.interfaces.ApiCallbacks;
import com.field.datamatics.synctables.SyncRoutePlan;
import com.field.datamatics.utils.Utilities;
import com.field.datamatics.views.adapters.MakeRoutePlanAdapter;
import com.google.gson.Gson;
import com.raizlabs.android.dbflow.sql.builder.Condition;
import com.raizlabs.android.dbflow.sql.language.Select;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by anoop on 12/10/15.
 */
public class MakeRoutePlan extends BaseFragment {
    private Spinner spin_months, spin_years;
    private GridView gridview;
    private TextView tv_pending_list;
    private ArrayList<String> data = new ArrayList<String>();
    private MakeRoutePlanAdapter monthlyVisitAdapter;
    String[] days = {"sunday", "monday", "tuesday", "wednesday", "thursday", "friday", "saturday"};
    //String[] days = {"SUN", "MON", "TUE", "WED", "THU", "FRI","SAT"};
    public static ArrayList<ArrayList<com.field.datamatics.models.MakeRoutePlan>> route_plan = new ArrayList<ArrayList<com.field.datamatics.models.MakeRoutePlan>>();
    public static ArrayList<ArrayList<String>> data_status = new ArrayList<ArrayList<String>>();
    public static int f = 0;
    int start = 1;
    int days_count = 30;
    ArrayList<String> months = new ArrayList<String>();
    ArrayAdapter month_adapter;
    ArrayList<String> years = new ArrayList<String>();
    ArrayAdapter years_adapter;
    String month_ = "01";
    String year_ = "2015";
    public static int month_position = 0;
    public static int year_position = 0;
    public int change = 0;
    private ArrayList<RoutePlanSubmit> arrroutePlanSubmits = new ArrayList<RoutePlanSubmit>();
    private int spinner_listener = 0;
    ProgressDialog dialog;
    ArrayList<com.field.datamatics.models.MakeRoutePlan> plan_sunday = new ArrayList<com.field.datamatics.models.MakeRoutePlan>();
    ArrayList<String> temp_s = new ArrayList<String>();
    ArrayList<com.field.datamatics.models.MakeRoutePlan> plan_monday = new ArrayList<com.field.datamatics.models.MakeRoutePlan>();
    ArrayList<String> temp_m = new ArrayList<String>();
    ArrayList<com.field.datamatics.models.MakeRoutePlan> plan_tuesday = new ArrayList<com.field.datamatics.models.MakeRoutePlan>();
    ArrayList<String> temp_t = new ArrayList<String>();
    ArrayList<com.field.datamatics.models.MakeRoutePlan> plan_wed = new ArrayList<com.field.datamatics.models.MakeRoutePlan>();
    ArrayList<String> temp_w = new ArrayList<String>();
    ArrayList<com.field.datamatics.models.MakeRoutePlan> plan_thurs = new ArrayList<com.field.datamatics.models.MakeRoutePlan>();
    ArrayList<String> temp_th = new ArrayList<String>();
    ArrayList<com.field.datamatics.models.MakeRoutePlan> plan_friday = new ArrayList<com.field.datamatics.models.MakeRoutePlan>();
    ArrayList<String> temp_fr = new ArrayList<String>();
    ArrayList<com.field.datamatics.models.MakeRoutePlan> plan_sat = new ArrayList<com.field.datamatics.models.MakeRoutePlan>();
    ArrayList<String> temp_sa = new ArrayList<String>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_monthly_visit, container, false);
        addFragmentTitle("MakeRoutePlan");
        dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Please wait...");
        gridview = (GridView) view.findViewById(R.id.grid_monthly_visit);
        tv_pending_list = (TextView) view.findViewById(R.id.tv_pending_list);
        spin_months = (Spinner) view.findViewById(R.id.spin_months);
        spin_years = (Spinner) view.findViewById(R.id.spin_years);
        tv_pending_list.setText("Make Route Plan");
        months.add("January");
        months.add("February");
        months.add("March");
        months.add("April");
        months.add("May");
        months.add("June");
        months.add("July");
        months.add("August");
        months.add("September");
        months.add("October");
        months.add("November");
        months.add("December");
        final Calendar c = Calendar.getInstance();
        month_ = (c.get(Calendar.MONTH) + 1) + "";
        year_ = c.get(Calendar.YEAR) + "";
        for (int i = c.get(Calendar.YEAR); i < (c.get(Calendar.YEAR) + 2); i++) {
            years.add(i + "");
        }
        month_adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, months);
        years_adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, years);
        spin_months.setAdapter(month_adapter);
        spin_years.setAdapter(years_adapter);
        month_position = c.get(Calendar.MONTH);
        spin_years.setSelection(year_position);
        spin_months.setSelection(month_position);

        setGridView();
        //registerEvents();
        if (f == 0) {
            //readClientList();
            f = 1;
        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        monthlyVisitAdapter.notifyDataSetChanged();
    }

    private void clearStatus() {
        for (int j = 0; j < data_status.size(); j++) {
            for (int k = 0; k < data_status.get(j).size(); k++) {
                if (data_status.get(j).get(k).equals("1")) {
                    data_status.get(j).remove(k);
                    data_status.get(j).add(k, "0");
                }
            }
        }
    }

    private void setGridView() {

        try {
            String input_date = "01/" + month_ + "/" + year_;
            //String input_date="01/11/2015";
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            Date date1 = format.parse(input_date);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date1);
            //Toast.makeText(getActivity(),cal.get(Calendar.DAY_OF_WEEK)+"",Toast.LENGTH_SHORT).show();
            start = cal.get(Calendar.DAY_OF_WEEK);
        } catch (Exception e) {

        }


        DisplayMetrics metrics = getActivity().getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int gridwidth = (int) width / 7;
        data.clear();
        switch (start) {
            case 1:
                break;
            case 2:
                data.add("");
                break;
            case 3:
                data.add("");
                data.add("");
                break;
            case 4:
                data.add("");
                data.add("");
                data.add("");
                break;
            case 5:
                data.add("");
                data.add("");
                data.add("");
                data.add("");
                break;
            case 6:
                data.add("");
                data.add("");
                data.add("");
                data.add("");
                data.add("");
                break;
            case 7:
                data.add("");
                data.add("");
                data.add("");
                data.add("");
                data.add("");
                data.add("");
                break;
            default:
                break;
        }
        if ((Integer.parseInt(month_)) % 2 == 0)
            days_count = 30;
        else
            days_count = 31;
        if (month_.equals("2")) {
            if ((Integer.parseInt(year_)) % 4 == 0)
                days_count = 29;
            else
                days_count = 28;
        }
        for (int i = 1; i <= days_count; i++)
            data.add(i + "");
        monthlyVisitAdapter = new MakeRoutePlanAdapter(getActivity(), data, gridwidth);
        gridview.setAdapter(monthlyVisitAdapter);
        monthlyVisitAdapter.notifyDataSetChanged();


    }

    /*private void registerEvents() {
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int pos = (position - (start - 1));
                if (data.get(position).equals(""))
                    return;
                if (route_plan.get(pos).size() == 0) {
                    showMessage("No clients available for the day", gridview);
                    return;
                }
                AddClient.position_ = pos;
                addFragment(new AddClient());

            }
        });

        tv_pending_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (checkPlan() == 0) {
                    saveRoutePlan();
                } else {
                    showMessage("Route plan was already created for the month", gridview);
                }
                //checkPlan();


            }
        });
        spin_months.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                month_position = position;
                month_ = (position + 1) + "";
                change++;
                if (checkPlan() == 0) {
                    setGridView();
                }
                if (change > 2) {
                    clearStatus();
                    readClientList();
                    //Toast.makeText(getActivity(),"Change",Toast.LENGTH_SHORT).show();
                }
                //Toast.makeText(getActivity(),"C->"+(++change)+"",Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spin_years.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                year_position = position;
                year_ = years.get(position);
                change++;
                if (checkPlan() == 0) {
                    setGridView();
                }
                //Toast.makeText(getActivity(),"C->"+(++change)+"",Toast.LENGTH_SHORT).show();
                if (change > 2) {
                    clearStatus();
                    readClientList();
                    //Toast.makeText(getActivity(),"Change",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }*/

    /*private void readClientList() {

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog.show();
            }

            @Override
            protected Void doInBackground(Void... params) {
                plan_sunday.clear();
                plan_monday.clear();
                plan_tuesday.clear();
                plan_wed.clear();
                plan_thurs.clear();
                plan_friday.clear();
                plan_sat.clear();
                temp_s.clear();
                temp_m.clear();
                temp_t.clear();
                temp_w.clear();
                temp_th.clear();
                temp_fr.clear();
                temp_sa.clear();
                ArrayList<Client_work_cal> sunday = (ArrayList<Client_work_cal>) new Select().from(Client_work_cal.class).where(Condition.column(Client_work_cal$Table.AVAILABLEDAYS).eq(days[0])).queryList();
                ArrayList<Client_work_cal> monday = (ArrayList<Client_work_cal>) new Select().from(Client_work_cal.class).where(Condition.column(Client_work_cal$Table.AVAILABLEDAYS).eq(days[1])).queryList();
                ArrayList<Client_work_cal> tuesday = (ArrayList<Client_work_cal>) new Select().from(Client_work_cal.class).where(Condition.column(Client_work_cal$Table.AVAILABLEDAYS).eq(days[2])).queryList();
                ArrayList<Client_work_cal> wednesday = (ArrayList<Client_work_cal>) new Select().from(Client_work_cal.class).where(Condition.column(Client_work_cal$Table.AVAILABLEDAYS).eq(days[3])).queryList();
                ArrayList<Client_work_cal> thursday = (ArrayList<Client_work_cal>) new Select().from(Client_work_cal.class).where(Condition.column(Client_work_cal$Table.AVAILABLEDAYS).eq(days[4])).queryList();
                ArrayList<Client_work_cal> friday = (ArrayList<Client_work_cal>) new Select().from(Client_work_cal.class).where(Condition.column(Client_work_cal$Table.AVAILABLEDAYS).eq(days[5])).queryList();
                ArrayList<Client_work_cal> saturday = (ArrayList<Client_work_cal>) new Select().from(Client_work_cal.class).where(Condition.column(Client_work_cal$Table.AVAILABLEDAYS).eq(days[6])).queryList();


                try {
                    for (int j = 0; j < sunday.size(); j++) {
                        Client client = new Select().from(Client.class).where(Condition.column(Client$Table.CLIENT_NUMBER).eq(sunday.get(j).client.Client_Number)).querySingle();
                        Customer customer = new Select().from(Customer.class).where(Condition.column(Customer$Table.CUSTOMER_ID).eq(sunday.get(j).customer.Customer_Id)).querySingle();
                        plan_sunday.add(new com.field.datamatics.models.MakeRoutePlan(false, sunday.get(j), client, customer));
                        temp_s.add("0");
                        Log.d("Customer ID", sunday.get(j).customer.Customer_Id + "");

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


                try {
                    for (int j = 0; j < monday.size(); j++) {
                        Client client = new Select().from(Client.class).where(Condition.column(Client$Table.CLIENT_NUMBER).eq(monday.get(j).client.Client_Number)).querySingle();
                        Customer customer = new Select().from(Customer.class).where(Condition.column(Customer$Table.CUSTOMER_ID).eq(monday.get(j).customer.Customer_Id)).querySingle();
                        plan_monday.add(new com.field.datamatics.models.MakeRoutePlan(false, monday.get(j), client, customer));
                        temp_m.add("0");
                        Log.d("Customer ID", monday.get(j).customer.Customer_Id + "");

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


                try {
                    for (int j = 0; j < tuesday.size(); j++) {
                        Client client = new Select().from(Client.class).where(Condition.column(Client$Table.CLIENT_NUMBER).eq(tuesday.get(j).client.Client_Number)).querySingle();
                        Customer customer = new Select().from(Customer.class).where(Condition.column(Customer$Table.CUSTOMER_ID).eq(tuesday.get(j).customer.Customer_Id)).querySingle();
                        plan_tuesday.add(new com.field.datamatics.models.MakeRoutePlan(false, tuesday.get(j), client, customer));
                        temp_t.add("0");
                        Log.d("Customer ID", tuesday.get(j).customer.Customer_Id + "");

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


                try {
                    for (int j = 0; j < wednesday.size(); j++) {
                        Client client = new Select().from(Client.class).where(Condition.column(Client$Table.CLIENT_NUMBER).eq(wednesday.get(j).client.Client_Number)).querySingle();
                        Customer customer = new Select().from(Customer.class).where(Condition.column(Customer$Table.CUSTOMER_ID).eq(wednesday.get(j).customer.Customer_Id)).querySingle();
                        plan_wed.add(new com.field.datamatics.models.MakeRoutePlan(false, wednesday.get(j), client, customer));
                        temp_w.add("0");
                        Log.d("Customer ID", wednesday.get(j).customer.Customer_Id + "");

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


                try {
                    for (int j = 0; j < thursday.size(); j++) {
                        Client client = new Select().from(Client.class).where(Condition.column(Client$Table.CLIENT_NUMBER).eq(thursday.get(j).client.Client_Number)).querySingle();
                        Customer customer = new Select().from(Customer.class).where(Condition.column(Customer$Table.CUSTOMER_ID).eq(thursday.get(j).customer.Customer_Id)).querySingle();
                        plan_thurs.add(new com.field.datamatics.models.MakeRoutePlan(false, thursday.get(j), client, customer));
                        temp_th.add("0");
                        Log.d("Customer ID", thursday.get(j).customer.Customer_Id + "");

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


                try {
                    for (int j = 0; j < friday.size(); j++) {
                        Client client = new Select().from(Client.class).where(Condition.column(Client$Table.CLIENT_NUMBER).eq(friday.get(j).client.Client_Number)).querySingle();
                        Customer customer = new Select().from(Customer.class).where(Condition.column(Customer$Table.CUSTOMER_ID).eq(friday.get(j).customer.Customer_Id)).querySingle();
                        plan_friday.add(new com.field.datamatics.models.MakeRoutePlan(false, friday.get(j), client, customer));
                        temp_fr.add("0");
                        Log.d("Customer ID", friday.get(j).customer.Customer_Id + "");


                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


                try {
                    for (int j = 0; j < saturday.size(); j++) {
                        Client client = new Select().from(Client.class).where(Condition.column(Client$Table.CLIENT_NUMBER).eq(saturday.get(j).client.Client_Number)).querySingle();
                        Customer customer = new Select().from(Customer.class).where(Condition.column(Customer$Table.CUSTOMER_ID).eq(saturday.get(j).customer.Customer_Id)).querySingle();
                        plan_sat.add(new com.field.datamatics.models.MakeRoutePlan(false, saturday.get(j), client, customer));
                        temp_sa.add("0");
                        Log.d("Customer ID", saturday.get(j).customer.Customer_Id + "");

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                fillRoutePlanDataByDate();

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                dialog.dismiss();
                //Toast.makeText(getActivity(),getDayName(start,1)+"-Day", Toast.LENGTH_SHORT).show();
            }
        }.execute();

    }*/

    private void fillRoutePlanDataByDate() {
        route_plan.clear();
        data_status.clear();
        for (int i = 1; i <= days_count; i++) {
            switch (getDayName(start, i)) {
                case 1:
                    //sunday
                    route_plan.add(plan_sunday);
                    data_status.add(temp_s);
                    break;
                case 2:
                    //monday
                    route_plan.add(plan_monday);
                    data_status.add(temp_m);
                    break;
                case 3:
                    //tuesday
                    route_plan.add(plan_tuesday);
                    data_status.add(temp_t);
                    break;
                case 4:
                    //wednesday
                    route_plan.add(plan_wed);
                    data_status.add(temp_w);
                    break;
                case 5:
                    //thursday
                    route_plan.add(plan_thurs);
                    data_status.add(temp_th);
                    break;
                case 6:
                    //friday
                    route_plan.add(plan_friday);
                    data_status.add(temp_fr);
                    break;
                case 0:
                    //saturday
                    route_plan.add(plan_sat);
                    data_status.add(temp_sa);
                    break;
            }


        }
    }

    private int getDayName(int start, int day) {
        day = (start - 1) + day;
        day = day % 7;
        return day;
    }

    private int checkPlan() {
        ArrayList<RoutePlanTemp> plan = (ArrayList<RoutePlanTemp>) new Select().from(RoutePlanTemp.class).where(Condition.column(RoutePlanTemp$Table.DATE).like(year_ + "-" + month_ + "-%")).queryList();
        if (plan == null)
            return 0;
        /*if(plan.size()==0)
            Toast.makeText(getActivity(),"Possible to create new route plan",Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(getActivity(),"Route plan Already created",Toast.LENGTH_SHORT).show();*/

        return plan.size();


    }

    private void saveRoutePlan() {


        new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                showDialog(true);
            }

            @Override
            protected Void doInBackground(Void... params) {
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                String date = new StringBuilder()
                        .append(year).append("-").append(month + 1).append("-")
                        .append(day).append(" ").toString();
                User user = new Select().from(User.class).querySingle();
                for (int i = 0; i < data_status.size(); i++) {
                    for (int k = 0; k < data_status.get(i).size(); k++) {
                        if (data_status.get(i).get(k).equals("1")) {
                            Calendar calendar = Calendar.getInstance();
                            RoutePlanTemp routePlan = new RoutePlanTemp();
                            //routePlan.Route_Plan_Number = ++routePlanNumber;
                            //routePlan.Date = "2015-30-11";
                            routePlan.Date = year_ + "-" + (month_) + "-" + (i + 1);
                            routePlan.client = route_plan.get(i).get(k).getClient();
                            routePlan.customer = route_plan.get(i).get(k).getCustomer();
                            routePlan.user = user;
                            routePlan.client_work_cal = route_plan.get(i).get(k).getClient_work_cal();
                            routePlan.Creation_Date = Utilities.dateInSqliteFormat(calendar);
                            routePlan.preparedBy = user;
                            routePlan.Prepareduser = user.First_Name;
                            routePlan.Visittype = 0;
                            //routePlan.AuthorizedBy = 7;
                            routePlan.save();


                            //Save and make an api call
                            RoutePlanSubmit routePlanSubmit = new RoutePlanSubmit(year_ + "-" + month_ + "-" + (i + 1),
                                    route_plan.get(i).get(k).getClient().Client_Number + "",
                                    route_plan.get(i).get(k).getCustomer().Customer_Id + "",
                                    user.UserNumber + "",
                                    route_plan.get(i).get(k).getClient_work_cal().Clientwork_Id + "",
                                    user.UserNumber + "",
                                    user.First_Name,
                                    "0", date, "", "1");
                            arrroutePlanSubmits.add(routePlanSubmit);

                        }
                    }
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                showDialog(false);
                showMessage("Route plan Created", gridview);
                year_position = 0;
                month_position = 0;
                change = 0;
                RoutePlanSubmitJson routePlanSubmitJson = new RoutePlanSubmitJson(ApiConstants.STATUS, arrroutePlanSubmits);
                submitRoutePlan(routePlanSubmitJson);


            }
        }.execute();

    }

    private void submitRoutePlan(final RoutePlanSubmitJson routePlanSubmitJson) {
        final Gson gson = new Gson();
        final CommonSubmitJson commonSubmitJson = new CommonSubmitJson(routePlanSubmitJson);
        showProgressDialog();
        ApiService.getInstance().makeApiCall(ApiConstants.AppRoutePlanDetails, commonSubmitJson, new ApiCallbacks() {
            @Override
            public void onSuccess(Object objects) {
                dissmissProgressDialog();
                addFragment(DashBoardNewDesign.getInstance());
            }

            @Override
            public void onError(Object objects) {
                //keep to local DB
                SyncRoutePlan syncRoutePlan = new SyncRoutePlan();
                syncRoutePlan.route_plan_details = gson.toJson(data);
                syncRoutePlan.save();
                dissmissProgressDialog();
                addFragment(DashBoardNewDesign.getInstance());

            }

            @Override
            public void onErrorMessage(String message) {

            }
        });

    }


}

