package com.field.datamatics.views.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.field.datamatics.R;
import com.field.datamatics.Services.ApiService;
import com.field.datamatics.apimodels.AppoinmentResponseBody;
import com.field.datamatics.apimodels.AppointmentSubmitJson;
import com.field.datamatics.apimodels.CommonSubmitJson;
import com.field.datamatics.constants.ApiConstants;
import com.field.datamatics.database.Appointment;
import com.field.datamatics.database.Appointment$Table;
import com.field.datamatics.database.RoutePlan$Table;
import com.field.datamatics.interfaces.ApiCallbacks;
import com.field.datamatics.interfaces.DialogCallBacks;
import com.field.datamatics.synctables.SyncAppointment;
import com.field.datamatics.utils.DialogUtil;
import com.field.datamatics.utils.PreferenceUtil;
import com.field.datamatics.views.adapters.ClientListAdapter_;
import com.google.gson.Gson;
import com.raizlabs.android.dbflow.sql.builder.Condition;
import com.raizlabs.android.dbflow.sql.language.Select;

import java.util.ArrayList;

/**
 * Created by anoop on 15/10/15.
 * Take new appointment screen logic
 */
public class TakeNewAppointment extends BaseFragment {
    private RecyclerView rv_list;
    private ClientListAdapter_ adapter;
    private  ArrayList<com.field.datamatics.database.RoutePlan>route_plan=new ArrayList<com.field.datamatics.database.RoutePlan>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_client_list,container,false);
        addFragmentTitle("TakeNewAppointment");
        rv_list= (RecyclerView) view.findViewById(R.id.rv_list);
        TakeAnAppointment.isShowMsg=false;

        try {
            readNewClient();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), "No route plan available for the day", Toast.LENGTH_SHORT).show();
            addFragment(new TakeAnAppointment());
        }
        return view;
    }
    private void listSetUp(){
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv_list.setLayoutManager(llm);
        adapter=new ClientListAdapter_(getActivity(),route_plan);
        rv_list.setAdapter(adapter);
        registerEvents();
    }
    private void registerEvents(){
        adapter.setOnItemClickListener(new ClientListAdapter_.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                try {
                    Appointment appointment = new Appointment();
                    appointment.Appointment_Date = TakeAnAppointment.date;
                    appointment.routePlan = route_plan.get(position);
                    appointment.client = route_plan.get(position).client;
                    appointment.customer = route_plan.get(position).customer;
                    appointment.save();


                    //Make an api call
                    AppoinmentResponseBody body = new AppoinmentResponseBody();
                    body.setCustomerid(route_plan.get(position).customer.Customer_Id + "");
                    body.setRoutplanno(route_plan.get(position).Route_Plan_Number + "");
                    body.setClientno(route_plan.get(position).client.Client_Number + "");
                    body.setAppdate(TakeAnAppointment.date + "");
                    body.setRemarks("");
                    ArrayList<AppoinmentResponseBody> arr = new ArrayList<AppoinmentResponseBody>();
                    arr.add(body);
                    AppointmentSubmitJson appointmentSubmitJson = new AppointmentSubmitJson(ApiConstants.STATUS, arr);
                    submitAppointment(appointmentSubmitJson);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

    }

    private void readNewClient(){
        showProgressDialog();
        route_plan.clear();
        route_plan= (ArrayList<com.field.datamatics.database.RoutePlan>) new Select().from(com.field.datamatics.database.RoutePlan.class).where(Condition.column(RoutePlan$Table.DATE).eq(TakeAnAppointment.date)).queryList();
        ArrayList<Appointment>appointments= (ArrayList<Appointment>) new Select().from(Appointment.class).where(Condition.column(Appointment$Table.APPOINTMENT_DATE).eq(TakeAnAppointment.date)).queryList();
        for(int i=0;i<appointments.size();i++){
            for(int k=0;k<route_plan.size();k++){
                if(appointments.get(i).client.Client_Number==route_plan.get(k).client.Client_Number){
                    route_plan.remove(k);
                    k--;
                }
            }
        }
        dissmissProgressDialog();
        if(route_plan.size()>0){
            listSetUp();
        }
        else {
            DialogUtil.getInstance().getDialog(getActivity(), "New Appointment", "You are not able take appointment right now, " +
                    "because no route  available for the day", new DialogCallBacks() {
                @Override
                public void onOk() {
                    addFragment(new TakeAnAppointment());
                }
            });

        }
    }

    private void submitAppointment(final AppointmentSubmitJson json){
        final Gson gson=new Gson();
        final CommonSubmitJson commonSubmitJson=new CommonSubmitJson(json);
        showProgressDialog();
        if(PreferenceUtil.getIntsance().isSyncManual()){
            //keep to local DB
            SyncAppointment syncAppointment=new SyncAppointment();
            syncAppointment.appointment_details=gson.toJson(commonSubmitJson);
            syncAppointment.save();
            dissmissProgressDialog();
            addFragment(new TakeAnAppointment());
        }
        else{
            ApiService.getInstance().makeApiCall(ApiConstants.AppAppointmentDetails, commonSubmitJson, new ApiCallbacks() {
                @Override
                public void onSuccess(Object objects) {
                    dissmissProgressDialog();
                    addFragment(new TakeAnAppointment());
                }

                @Override
                public void onError(Object objects) {
                    //keep to local DB
                    SyncAppointment syncAppointment=new SyncAppointment();
                    syncAppointment.appointment_details=gson.toJson(commonSubmitJson);
                    syncAppointment.save();
                    dissmissProgressDialog();
                    addFragment(new TakeAnAppointment());
                }

                @Override
                public void onErrorMessage(String message) {

                }
            });
        }



    }

}

