package com.field.datamatics.views.fragments;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.field.datamatics.R;
import com.field.datamatics.comparators.LocationComparator;
import com.field.datamatics.constants.Constants;
import com.field.datamatics.database.Appointment$Table;
import com.field.datamatics.database.Client;
import com.field.datamatics.database.Client$Table;
import com.field.datamatics.database.Client_work_cal;
import com.field.datamatics.database.Client_work_cal$Table;
import com.field.datamatics.database.Customer;
import com.field.datamatics.database.Customer$Table;
import com.field.datamatics.database.JoinClientRoutePlan;
import com.field.datamatics.database.RoutePlan$Table;
import com.field.datamatics.ui.AnimatedButton;
import com.field.datamatics.ui.DividerDecoration;
import com.field.datamatics.ui.RecyclerItemClickListener;
import com.field.datamatics.utils.AppControllerUtil;
import com.field.datamatics.utils.PreferenceUtil;
import com.field.datamatics.utils.Utilities;
import com.field.datamatics.views.MainActivity;
import com.field.datamatics.views.adapters.AdditionalAdapter;
import com.field.datamatics.views.adapters.PendingListAdapter;
import com.field.datamatics.views.adapters.TodaysClientAdapter;
import com.google.gson.Gson;
import com.raizlabs.android.dbflow.sql.builder.Condition;
import com.raizlabs.android.dbflow.sql.language.ColumnAlias;
import com.raizlabs.android.dbflow.sql.language.Select;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Anoop on 29-05-2016.
 */
public class AdditionalVisit extends BaseFragment {
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private List<JoinClientRoutePlan> data;
    private ArrayList<JoinClientRoutePlan> backUpdata;
    private AdditionalAdapter adapter;
    private String fragmentName;
    private TextView emptyView;
    private EditText edt_search;
    private AnimatedButton search_button;
    private AnimatedButton clear_button;
    private int index=0;
    private boolean isEnd=false;
    public AdditionalVisit() {
    }
    public static AdditionalVisit getInstance(String fragmentName) {
        AdditionalVisit f = new AdditionalVisit();
        f.fragmentName = fragmentName;
        return f;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        SharedPreferences.Editor edit = AppControllerUtil.getPrefsResume()
                .edit();
        edit.putString(Constants.PREF_DATE, Utilities.dateToString(Calendar.getInstance(), "yyyy-MM-dd"));
        edit.putBoolean(Constants.PREF_SHOULD_RESUME, true);
        edit.putInt(Constants.PREF_CURRENT_ACTIVITY, Constants.STAT_ADDITIONAL_VISIT);
        edit.commit();
        View view = inflater.inflate(R.layout.fragment_client_list, container, false);
        //setTitle("Additional visits");
        addFragmentTitle(fragmentName);
        initializeViews(view);
        loadData();
        return view;
    }

    private void initializeViews(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.rv_list);
        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        emptyView = (TextView) view.findViewById(R.id.empty);
        edt_search= (EditText) view.findViewById(R.id.edt_search);
        search_button= (AnimatedButton) view.findViewById(R.id.search_button);
        clear_button= (AnimatedButton) view.findViewById(R.id.clear_button);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        data = new ArrayList<>();
        backUpdata=new ArrayList<>();
        recyclerView.setHasFixedSize(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            recyclerView.addItemDecoration(new DividerDecoration(getActivity()));

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                JoinClientRoutePlan client = adapter.getItem(position);
                Bundle data = new Bundle();
                ArrayList<String> values = new ArrayList<String>();
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
                values.add(String.valueOf(client.Route_Plan_Number));
                values.add(client.RoutePlanDate);
                values.add(client.Client_Prefix);
                values.add(client.Location);
                data.putStringArrayList("values", values);
                data.putInt("route_plan_number", client.Route_Plan_Number);
                data.putInt("apointment_id", client.Appointment_Id);
                data.putBoolean("is_pending", true);
                data.putBoolean("is_additional", true);
                data.putString("fragmentName","Additional Visit Details");
                if(client.Client_Prefix!=null)
                    data.putString("time_availability",client.Client_Prefix);
                if(client.Location!=null)
                    data.putString("location",client.Location);
                data.putString("use","ADDITIONAL");
                PreferenceUtil.getIntsance().setCLIENT_ID(client.Client_Number+"");
                PreferenceUtil.getIntsance().setCUSTOMER_ID(client.RoutePlanDate+"");

                data.putInt("client_id", client.Client_Number);
                SharedPreferences.Editor edit = AppControllerUtil.getPrefsResume().edit();
                String jsonValue = new Gson().toJson(client);
                edit.putString(Constants.VAL_VISIT_HOME_PAGE, jsonValue);
                edit.commit();
                addFragment(VisitHomePage.getInstance(data));
            }
        }));

        adapter = new AdditionalAdapter(getActivity(), (ArrayList<JoinClientRoutePlan>) data);
        recyclerView.setAdapter(adapter);

    }
    private void loadData(){
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressBar.setVisibility(View.VISIBLE);
            }
            @Override
            protected Void doInBackground(Void... params) {
                data.clear();
                Calendar calendar=Calendar.getInstance();
                Date date=calendar.getTime();
                String today=new SimpleDateFormat("EEEE", Locale.ENGLISH).format(date.getTime());
                today=today.toLowerCase();
                ArrayList<Client_work_cal>client_work_cals= (ArrayList<Client_work_cal>) new Select().from(Client_work_cal.class)
                        //.where(Condition.column(Client_work_cal$Table.AVAILABLEDAYS).eq(today))
                        //.and(Condition.column(Client_work_cal$Table.CLIENTWORK_ID).greaterThan(index))
                        //.limit(100)
                        .queryList();
                if(client_work_cals!=null){
                    final int sizeee=client_work_cals.size();
                    for(int i=0;i<client_work_cals.size();i++){
                        index=client_work_cals.get(i).Clientwork_Id;
                        Client client=  new Select().from(Client.class)
                                .where(Condition.column(Client$Table.CLIENT_NUMBER).eq(client_work_cals.get(i).Clientno))
                                .querySingle();
                        Customer customer=  new Select().from(Customer.class)
                                .where(Condition.column(Customer$Table.CUSTOMER_ID).eq(client_work_cals.get(i).Customerid))
                                .querySingle();
                        JoinClientRoutePlan joinClientRoutePlan=new JoinClientRoutePlan();
                        joinClientRoutePlan.Client_Number=Integer.parseInt(client_work_cals.get(i).Clientno);
                        joinClientRoutePlan.Client_First_Name=client_work_cals.get(i).Clientfirstname;
                        joinClientRoutePlan.CustomerName=client_work_cals.get(i).Customername;
                        joinClientRoutePlan.Client_Prefix= client_work_cals.get(i).Fromtime+" - "+client_work_cals.get(i).Totime;
                        if(client!=null){
                            joinClientRoutePlan.Address_Number_JDE= client.Address_Number_JDE;
                            //joinClientRoutePlan.Client_Prefix=client.Client_Prefix;
                            joinClientRoutePlan.Client_Last_Name=client.Client_Last_Name;
                            joinClientRoutePlan.Client_Gender=client.Client_Gender;
                            joinClientRoutePlan.Client_Email=client.Client_Email;
                            joinClientRoutePlan.Client_Phone=client.Client_Phone;
                            joinClientRoutePlan.Client_Mobile=client.Client_Mobile;
                            joinClientRoutePlan.Client_Fax=client.Client_Fax;
                            joinClientRoutePlan.Speciality=client.Speciality;
                            joinClientRoutePlan.STEclass=client.STEclass;
                            joinClientRoutePlan.Type=client.Type;
                            joinClientRoutePlan.Status=client.Status;
                            joinClientRoutePlan.Visit=client.Visit;
                            joinClientRoutePlan.Account_Manager=client.Account_Manager;
                            joinClientRoutePlan.Remarks=client.Remarks;
                        }
                        String location="";
                        if(customer!=null&&customer.Location!=null){
                            location=customer.Location;
                        }
                        joinClientRoutePlan.Location=location;
                        joinClientRoutePlan.Route_Plan_Number=-1;
                        joinClientRoutePlan.Appointment_Id=-1;
                        joinClientRoutePlan.RoutePlanDate=client_work_cals.get(i).Customerid;
                        data.add(joinClientRoutePlan);
                    }
                    boolean asc=true;
                    Comparator<JoinClientRoutePlan> comp = asc ? new LocationComparator() : Collections.reverseOrder(new LocationComparator());
                    Collections.sort(data, comp);
                }
                return null;
            }
            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                if (data == null || data.size() == 0) {
                    isEnd=true;
                    emptyView.setVisibility(View.VISIBLE);
                    data.addAll(backUpdata);
                } else {
                    emptyView.setVisibility(View.GONE);
                    backUpdata.addAll(data);
                    data.clear();
                    data.addAll(backUpdata);
                    searchManagement();
                }
                adapter.setData((ArrayList<JoinClientRoutePlan>) data);
                progressBar.setVisibility(View.GONE);
            }
        }.execute();
    }
    private void searchManagement(){
        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchParam = edt_search.getText().toString().trim().toLowerCase();
                if(searchParam.equals("")){
                    data.clear();
                    data.addAll(backUpdata);
                    adapter.setData((ArrayList<JoinClientRoutePlan>) data);
                    adapter.notifyDataSetChanged();
                }
                else{
                    data.clear();
                    for (int i = 0; i < backUpdata.size(); i++) {
                        if ((backUpdata.get(i).Client_First_Name.toLowerCase().contains(searchParam)) ||
                                (backUpdata.get(i).CustomerName.toLowerCase().contains(searchParam))) {
                            data.add(backUpdata.get(i));
                        }
                    }
                    adapter.setData((ArrayList<JoinClientRoutePlan>) data);
                }

            }
        });
        clear_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edt_search.setText("");
                data.clear();
                data.addAll(backUpdata);
                adapter.setData((ArrayList<JoinClientRoutePlan>) data);
                adapter.notifyDataSetChanged();
            }
        });

    }

}
