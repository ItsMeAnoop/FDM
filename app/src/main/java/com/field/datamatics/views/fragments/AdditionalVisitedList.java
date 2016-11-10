package com.field.datamatics.views.fragments;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.field.datamatics.R;
import com.field.datamatics.database.AdditionalVisits;
import com.field.datamatics.database.AdditionalVisits$Table;
import com.field.datamatics.database.JoinClientRoutePlan;
import com.field.datamatics.database.RoutePlan$Table;
import com.field.datamatics.ui.DividerDecoration;
import com.field.datamatics.ui.RecyclerItemClickListener;
import com.field.datamatics.utils.AppControllerUtil;
import com.field.datamatics.views.adapters.AdditionalAdapter;
import com.field.datamatics.views.adapters.ScheduleActualAdapter;
import com.field.datamatics.views.dialogs.ClientDetailsDialog;
import com.raizlabs.android.dbflow.sql.builder.Condition;
import com.raizlabs.android.dbflow.sql.language.Select;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anoop on 07-08-2016.
 */
public class AdditionalVisitedList extends BaseFragment {
    private RecyclerView recyclerView;
    private ArrayList<JoinClientRoutePlan> data=new ArrayList<>();
    private ArrayList<AdditionalVisits>visitList=null;
    private AdditionalAdapter adapter;
    private RelativeLayout search_layout;

    public static String dateFrom;
    public static String dateTo;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_client_list, container, false);
        initializeViews(view);
        loadData();
        return view;
    }
    private void initializeViews(View view) {
        search_layout= (RelativeLayout) view.findViewById(R.id.search_layout);
        search_layout.setVisibility(View.GONE);
        recyclerView = (RecyclerView) view.findViewById(R.id.rv_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        data = new ArrayList<>();
        recyclerView.setHasFixedSize(true);
        setTitle("Additional Visited List");
        AppControllerUtil.getInstance().setCurrent_fragment("AdditionalVisitedList");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            recyclerView.addItemDecoration(new DividerDecoration(getActivity()));

        adapter = new AdditionalAdapter(getActivity());
        recyclerView.setAdapter(adapter);
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                ClientDetailsDialog.getInstance().ClientDetailsDialog(getActivity(),visitList.get(position));
                /*JoinClientRoutePlan client = adapter.getItem(position);
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
                addFragment(VisitHomePage.getInstance(data));*/
            }
        }));

    }

    private void loadData(){
        showProgressDialog();
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                visitList= (ArrayList<AdditionalVisits>) new Select().from(AdditionalVisits.class)
                        .where(Condition.column(AdditionalVisits$Table.VISITDATE).between(dateFrom).and(dateTo))
                        .queryList();
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                if(visitList!=null){
                    for(int i=0;i<visitList.size();i++){
                        JoinClientRoutePlan joinClientRoutePlan=new JoinClientRoutePlan();
                        joinClientRoutePlan.Client_First_Name=visitList.get(i).clientName;
                        joinClientRoutePlan.CustomerName=visitList.get(i).customer_name;
                        joinClientRoutePlan.Client_Prefix=visitList.get(i).time_availability;
                        joinClientRoutePlan.Location=visitList.get(i).location;
                        data.add(joinClientRoutePlan);
                    }
                    adapter.setData(data);
                    dissmissProgressDialog();
                }
                dissmissProgressDialog();
            }
        }.execute();
    }
}
