package com.field.datamatics.views.fragments;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.field.datamatics.R;
import com.field.datamatics.database.Appointment;
import com.field.datamatics.database.Appointment$Table;
import com.field.datamatics.database.Client;
import com.field.datamatics.database.Client$Table;
import com.field.datamatics.database.Customer;
import com.field.datamatics.database.Customer$Table;
import com.field.datamatics.database.JoinClientRoutePlan;
import com.field.datamatics.database.RoutePlan$Table;
import com.field.datamatics.ui.AnimatedButton;
import com.field.datamatics.ui.DividerDecoration;
import com.field.datamatics.utils.Utilities;
import com.field.datamatics.views.adapters.TodaysClientAdapter;
import com.raizlabs.android.dbflow.sql.builder.Condition;
import com.raizlabs.android.dbflow.sql.language.ColumnAlias;
import com.raizlabs.android.dbflow.sql.language.Join;
import com.raizlabs.android.dbflow.sql.language.Select;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by anoop on 22/9/15.
 */
public class ClientList extends BaseFragment {
    private RecyclerView rv_list;
    private ArrayList<JoinClientRoutePlan> data;
    private ArrayList<JoinClientRoutePlan> backUpdata=new ArrayList<>();
    private TodaysClientAdapter adapter;
    private EditText edt_search;
    private AnimatedButton search_button;
    private AnimatedButton clear_button;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_client_list,container,false);
        addFragmentTitle("ClientList");
        rv_list= (RecyclerView) view.findViewById(R.id.rv_list);
        edt_search= (EditText) view.findViewById(R.id.edt_search);
        search_button= (AnimatedButton) view.findViewById(R.id.search_button);
        clear_button= (AnimatedButton) view.findViewById(R.id.clear_button);
        new loadData().execute();
        return view;
    }
    private void listSetUp(){
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv_list.setLayoutManager(llm);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            rv_list.addItemDecoration(new DividerDecoration(getActivity()));
        /*for(int i=0;i< MonthlyVisit.route_plan.size();i++){
            JoinClientRoutePlan d=new JoinClientRoutePlan();
            com.field.datamatics.database.RoutePlan rp= MonthlyVisit.route_plan.get(i);
            d.Client_First_Name=rp.client.Client_First_Name;
            d.Client_Last_Name=rp.client.Client_Last_Name;
            d.Speciality=rp.client.Speciality;
            d.Client_Email=rp.client.Client_Email;
            data.add(d);
        }*/
        adapter = new TodaysClientAdapter(getActivity(),data);
        rv_list.setAdapter(adapter);
    }
    private class loadData extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
        }

        @Override
        protected String doInBackground(Void... params) {
            String today = Utilities.dateToString(Calendar.getInstance(), "yyyy-MM-dd");
            ColumnAlias c1 = ColumnAlias.column("Client." + Client$Table.CLIENT_NUMBER).as("Client_Number");
            ColumnAlias c2 = ColumnAlias.column("Client." + Client$Table.ADDRESS_NUMBER_JDE).as("Address_Number_JDE");
            ColumnAlias c3 = ColumnAlias.column("Client." + Client$Table.CLIENT_PREFIX).as("Client_Prefix");
            ColumnAlias c4 = ColumnAlias.column("Client." + Client$Table.CLIENT_FIRST_NAME).as("Client_First_Name");
            ColumnAlias c5 = ColumnAlias.column("Client." + Client$Table.CLIENT_LAST_NAME).as("Client_Last_Name");
            ColumnAlias c6 = ColumnAlias.column("Client." + Client$Table.CLIENT_GENDER).as("Client_Gender");
            ColumnAlias c7 = ColumnAlias.column("Client." + Client$Table.CLIENT_EMAIL).as("Client_Email");
            ColumnAlias c8 = ColumnAlias.column("Client." + Client$Table.CLIENT_PHONE).as("Client_Phone");
            ColumnAlias c9 = ColumnAlias.column("Client." + Client$Table.CLIENT_MOBILE).as("Client_Mobile");
            ColumnAlias c10 = ColumnAlias.column("Client." + Client$Table.CLIENT_FAX).as("Client_Fax");
            ColumnAlias c11 = ColumnAlias.column("Client." + Client$Table.SPECIALITY).as("Speciality");
            ColumnAlias c12 = ColumnAlias.column("Client." + Client$Table.MARKETCLASS).as("Marketclass");
            ColumnAlias c13 = ColumnAlias.column("Client." + Client$Table.STECLASS).as("STEclass");
            ColumnAlias c14 = ColumnAlias.column("Client." + Client$Table.TYPE).as("Type");
            ColumnAlias c15 = ColumnAlias.column("Client." + Client$Table.STATUS).as("Status");
            ColumnAlias c16 = ColumnAlias.column("Client." + Client$Table.VISIT).as("Visit");
            ColumnAlias c17 = ColumnAlias.column("Client." + Client$Table.ACCOUNT_MANAGER).as("Account_Manager");
            ColumnAlias c18 = ColumnAlias.column("Client." + Client$Table.REMARKS).as("Remarks");
            ColumnAlias c19 = ColumnAlias.column("RoutePlan." + RoutePlan$Table.ROUTE_PLAN_NUMBER).as("Route_Plan_Number");
            ColumnAlias c20 = ColumnAlias.column("Appointment." + Appointment$Table.APPOINTMENT_ID).as("Appointment_Id");
            ColumnAlias c21 = ColumnAlias.column("Customer." + Customer$Table.CUSTOMER_NAME).as("CustomerName");
            ColumnAlias c22 = ColumnAlias.column("RoutePlan." + RoutePlan$Table.DATE).as("RoutePlanDate");
            ColumnAlias c23 = ColumnAlias.column("RoutePlan." + RoutePlan$Table.ROUTENO).as("routeNumber");
            ColumnAlias c24 = ColumnAlias.column("Customer." + Customer$Table.LOCATION).as("Location");

            data = (ArrayList<JoinClientRoutePlan>) new Select(c1, c2, c3, c4, c5, c6, c7, c8, c9, c10, c11, c12, c13, c14, c15, c16, c17, c18, c19, c20, c21, c22, c23,c24)
                    .from(Client.class)
                    .join(com.field.datamatics.database.RoutePlan.class, Join.JoinType.INNER)
                    .on(Condition.column(ColumnAlias.columnWithTable("RoutePlan", RoutePlan$Table.CLIENT_CLIENT_NUMBER))
                            .is(ColumnAlias.columnWithTable("Client", Client$Table.CLIENT_NUMBER)))
                    .join(Customer.class, Join.JoinType.INNER)
                    .on(Condition.column(ColumnAlias.columnWithTable("Customer", Customer$Table.CUSTOMER_ID))
                            .is(ColumnAlias.columnWithTable("RoutePlan", RoutePlan$Table.CUSTOMER_CUSTOMER_ID)))
                    .join(Appointment.class, Join.JoinType.LEFT)
                    .on(Condition.column(ColumnAlias.columnWithTable("Appointment", Appointment$Table.ROUTEPLAN_ROUTE_PLAN_NUMBER))
                            .is(ColumnAlias.columnWithTable("RoutePlan", RoutePlan$Table.ROUTE_PLAN_NUMBER)))
                    .where(Condition.column(RoutePlan$Table.DATE).eq(today))
                    .and(Condition.column(RoutePlan$Table.VISITTYPE).eq(0))
                    .and(Condition.column(RoutePlan$Table.AUTHORIZEDBY).isNot(0))
                    .orderBy(true, Customer$Table.LOCATION)
                    .queryCustomList(JoinClientRoutePlan.class);
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(data!=null){
                listSetUp();
                backUpdata.clear();
                backUpdata.addAll(data);
                searchManagement();
            }
            dissmissProgressDialog();
        }
    }
    private void searchManagement(){
        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchParam = edt_search.getText().toString().trim();
                if(searchParam.equals("")){
                    data.clear();
                    data.addAll(backUpdata);
                    adapter.setData((ArrayList<JoinClientRoutePlan>) data);
                    adapter.notifyDataSetChanged();
                }
                else{
                    data.clear();
                    for (int i = 0; i < backUpdata.size(); i++) {
                        if ((backUpdata.get(i).Client_First_Name.toLowerCase().contains(searchParam.toLowerCase())) ||
                                (backUpdata.get(i).CustomerName.toLowerCase().contains(searchParam.toLowerCase()))||
                                (backUpdata.get(i).Location.toLowerCase().contains(searchParam.toLowerCase()))) {
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
                if(edt_search.getText().toString().trim().equals(""))
                    return;
                data.clear();
                data.addAll(backUpdata);
                edt_search.setText("");
                adapter.setData((ArrayList<JoinClientRoutePlan>) data);
                adapter.notifyDataSetChanged();
            }
        });
        edt_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(edt_search.getText().toString().trim().equals("")){
                    data.clear();
                    data.addAll(backUpdata);
                    adapter.setData((ArrayList<JoinClientRoutePlan>) data);
                    adapter.notifyDataSetChanged();
                }

            }
        });

    }
}
