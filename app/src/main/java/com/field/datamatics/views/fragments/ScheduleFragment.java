package com.field.datamatics.views.fragments;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.field.datamatics.R;
import com.field.datamatics.database.Appointment;
import com.field.datamatics.database.Appointment$Table;
import com.field.datamatics.database.Client;
import com.field.datamatics.database.Client$Table;
import com.field.datamatics.database.Customer;
import com.field.datamatics.database.Customer$Table;
import com.field.datamatics.database.JoinClientRoutePlan;
import com.field.datamatics.database.RoutePlan$Table;
import com.field.datamatics.ui.DividerDecoration;
import com.field.datamatics.ui.RecyclerItemClickListener;
import com.field.datamatics.utils.Utilities;
import com.field.datamatics.views.adapters.ScheduleActualAdapter;
import com.field.datamatics.views.adapters.TodaysClientAdapter;
import com.raizlabs.android.dbflow.sql.builder.Condition;
import com.raizlabs.android.dbflow.sql.language.ColumnAlias;
import com.raizlabs.android.dbflow.sql.language.Join;
import com.raizlabs.android.dbflow.sql.language.Select;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Jith on 11/8/2015.
 */
public class ScheduleFragment extends BaseFragment {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private List<JoinClientRoutePlan> data;
    private ScheduleActualAdapter adapter;
    private TextView emptyView;

    private String dateFrom;
    private String dateTo;
    private int filterType;

    public ScheduleFragment() {
    }

    public static ScheduleFragment getInstance(String dateFrom, String dateTo, int filterType) {
        ScheduleFragment f = new ScheduleFragment();
        f.dateFrom = dateFrom;
        f.dateTo = dateTo;
        f.filterType = filterType;
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_client_list, container, false);
        initializeViews(view);
        new loadData().execute();
        return view;
    }

    private void initializeViews(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.rv_list);
        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        data = new ArrayList<>();
        recyclerView.setHasFixedSize(true);
        emptyView = (TextView) view.findViewById(R.id.empty);

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
                data.putStringArrayList("values", values);
                data.putInt("route_plan_number", client.Route_Plan_Number);
                data.putInt("apointment_id", client.Appointment_Id);
                data.putBoolean("disable_activities", true);
                addFragment(VisitHomePage.getInstance(data));
            }
        }));

        adapter = new ScheduleActualAdapter(filterType);
        recyclerView.setAdapter(adapter);

    }


    private class loadData extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(Void... params) {
            /*ColumnAlias c1 = ColumnAlias.column("Client." + Client$Table.CLIENT_NUMBER).as("Client_Number");
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
            ColumnAlias c22 = ColumnAlias.column("Customer." + Customer$Table.REGION).as("Region");
            ColumnAlias c23 = ColumnAlias.column("RoutePlan." + RoutePlan$Table.DATE).as("RoutePlanDate");

            data = new Select(c1, c2, c3, c4, c5, c6, c7, c8, c9, c10, c11, c12, c13, c14, c15, c16, c17, c18, c19, c20, c21, c22, c23)
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
                    .where(Condition.column(RoutePlan$Table.DATE).between(dateFrom).and(dateTo))
                    .and(Condition.column(RoutePlan$Table.VISITTYPE).eq(0)).or(Condition.column(RoutePlan$Table.VISITTYPE).eq(2))
                    .and(Condition.column(RoutePlan$Table.AUTHORIZEDBY).isNot(0))
                    .orderBy(true, filterType == 0 ? "Customer." + Customer$Table.CUSTOMER_ID : "Customer." + Customer$Table.REGION)
                    .queryCustomList(JoinClientRoutePlan.class);*/


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

            data = new Select(c1, c2, c3, c4, c5, c6, c7, c8, c9, c10, c11, c12, c13, c14, c15, c16, c17, c18, c19, c20, c21, c22, c23,c24)
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
                    .where(Condition.column(RoutePlan$Table.DATE).between(dateFrom).and(dateTo))
                    .and(Condition.column(RoutePlan$Table.VISITTYPE).eq(0))
                    .and(Condition.column(RoutePlan$Table.AUTHORIZEDBY).isNot(0))
                    .orderBy(true, filterType == 0 ? "Customer." + Customer$Table.CUSTOMER_ID : "Customer." + Customer$Table.REGION)
                    .queryCustomList(JoinClientRoutePlan.class);
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressBar.setVisibility(View.GONE);
            if (data == null || data.size() == 0) {
                emptyView.setVisibility(View.VISIBLE);
            } else {
                emptyView.setVisibility(View.GONE);
                adapter.setData((ArrayList<JoinClientRoutePlan>) data);
            }
        }
    }

}
