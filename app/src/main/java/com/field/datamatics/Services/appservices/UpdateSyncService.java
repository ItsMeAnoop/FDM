package com.field.datamatics.Services.appservices;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import com.field.datamatics.Services.ApiService;
import com.field.datamatics.apimodels.CommonSubmitJson;
import com.field.datamatics.constants.ApiConstants;
import com.field.datamatics.interfaces.ApiCallbacks;
import com.field.datamatics.interfaces.SyncingCallBack;
import com.field.datamatics.synctables.SyncAppLog;
import com.field.datamatics.synctables.SyncAppointment;
import com.field.datamatics.synctables.SyncGeo;
import com.field.datamatics.synctables.SyncMessage;
import com.field.datamatics.synctables.SyncRemarks;
import com.field.datamatics.synctables.SyncRoutePlan;
import com.field.datamatics.synctables.SyncSurveyDetails;
import com.field.datamatics.synctables.SyncVisitDetails;
import com.google.gson.Gson;
import com.raizlabs.android.dbflow.sql.language.Delete;
import com.raizlabs.android.dbflow.sql.language.Select;

import java.util.ArrayList;

/**
 * Created by anoop on 10/11/15.
 * Service update offline data to server
 */
public class UpdateSyncService extends Service {
    private int PUT_REMARK_API_RETRY = 3;
    private int PUT_APP_LOG_API_RETRY = 3;
    private int PUT_APPOINTMENT_API_RETRY = 3;
    private int PUT_GEO_API_RETRY = 3;
    private int PUT_MESSAGE_API_RETRY = 3;
    private int PUT_VISIT_API_RETRY = 3;
    private Gson gson;
    private ArrayList<SyncAppLog> data;
    private ArrayList<SyncAppointment> app_data;
    private ArrayList<SyncGeo> geo_data;
    private ArrayList<SyncMessage> msg_data;
    private ArrayList<SyncRemarks>remark_data;
    private ArrayList<SyncVisitDetails> visit_data;
    private static SyncingCallBack callBack;

    public static Intent getIntent(SyncingCallBack call,Activity activity){
        callBack =call;
        return new Intent(activity,UpdateSyncService.class);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        gson=new Gson();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        readAllData();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    private void readAllData(){
        remark_data= (ArrayList<SyncRemarks>) new Select().from(SyncRemarks.class).queryList();
        syncRemark();
    }

    /**
     * sync remarks api implementation
     */
    private void syncRemark(){
        PUT_REMARK_API_RETRY--;
        callBack.onPerecentage(0,true);
        if(remark_data!=null&&remark_data.size()>0){
            CommonSubmitJson commonSubmitJson=gson.fromJson(remark_data.get(0).remarks,CommonSubmitJson.class);
            ApiService.getInstance().makeApiCall(ApiConstants.AppSendRemarks, commonSubmitJson, new ApiCallbacks() {
                @Override
                public void onSuccess(Object objects) {
                    remark_data.remove(0);
                    if(remark_data.size()!=0){
                        syncRemark();
                    }
                    else{
                        //clear db data
                        Delete.table(SyncAppLog.class);
                        syncAppLog();
                    }
                }
                @Override
                public void onError(Object objects) {
                    if(PUT_REMARK_API_RETRY >= 0){
                        syncAppLog();
                    }
                    else {
                        callBack.onPerecentage(0, true);
                    }
                }

                @Override
                public void onErrorMessage(String message) {

                }
            });

        }
        else{
            data= (ArrayList<SyncAppLog>) new Select().from(SyncAppLog.class).queryList();
            syncAppLog();
        }
    }

    /**
     * sync log api implementation
     */
    private void syncAppLog(){
        PUT_APP_LOG_API_RETRY--;
        callBack.onPerecentage(5,true);
        if(data!=null&&data.size()>0){
            CommonSubmitJson commonSubmitJson=gson.fromJson(data.get(0).log_details,CommonSubmitJson.class);
            ApiService.getInstance().makeApiCall(ApiConstants.AppLogDetails, commonSubmitJson, new ApiCallbacks() {
                @Override
                public void onSuccess(Object objects) {
                    data.remove(0);
                    if(data.size()!=0){
                        syncAppLog();
                    }
                    else{
                        //clear db data
                        Delete.table(SyncAppLog.class);
                        syncAppointment();
                    }
                }

                @Override
                public void onError(Object objects) {
                    if(PUT_APP_LOG_API_RETRY >= 0){
                        syncAppLog();
                    }
                    else{
                        callBack.onPerecentage(5,true);
                    }
                }

                @Override
                public void onErrorMessage(String message) {

                }
            });

        }
        else{
            app_data= (ArrayList<SyncAppointment>) new Select().from(SyncAppointment.class).queryList();
            syncAppointment();
        }

    }

    /**
     * Sync appointment api implementation
     */
    private void syncAppointment(){
        PUT_APPOINTMENT_API_RETRY--;
        callBack.onPerecentage(20,true);
        if(app_data!=null&&app_data.size()>0){
            CommonSubmitJson commonSubmitJson=gson.fromJson(app_data.get(0).appointment_details,CommonSubmitJson.class);
            ApiService.getInstance().makeApiCall(ApiConstants.AppAppointmentDetails, commonSubmitJson, new ApiCallbacks() {
                @Override
                public void onSuccess(Object objects) {
                    app_data.remove(0);
                    if(app_data.size()!=0){
                        syncAppointment();
                    }
                    else{
                        //clear db data
                        Delete.table(SyncAppointment.class);
                        syncGeo();
                    }
                }
                @Override
                public void onError(Object objects) {
                    if(PUT_APPOINTMENT_API_RETRY >= 0){
                        syncAppointment();
                    }
                    else{
                        callBack.onPerecentage(2,true);
                    }
                }
                @Override
                public void onErrorMessage(String message) {

                }
            });
        }
        else{
            geo_data= (ArrayList<SyncGeo>) new Select().from(SyncGeo.class).queryList();
            syncGeo();
        }

    }

    /**
     * Sync location api implementation
     */
    private void syncGeo(){
        PUT_GEO_API_RETRY--;
        callBack.onPerecentage(40,true);
        if(geo_data!=null&&geo_data.size()>0){
            CommonSubmitJson commonSubmitJson=gson.fromJson(geo_data.get(0).geo_details,CommonSubmitJson.class);
            ApiService.getInstance().makeApiCall(ApiConstants.GeocordinatedUpdat, commonSubmitJson, new ApiCallbacks() {
                @Override
                public void onSuccess(Object objects) {
                    geo_data.remove(0);
                    if(geo_data.size()!=0){
                        syncGeo();
                    }
                    else{
                        //clear data
                        Delete.table(SyncGeo.class);
                        syncMessages();
                    }
                }
                @Override
                public void onError(Object objects) {
                    if(PUT_GEO_API_RETRY >= 0){
                     syncGeo();
                    }
                    else {
                        callBack.onPerecentage(40, true);
                    }
                }
                @Override
                public void onErrorMessage(String message) {

                }
            });

        }
        else{
            msg_data=(ArrayList<SyncMessage>) new Select().from(SyncMessage.class).queryList();
            syncMessages();
        }

    }

    /**
     * sync message api implementation
     */
    private void syncMessages(){
        PUT_MESSAGE_API_RETRY--;
        callBack.onPerecentage(60,true);
        if(msg_data!=null&&msg_data.size()>0){
            CommonSubmitJson commonSubmitJson=gson.fromJson(msg_data.get(0).message_details,CommonSubmitJson.class);
            ApiService.getInstance().makeApiCall(ApiConstants.AppMessageDetails, commonSubmitJson, new ApiCallbacks() {
                @Override
                public void onSuccess(Object objects) {
                    msg_data.remove(0);
                    if(msg_data.size()!=0){
                        syncMessages();
                    }
                    else{
                        //clear data
                        Delete.table(SyncMessage.class);
                        syncVisitDetails();
                    }
                }
                @Override
                public void onError(Object objects) {
                    if(PUT_MESSAGE_API_RETRY >= 0){
                        syncMessages();
                    }
                    else{
                        callBack.onPerecentage(60,true);
                    }
                }

                @Override
                public void onErrorMessage(String message) {

                }
            });

        }
        else{
            visit_data= (ArrayList<SyncVisitDetails>) new Select().from(SyncVisitDetails.class).queryList();
            syncVisitDetails();
        }
    }

    /**
     * sync visit details api implementation
     */
    private void syncVisitDetails(){
        PUT_VISIT_API_RETRY--;
        callBack.onPerecentage(80,true);
        if(visit_data!=null&&visit_data.size()>0){
            CommonSubmitJson commonSubmitJson=gson.fromJson(visit_data.get(0).visit_details,CommonSubmitJson.class);
            ApiService.getInstance().makeApiCall(ApiConstants.AppvisitedDetails, commonSubmitJson, new ApiCallbacks() {
                @Override
                public void onSuccess(Object objects) {
                    visit_data.remove(0);
                    if(visit_data.size()!=0){
                        syncVisitDetails();
                    }
                    else{
                        //clear data
                        Delete.table(SyncVisitDetails.class);
                        callBack.onPerecentage(100,true);
                        stopSelf();
                    }
                }

                @Override
                public void onError(Object objects) {
                    if(PUT_VISIT_API_RETRY >= 0){
                        syncVisitDetails();
                    }
                    else {
                        callBack.onPerecentage(100, true);
                        stopSelf();
                    }

                }

                @Override
                public void onErrorMessage(String message) {

                }
            });
        }
        else{
            callBack.onPerecentage(100,true);
            stopSelf();
        }
    }
    private void nextSchedule(){
        try {

            //Create a new PendingIntent and add it to the AlarmManager
            Intent intent = new Intent(getApplicationContext(), UpdateSyncService.class);
            PendingIntent pendingIntent = PendingIntent.getService(getApplicationContext(),
                    12345, intent, PendingIntent.FLAG_CANCEL_CURRENT);
            AlarmManager am =
                    (AlarmManager)getSystemService(Activity.ALARM_SERVICE);
            am.setRepeating(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime(),
                    2*60*60,pendingIntent);

        } catch (Exception e) {}
    }
}