package com.field.datamatics.Services.appservices;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.field.datamatics.Services.ApiService;
import com.field.datamatics.apimodels.AppointmentSubmitJson;
import com.field.datamatics.apimodels.MessageJson;
import com.field.datamatics.apimodels.RoutePlanSubmitJson;
import com.field.datamatics.apimodels.SurveyDetailsJson;
import com.field.datamatics.apimodels.VisitJson;
import com.field.datamatics.constants.ApiConstants;
import com.field.datamatics.interfaces.ApiCallbacks;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by anoop on 8/11/15.
 * service to sync data to server
 */
public class DataSubmitSyncingService extends Service {
    private String encription_key = "5994471abb01112afcc18159f6cc74b4f511b99806da59b3caf5a9c173cacfc5";
    private Gson gson = new Gson();
    public static ArrayList<VisitJson> visitJsons = new ArrayList<VisitJson>();
    public static ArrayList<AppointmentSubmitJson> appointmentSubmitJsons = new ArrayList<AppointmentSubmitJson>();
    public static ArrayList<SurveyDetailsJson> surveyDetailsJsons = new ArrayList<SurveyDetailsJson>();
    public static ArrayList<MessageJson> messageJsons = new ArrayList<MessageJson>();
    public static ArrayList<RoutePlanSubmitJson> routePlanSubmitJsons = new ArrayList<RoutePlanSubmitJson>();


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void syncVisitDetails() {
        if (visitJsons.size() >= 0) {
            HashMap<String, String> params = new HashMap<String, String>();
            params.put("encription_key", encription_key);
            params.put("json", gson.toJson(visitJsons.get(0)));
            ApiService.getInstance().makeApiCall(ApiConstants.AppvisitedDetails, params, new ApiCallbacks() {
                @Override
                public void onSuccess(Object objects) {
                    String response = (String) objects;
                    visitJsons.remove(0);
                    syncVisitDetails();

                }

                @Override
                public void onError(Object objects) {
                    syncVisitDetails();
                }

                @Override
                public void onErrorMessage(String message) {

                }
            });
        } else {
            syncAppointmnt();
        }

    }


    private void syncAppointmnt() {
        if (appointmentSubmitJsons.size() >= 0) {
            HashMap<String, String> params = new HashMap<String, String>();
            params.put("encription_key", encription_key);
            params.put("json", gson.toJson(appointmentSubmitJsons.get(0)));
            ApiService.getInstance().makeApiCall(ApiConstants.AppAppointmentDetails, params, new ApiCallbacks() {
                @Override
                public void onSuccess(Object objects) {
                    String response = (String) objects;
                    visitJsons.remove(0);
                    syncVisitDetails();

                }

                @Override
                public void onError(Object objects) {
                    syncAppointmnt();
                }

                @Override
                public void onErrorMessage(String message) {

                }
            });
        } else {
            syncMessage();
        }

    }

    private void syncMessage() {
        if (messageJsons.size() >= 0) {
            HashMap<String, String> params = new HashMap<String, String>();
            params.put("encription_key", encription_key);
            params.put("json", gson.toJson(messageJsons.get(0)));
            ApiService.getInstance().makeApiCall(ApiConstants.AppMessageDetails, params, new ApiCallbacks() {
                @Override
                public void onSuccess(Object objects) {
                    String response = (String) objects;
                    visitJsons.remove(0);
                    syncVisitDetails();
                }

                @Override
                public void onError(Object objects) {
                    syncMessage();
                }

                @Override
                public void onErrorMessage(String message) {

                }
            });
        } else {
            syncSurveyDetails();

        }

    }

    private void syncSurveyDetails() {
        if (surveyDetailsJsons.size() >= 0) {
            HashMap<String, String> params = new HashMap<String, String>();
            params.put("encription_key", encription_key);
            params.put("json", gson.toJson(surveyDetailsJsons.get(0)));
            ApiService.getInstance().makeApiCall(ApiConstants.AppSurveyDetails, params, new ApiCallbacks() {
                @Override
                public void onSuccess(Object objects) {
                    String response = (String) objects;
                    visitJsons.remove(0);
                    syncVisitDetails();
                }

                @Override
                public void onError(Object objects) {
                    syncSurveyDetails();
                }

                @Override
                public void onErrorMessage(String message) {

                }
            });
        } else {
            syncAppRoutePlanDetails();
        }

    }

    private void syncAppRoutePlanDetails() {
        if (routePlanSubmitJsons.size() >= 0) {
            HashMap<String, String> params = new HashMap<String, String>();
            params.put("encription_key", encription_key);
            params.put("json", gson.toJson(routePlanSubmitJsons.get(0)));
            ApiService.getInstance().makeApiCall(ApiConstants.AppRoutePlanDetails, params, new ApiCallbacks() {
                @Override
                public void onSuccess(Object objects) {
                    String response = (String) objects;
                    visitJsons.remove(0);
                    syncVisitDetails();

                }

                @Override
                public void onError(Object objects) {
                    syncAppRoutePlanDetails();
                }

                @Override
                public void onErrorMessage(String message) {

                }
            });
        } else {
            //service stoped
            stopSelf();

        }

    }
}
