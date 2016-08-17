package com.field.datamatics.views.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.field.datamatics.R;
import com.field.datamatics.Services.ApiService;
import com.field.datamatics.Services.appservices.SignatureUploadService;
import com.field.datamatics.apimodels.CommonSubmitJson;
import com.field.datamatics.apimodels.SendPendingRemarks;
import com.field.datamatics.constants.ApiConstants;
import com.field.datamatics.constants.Constants;
import com.field.datamatics.database.*;
import com.field.datamatics.database.RoutePlan;
import com.field.datamatics.interfaces.ApiCallbacks;
import com.field.datamatics.json.VisitHomePageJson;
import com.field.datamatics.synctables.SyncRemarks;
import com.field.datamatics.synctables.SyncVisitDetails;
import com.field.datamatics.utils.AppControllerUtil;
import com.field.datamatics.utils.PreferenceUtil;
import com.field.datamatics.utils.Utilities;
import com.field.datamatics.views.MainActivity;
import com.field.datamatics.views.dialogs.AddToPendingDialog;
import com.google.gson.Gson;
import com.raizlabs.android.dbflow.data.Blob;
import com.raizlabs.android.dbflow.sql.builder.Condition;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.raizlabs.android.dbflow.sql.language.Update;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

/**
 * Created by Jith on 18/10/2015.
 */
public class VisitHomePage extends BaseFragment implements View.OnClickListener {
    private CardView cv_check_in;
    private CardView cv_meet_client;
    private CardView cv_add_to_pending;
    private TextView tv_name;
    private TextView tv_gender;
    private TextView tv_speciality;
    private TextView tv_market_class;
    private TextView tv_ste_class;
    private TextView tv_email;
    private TextView tv_phone;
    private TextView tv_mobile;
    private TextView tv_fax;
    private TextView tv_type;
    private TextView tv_nationality;
    private TextView tv_Customer;
    private TextView tv_RoutePlanNumber;
    private TextView tv_RoutePlandDate;
    private TextView tvVisitedDate;
    private TextView tvCheckinTime;
    private TextView tvCheckOutTime;
    private TextView tvSessionEnd;
    private TextView tvFeedback;
    private TextView tvClientNumber;

    private int routePlanNumber;
    private int appointmentId;
    private int clientId;

    private String checkInTime;
    private String checkOutTime;
    private String checkInCoordinates = "";
    private String checkOutCoordinates = "";

    private String clientName = "";
    private String customerName = "";

    private Bundle data;
    private boolean isPending;
    private boolean isPendingTab;
    private boolean isVisited;
    private boolean disableActivities;
    private boolean isAdditional;
    private String time="";
    private String location="";
    private String speciality="";
    private String client_name="";


    private MainActivity mContainer;


    public VisitHomePage() {
    }

    public static VisitHomePage getInstance(Bundle data) {
        VisitHomePage visitHomePage = new VisitHomePage();
        visitHomePage.setArguments(data);
        return visitHomePage;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_visit_home_page, container, false);
        data = getArguments();
        mContainer = (MainActivity) getActivity();
        SharedPreferences.Editor edit = AppControllerUtil.getPrefsResume().edit();
        if ((isPending = data.getBoolean("is_pending", false))) {
            edit.putInt(Constants.PREF_CURRENT_ACTIVITY, Constants.STAT_VISIT_HOME_PENDING);
            addFragmentTitle("PendingDetails");
            setTitle("Pending Visits");
        } else if (data.getBoolean("disable_activities", false)) {
            addFragmentTitle("ScheduleVsActualDetails");
        } else {
            isPendingTab = data.getBoolean("is_pending_tab", false);
            edit.putInt(Constants.PREF_CURRENT_ACTIVITY, Constants.STAT_VISIT_HOME);
            addFragmentTitle("VisitHomePage");
            setTitle("Today's Visit");
        }
        edit.putString(Constants.PREF_DATE, Utilities.dateToString(Calendar.getInstance(), "yyyy-MM-dd"));
        edit.commit();
        isVisited = data.getBoolean("is_visited", false);
        initializeViews(view);
        if(data.getBoolean("is_additional",false)){
            addFragmentTitle("Additional Visit");
            setTitle("Additional Visit");
            LinearLayout containerRPN= (LinearLayout) view.findViewById(R.id.containerRPN);
            LinearLayout containerRPD= (LinearLayout) view.findViewById(R.id.containerRPD);
            containerRPN.setVisibility(View.GONE);
            containerRPD.setVisibility(View.GONE);
            isAdditional=true;
        }
        try {
            AppControllerUtil.getInstance().setCurrent_fragment(data.getString("fragmentName"));
            setTitle(data.getString("fragmentName"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        setData();
        return view;
    }

    private void initializeViews(View view) {
        cv_check_in = (CardView) view.findViewById(R.id.cv_check_in);
        cv_meet_client = (CardView) view.findViewById(R.id.cv_meet_client);
        cv_add_to_pending = (CardView) view.findViewById(R.id.cv_add_to_pending);
        tv_name = (TextView) view.findViewById(R.id.tv_client_name);
        tv_gender = (TextView) view.findViewById(R.id.tv_gender);
        tv_speciality = (TextView) view.findViewById(R.id.tv_speciality);
        tv_market_class = (TextView) view.findViewById(R.id.tv_market_class);
        tv_ste_class = (TextView) view.findViewById(R.id.tv_ste_class);
        tv_email = (TextView) view.findViewById(R.id.tv_email);
        tv_phone = (TextView) view.findViewById(R.id.tv_phno);
        tv_mobile = (TextView) view.findViewById(R.id.tv_mobile);
        tv_fax = (TextView) view.findViewById(R.id.tv_fax);
        tv_type = (TextView) view.findViewById(R.id.tv_type);
        tv_nationality = (TextView) view.findViewById(R.id.tv_nationality);
        tv_Customer = (TextView) view.findViewById(R.id.tv_customer_name);
        tv_RoutePlandDate = (TextView) view.findViewById(R.id.tv_route_plan_date);
        tv_RoutePlanNumber = (TextView) view.findViewById(R.id.tv_route_plan_number);
        tvVisitedDate = (TextView) view.findViewById(R.id.tv_visited_date);
        tvCheckinTime = (TextView) view.findViewById(R.id.tv_check_in_time);
        tvCheckOutTime = (TextView) view.findViewById(R.id.tv_check_out_time);
        tvSessionEnd = (TextView) view.findViewById(R.id.tv_session_end);
        tvFeedback = (TextView) view.findViewById(R.id.tv_feedback);
        tvClientNumber = (TextView) view.findViewById(R.id.tv_client_number);

        if (isPending | isPendingTab) {
            cv_add_to_pending.setVisibility(View.GONE);
//            cv_meet_client.setVisibility(View.GONE);
//            cv_check_in.setVisibility(View.GONE);
        }
        if (isVisited) {
            view.findViewById(R.id.layout_visited).setVisibility(View.VISIBLE);
        }

        if (data.getBoolean("disable_activities", false)) {
            cv_add_to_pending.setVisibility(View.GONE);
            cv_meet_client.setVisibility(View.GONE);
            cv_check_in.setVisibility(View.GONE);
        }

        cv_check_in.setOnClickListener(this);
        cv_meet_client.setOnClickListener(this);
        cv_add_to_pending.setOnClickListener(this);

    }

    private void setData() {
        if (data != null) {
            if (data.containsKey("values")) {
                ArrayList<String> values = data.getStringArrayList("values");
                tv_name.setText((clientName = values.get(0)));
                tv_gender.setText(values.get(1));
                tv_speciality.setText(values.get(2));
                tv_market_class.setText(values.get(3));
                tv_ste_class.setText(values.get(4));
                tv_email.setText(values.get(5));
                tv_phone.setText(values.get(6));
                tv_mobile.setText(values.get(7));
                tv_fax.setText(values.get(8));
                tv_type.setText(values.get(9));
                tv_nationality.setText(values.get(10));
                tv_Customer.setText((customerName = values.get(11)));
                tv_RoutePlanNumber.setText(values.get(12));
                tv_RoutePlandDate.setText(values.get(13));
                try {
                    time=values.get(14);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    location=values.get(15);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (isVisited) {
                    tvVisitedDate.setText(Utilities.dateToString(values.get(14), "yyyy-MM-dd", "MMM dd, yyyy"));
                    tvCheckinTime.setText(Utilities.dateToString(values.get(15), "yyyy-MM-dd HH:mm:ss", "MMM dd, yyyy hh:mm a"));
                    tvCheckOutTime.setText(Utilities.dateToString(values.get(16), "yyyy-MM-dd HH:mm:ss", "MMM dd, yyyy hh:mm a"));
                    tvSessionEnd.setText(Utilities.dateToString(values.get(17), "yyyy-MM-dd HH:mm:ss", "MMM dd, yyyy hh:mm a"));
                    tvFeedback.setText(values.get(18));
                }
            }
            routePlanNumber = data.getInt("route_plan_number");
            appointmentId = data.getInt("appointment_id", -1);
            clientId = data.getInt("client_id");
            tvClientNumber.setText(String.valueOf(clientId));
            AppControllerUtil.getInstance().setRoutePlanNumber(routePlanNumber);
        }
    }

    @Override
    public void onClick(View v) {
        if (v == cv_check_in) {
            AppControllerUtil.setCheckinTime(System.currentTimeMillis());
            checkInTime = Utilities.dateToString(Calendar.getInstance(), "yyyy-MM-dd HH:mm:ss");
            cv_check_in.setVisibility(View.GONE);
            cv_meet_client.setVisibility(View.VISIBLE);
            AppControllerUtil.getInstance().setIsCheckIn(true);
            Location loc = mContainer.mLastLocation;
            if (loc != null)
                checkInCoordinates = loc.getLatitude() + "," + loc.getLongitude();
            if(routePlanNumber!=-1){
                String today = Utilities.dateToString(Calendar.getInstance(), "yyyy-MM-dd");
                RoutePlan updateRoutePlan=new Select().from(RoutePlan.class)
                        .where(Condition.column(RoutePlan$Table.ROUTE_PLAN_NUMBER).eq(routePlanNumber))
                        .querySingle();
                updateRoutePlan.Date=today;
                updateRoutePlan.update();
            }
        } else if (v == cv_meet_client) {
            checkOutTime = Utilities.dateToString(Calendar.getInstance(), "yyyy-MM-dd HH:mm:ss");
            Location loc = mContainer.mLastLocation;
            if (loc != null)
                checkOutCoordinates = loc.getLatitude() + "," + loc.getLongitude();
            Bundle data = new Bundle();
            data.putInt("route_plan_number", routePlanNumber);
            data.putInt("appointment_id", appointmentId);
            data.putInt("client_id", clientId);
            data.putString("checkin", checkInTime);
            data.putString("checkout", checkOutTime);
            data.putString("checkin_coordinates", checkInCoordinates);
            data.putString("checkout_coordinates", checkOutCoordinates);
            data.putBoolean("is_pending", isPending);
            try {
                data.putString("fragmentName",data.getString("fragmentName"));
            } catch (Exception e) {
                e.printStackTrace();
            }
            //check is additional visit.
            try {
                if(isAdditional){
                    AdditionalVisits additionalVisits=new AdditionalVisits();
                    additionalVisits.visitdate=Utilities.dateToString(Calendar.getInstance(), "yyyy-MM-dd");
                    additionalVisits.clientName=tv_name.getText().toString().replaceAll("null","");
                    additionalVisits.customer_name=tv_Customer.getText().toString().replaceAll("null","");
                    additionalVisits.time_availability=time;
                    additionalVisits.location=location;
                    additionalVisits.gender=tv_gender.getText().toString().replaceAll("null","")+"";
                    additionalVisits.speciality=tv_speciality.getText().toString().replaceAll("null","")+"";
                    additionalVisits.marketClass=tv_market_class.getText().toString().replaceAll("null","")+"";
                    additionalVisits.steClass=tv_ste_class.getText().toString().replaceAll("null","")+"";
                    additionalVisits.email=tv_email.getText().toString().replaceAll("null","")+"";
                    additionalVisits.phone=tv_phone.getText().toString().replaceAll("null","")+"";
                    additionalVisits.mob=tv_mobile.getText().toString().replaceAll("null","")+"";
                    additionalVisits.fax=tv_fax.getText().toString().replaceAll("null","")+"";
                    additionalVisits.type=tv_type.getText().toString().replaceAll("null","")+"";
                    additionalVisits.nationality=tv_nationality.getText().toString().replaceAll("null","")+"";
                    additionalVisits.save();

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            /* to retain values in case of unexpected shutdown*/
            VisitHomePageJson jsonClass = new VisitHomePageJson(routePlanNumber, appointmentId, clientId, checkInTime, checkOutTime, checkInCoordinates, checkOutCoordinates, isPending);
            String jsonValue = new Gson().toJson(jsonClass);
            AppControllerUtil.getPrefsResume().edit().putString(Constants.VAL_MR_ACTION, jsonValue).commit();
            addFragment(MrActions.getInstance(data, true));
            AppControllerUtil.getInstance().setIsCheckIn(false);
        } else if (v == cv_add_to_pending) {
            manageAddToPending();
        }

    }

    private void manageAddToPending(){
        AddToPendingDialog.getInstance().showDialog(getActivity(), false, false, new AddToPendingDialog.GetPositionCallBack() {
            @Override
            public void getPosition(int position) {
                switch (position){
                    case 0:
                        setRemarks();
                        break;
                    case 1:
                        setRemainder();
                        break;
                    case 2:
                        addTopending();
                        break;
                }
            }
        });
    }
    private void setRemarks(){
        final Dialog notesDialog = new Dialog(getContext());
        //notesDialog.setCancelable(false);
        //notesDialog.setCanceledOnTouchOutside(false);
        notesDialog.setContentView(R.layout.dialog_remark_dialog);
        notesDialog.setTitle("Remark");
        AppCompatButton btnDone = (AppCompatButton) notesDialog.findViewById(R.id.btn_done);
        final EditText edtNotes = (EditText) notesDialog.findViewById(R.id.edt_notes);
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //notes = edtNotes.getText().toString();
                PendingRemarks pendingRemarks=new PendingRemarks();
                pendingRemarks.remarks=edtNotes.getText().toString();
                pendingRemarks.datetime=Utilities.dateToString(Calendar.getInstance(), "yyyy-MM-dd hh:mm");
                pendingRemarks.routeplanno=routePlanNumber;
                pendingRemarks.client_ID=clientId+"";
                pendingRemarks.save();
                notesDialog.dismiss();
                sendRemarksToServer(pendingRemarks);
            }
        });
        notesDialog.show();
    }
    private void setRemainder(){
        StringBuilder sb = new StringBuilder();
        sb.append("Route Plan no. ")
                .append(routePlanNumber)
                .append(",\n").append("Client : ")
                .append(clientName)
                .append(",\n")
                .append("Customer : ")
                .append(customerName)
                .append(",\n")
                .append("Actual visit date :")
                .append(Utilities.dateInSqliteFormat(Calendar.getInstance()));
        addFragment(AddReminder.getInstance(sb.toString(),routePlanNumber),"Remainder",true);
    }
    private void addTopending(){
        mContainer.shouldUpdatePerformanceGraph = true;
        mContainer.shouldUpdateScore = true;
        AppControllerUtil.getInstance().setIsCheckIn(false);
        //Time spent calculation
        long timeSpent = System.currentTimeMillis() - AppControllerUtil.getCheckinTime();
        SharedPreferences pref = AppControllerUtil.getPrefs();
        SharedPreferences.Editor editor = pref.edit();
        editor.putLong(Constants.PREF_TIME_SPENT, pref.getLong(Constants.PREF_TIME_SPENT, 0) + timeSpent);
        editor.commit();
        /**
        * Save time spent to database table
        */
        TimeSpendInMarket timeSpendInMarket = new TimeSpendInMarket();
        timeSpendInMarket.date_ = Utilities.dateToString(Calendar.getInstance(), "yyyy-MM-dd");
        timeSpendInMarket.time_spend = Math.round((timeSpent / 1000));
        timeSpendInMarket.save();

        mContainer.shouldUpdateTimeSpent = true;

        RoutePlan r = new Select().from(RoutePlan.class)
                .where(Condition.column(RoutePlan$Table.ROUTE_PLAN_NUMBER)
                        .eq(routePlanNumber)).querySingle();
        r.Visittype = 2;

        final VisitedDetails visitedDetails = new VisitedDetails();
        visitedDetails.routePlan = r;
        visitedDetails.appointment = new Appointment();
        if (appointmentId > 0)
            visitedDetails.appointment.Appointment_Id = appointmentId;
        visitedDetails.Visited_Date = Utilities.dateInSqliteFormat(Calendar.getInstance());
        visitedDetails.checkintime = checkInTime;
        visitedDetails.checkouttime = checkOutTime;
        visitedDetails.Sessionend = checkOutTime;
        visitedDetails.status = 2;
        visitedDetails.signature = new Blob(new byte[1]);
        visitedDetails.Geo_Cordinates_in = checkInCoordinates;
        visitedDetails.Geo_Cordinates_out = checkOutCoordinates;
        String coordinates = "";
        Location loc = mContainer.mLastLocation;
        if (loc != null)
            coordinates = loc.getLatitude() + "," + loc.getLongitude();
        visitedDetails.Geo_Cordinates_sessout = coordinates;

        visitedDetails.save();
        r.update();
        addFragment(TodaysVisitTabbed.getInstance());
    }
    private void sendRemarksToServer(PendingRemarks pendingRemarks){
        final Gson gson = new Gson();
        SendPendingRemarks sendPendingRemarks=new SendPendingRemarks(pendingRemarks.remark_id,
                pendingRemarks.routeplanno,pendingRemarks.datetime,pendingRemarks.remarks,pendingRemarks.client_ID);
        HashMap<String,SendPendingRemarks>data=new HashMap<>();
        data.put("pendingRemark",sendPendingRemarks);
        final CommonSubmitJson commonSubmitJson = new CommonSubmitJson(data);
        Log.d("REMARK", gson.toJson(commonSubmitJson));
        showProgressDialog();
        if (PreferenceUtil.getIntsance().isSyncManual()) {
            dissmissProgressDialog();
            //keep to local DB
            SyncRemarks remarks = new SyncRemarks();
            remarks.remarks = gson.toJson(commonSubmitJson);
            remarks.save();
            dissmissProgressDialog();
        }
        else {
            ApiService.getInstance().makeApiCall(ApiConstants.AppSendRemarks, commonSubmitJson, new ApiCallbacks() {
                @Override
                public void onSuccess(Object objects) {
                    dissmissProgressDialog();
                }

                @Override
                public void onError(Object objects) {
                    dissmissProgressDialog();
                    //keep to local DB
                    SyncRemarks remarks = new SyncRemarks();
                    remarks.remarks = gson.toJson(commonSubmitJson);
                    remarks.save();
                }

                @Override
                public void onErrorMessage(String message) {

                }
            });
        }

    }
}
