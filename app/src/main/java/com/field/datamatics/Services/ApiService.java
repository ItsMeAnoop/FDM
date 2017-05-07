package com.field.datamatics.Services;

import android.util.Log;

import com.field.datamatics.apimodels.CommonSubmitJson;
import com.field.datamatics.constants.ApiConstants;
import com.field.datamatics.constants.ViewConstants;
import com.field.datamatics.interfaces.ApiCallbacks;
import com.field.datamatics.utils.NetworkStatusUtil;
import com.field.datamatics.utils.PreferenceUtil;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.QueryMap;

/**
 * Created by anoop on 15/10/15.
 * API service general design
 */
public class ApiService extends BaseService {
    private static ApiService instance = new ApiService();
    private String errorMessage = "Something went wrong,try again";
    private final static String url=ApiConstants.url;

    ApiService() {
    }

    public static ApiService getInstance() {
        return instance;
    }


    //AppLoginDetails
    interface AppLoginDetails {
        @POST("/{link}/" + ApiConstants.AppLoginDetails)
        public void login(@QueryMap Map<String, String> params, @Path("link") String link, Callback<JsonObject> callback);
    }

    //AppUserRegionDetails
    interface AppUserRegionDetails {
        @POST("/{link}/" + ApiConstants.AppUserRegionDetails)
        public void region(@QueryMap Map<String, String> params, @Path("link") String link, Callback<JsonObject> callback);
    }

    //AppViewCustomerDetails
    interface AppViewCustomerDetails {
        @POST("/{link}/" + ApiConstants.AppViewCustomerDetails)
        public void customer(@QueryMap Map<String, String> params, @Path("link") String link, Callback<JsonObject> callback);
    }

    //AppClientDetails
    interface AppClientDetails {
        @POST("/{link}/" + ApiConstants.AppClientDetails)
        public void clients(@QueryMap Map<String, String> params, @Path("link") String link, Callback<JsonObject> callback);
    }

    //AppClietProductDetails
    interface AppClietProductDetails {
        @POST("/{link}/" + ApiConstants.AppClietProductDetails)
        public void client_product(@QueryMap Map<String, String> params, @Path("link") String link, Callback<JsonObject> callback);
    }

    //clientworkcalander
    interface Clientworkcalander {
        @POST("/{link}/" + ApiConstants.AppViewClientcalenderDetails)
        public void clientworkcalander(@QueryMap Map<String, String> params, @Path("link") String link, Callback<JsonObject> callback);
    }

    //routeplan
    interface Routeplan {
        @POST("/{link}/" + ApiConstants.AppViewRoutePlaneDetails)
        public void routeplan(@QueryMap Map<String, String> params, @Path("link") String link, Callback<JsonObject> callback);
    }

    //Reminder API
    interface Reminder {
        @POST("/{link}/" + ApiConstants.AppViewReminderDetails)
        public void remainder(@QueryMap Map<String, String> params, @Path("link") String link, Callback<JsonObject> callback);
    }

    //AppViewAppointmentDetails
    interface Appoinment {
        @POST("/{link}/" + ApiConstants.AppViewAppointmentDetails)
        public void appoinment(@QueryMap Map<String, String> params, @Path("link") String link, Callback<JsonObject> callback);
    }

    //AppViewProductDetails
    interface AppViewProductDetails {
        @POST("/{link}/" + ApiConstants.AppViewProductDetails)
        public void product(@QueryMap Map<String, String> params, @Path("link") String link, Callback<JsonObject> callback);
    }

    //AppSurveyDetails
    interface AppSurveyDetails {
        @POST("/{link}/" + ApiConstants.AppSurveyDetails)
        public void surveyDetails(@QueryMap Map<String, String> params, @Path("link") String link, Callback<JsonObject> callback);
    }

    //SEND APIS

    //AppvisitedDetails
    interface AppvisitedDetails {
        @POST("/{link}/" + ApiConstants.AppvisitedDetails)
        public void visit(@Body CommonSubmitJson commonSubmitJson, @Path("link") String link, Callback<JsonObject> callback);
    }

    //AppAppointmentDetails
    interface AppAppointmentDetails {
        @POST("/{link}/" + ApiConstants.AppAppointmentDetails)
        public void appintment(@Body CommonSubmitJson commonSubmitJson, @Path("link") String link, Callback<JsonObject> callback);
    }

    //AppMessageDetails
    interface AppMessageDetails {
        @POST("/{link}/" + ApiConstants.AppMessageDetails)
        public void messageDetails(@Body CommonSubmitJson commonSubmitJson, @Path("link") String link, Callback<JsonObject> callback);
    }



    //AppRoutePlanDetails
    interface AppRoutePlanDetails {
        @POST("/{link}/" + ApiConstants.AppRoutePlanDetails)
        public void routePlanDetails(@Body CommonSubmitJson commonSubmitJson, @Path("link") String link, Callback<JsonObject> callback);
    }

    //AppLogDetails
    interface AppLogDetails {
        @POST("/{link}/" + ApiConstants.AppLogDetails)
        public void appLogDetails(@Body CommonSubmitJson commonSubmitJson, @Path("link") String link, Callback<JsonObject> callback);
    }

    //GeocordinatedUpdat
    interface GeocordinatedUpdat {
        @POST("/{link}/" + ApiConstants.GeocordinatedUpdat)
        public void geoCordinatedUpdat(@Body CommonSubmitJson commonSubmitJson, @Path("link") String link, Callback<JsonObject> callback);
    }
    //AppViewProductTextFile
    interface AppViewProductTextFile {
        @POST("/{link}/" + ApiConstants.AppViewProductTextFile)
        public void getProductCSV(@Body Map<String, String> params, @Path("link") String link, Callback<JsonObject> callback);
    }
    //AppSendRemark
    interface AppSendRemark {
        @POST("/{link}/" + ApiConstants.AppSendRemarks)
        public void sendRemark(@Body CommonSubmitJson commonSubmitJson, @Path("link") String link, Callback<JsonObject> callback);
    }

    /**
     * Function which make API call based on api type
     * @param type
     * @param objects
     * @param callbacks
     */
    public void makeApiCall(String type, Object objects, final ApiCallbacks callbacks) {
        String myUrl="";
        if(PreferenceUtil.getIntsance().isTesting()){
            myUrl=ApiConstants.url_test;
        }
        else{
            myUrl=ApiConstants.url;
        }
        Log.d("URL",myUrl);
        try {
            Log.d("Data", new Gson().toJson(objects));
        } catch (Throwable e) {
            e.printStackTrace();
        }
        if (NetworkStatusUtil.getInstance().isNetworkAvailable() == false) {
            callbacks.onErrorMessage(ViewConstants.NO_NETWORK);
            callbacks.onError(null);
            return;
        }
        if (type.equals(ApiConstants.AppLoginDetails)) {
            HashMap<String, String> params = (HashMap) objects;
            AppLoginDetails appLoginDetails = getRestAdapter().create(AppLoginDetails.class);
            appLoginDetails.login(params,myUrl ,new Callback<JsonObject>() {
                @Override
                public void success(JsonObject s, Response response) {
                    callbacks.onSuccess(s);

                }

                @Override
                public void failure(RetrofitError error) {
                    callbacks.onErrorMessage(errorMessage);
                    callbacks.onError(error);
                }
            });

        } else if (type.equals(ApiConstants.AppUserRegionDetails)) {
            HashMap<String, String> params = (HashMap) objects;
            AppUserRegionDetails appUserRegionDetails = getRestAdapter().create(AppUserRegionDetails.class);
            appUserRegionDetails.region(params, myUrl ,new Callback<JsonObject>() {
                @Override
                public void success(JsonObject s, Response response) {
                    callbacks.onSuccess(s);

                }

                @Override
                public void failure(RetrofitError error) {
                    callbacks.onErrorMessage(errorMessage);
                    callbacks.onError(error);

                }
            });

        } else if (type.equals(ApiConstants.AppViewCustomerDetails)) {
            HashMap<String, String> params = (HashMap) objects;
            AppViewCustomerDetails appViewCustomerDetails = getRestAdapter().create(AppViewCustomerDetails.class);
            appViewCustomerDetails.customer(params,myUrl , new Callback<JsonObject>() {
                @Override
                public void success(JsonObject s, Response response) {
                    callbacks.onSuccess(s);
                }

                @Override
                public void failure(RetrofitError error) {
                    callbacks.onErrorMessage(errorMessage);
                    callbacks.onError(error);

                }
            });

        } else if (type.equals(ApiConstants.AppClientDetails)) {
            HashMap<String, String> params = (HashMap) objects;
            AppClientDetails appClientDetails = getRestAdapter().create(AppClientDetails.class);
            appClientDetails.clients(params, myUrl ,new Callback<JsonObject>() {
                @Override
                public void success(JsonObject s, Response response) {
                    callbacks.onSuccess(s);

                }

                @Override
                public void failure(RetrofitError error) {
                    callbacks.onErrorMessage(errorMessage);
                    callbacks.onError(error);

                }
            });

        } else if (type.equals(ApiConstants.AppClietProductDetails)) {
            HashMap<String, String> params = (HashMap) objects;
            AppClietProductDetails appClietProductDetails = getRestAdapter().create(AppClietProductDetails.class);
            appClietProductDetails.client_product(params, myUrl ,new Callback<JsonObject>() {
                @Override
                public void success(JsonObject s, Response response) {
                    callbacks.onSuccess(s);

                }

                @Override
                public void failure(RetrofitError error) {
                    callbacks.onErrorMessage(errorMessage);
                    callbacks.onError(error);

                }
            });

        } else if (type.equals(ApiConstants.AppViewClientcalenderDetails)) {
            HashMap<String, String> params = (HashMap) objects;
            Clientworkcalander clientworkcalander = getRestAdapter().create(Clientworkcalander.class);
            clientworkcalander.clientworkcalander(params, myUrl ,new Callback<JsonObject>() {
                @Override
                public void success(JsonObject s, Response response) {
                    callbacks.onSuccess(s);

                }

                @Override
                public void failure(RetrofitError error) {
                    callbacks.onErrorMessage(errorMessage);
                    callbacks.onError(error);

                }
            });

        } else if (type.equals(ApiConstants.AppViewRoutePlaneDetails)) {
            HashMap<String, String> params = (HashMap) objects;
            Routeplan routeplan = getRestAdapter().create(Routeplan.class);
            routeplan.routeplan(params,myUrl , new Callback<JsonObject>() {
                @Override
                public void success(JsonObject s, Response response) {
                    callbacks.onSuccess(s);

                }

                @Override
                public void failure(RetrofitError error) {
                    callbacks.onErrorMessage(errorMessage);
                    callbacks.onError(error);

                }
            });

        } else if (type.equals(ApiConstants.AppViewReminderDetails)) {
            HashMap<String, String> params = (HashMap) objects;
            Reminder reminder = getRestAdapter().create(Reminder.class);
            reminder.remainder(params, myUrl ,new Callback<JsonObject>() {
                @Override
                public void success(JsonObject s, Response response) {
                    callbacks.onSuccess(s);

                }

                @Override
                public void failure(RetrofitError error) {
                    callbacks.onErrorMessage(errorMessage);
                    callbacks.onError(error);

                }
            });

        } else if (type.equals(ApiConstants.AppViewAppointmentDetails)) {
            HashMap<String, String> params = (HashMap) objects;
            Appoinment appoinment = getRestAdapter().create(Appoinment.class);
            appoinment.appoinment(params,myUrl , new Callback<JsonObject>() {
                @Override
                public void success(JsonObject s, Response response) {
                    callbacks.onSuccess(s);

                }

                @Override
                public void failure(RetrofitError error) {
                    callbacks.onErrorMessage(errorMessage);
                    callbacks.onError(error);

                }
            });

        } else if (type.equals(ApiConstants.AppViewProductDetails)) {
            HashMap<String, String> params = (HashMap) objects;
            AppViewProductDetails appViewProductDetails = getRestAdapter().create(AppViewProductDetails.class);
            appViewProductDetails.product(params, myUrl ,new Callback<JsonObject>() {
                @Override
                public void success(JsonObject s, Response response) {
                    callbacks.onSuccess(s);

                }

                @Override
                public void failure(RetrofitError error) {
                    callbacks.onErrorMessage(errorMessage);
                    callbacks.onError(error);

                }
            });

        } else if (type.equals(ApiConstants.AppvisitedDetails)) {
            CommonSubmitJson commonSubmitJson=(CommonSubmitJson)objects;
            AppvisitedDetails appvisitedDetails = getRestAdapter().create(AppvisitedDetails.class);
            appvisitedDetails.visit(commonSubmitJson,myUrl , new Callback<JsonObject>() {
                @Override
                public void success(JsonObject s, Response response) {
                    callbacks.onSuccess(s);

                }

                @Override
                public void failure(RetrofitError error) {
                    callbacks.onErrorMessage(errorMessage);
                    callbacks.onError(error);

                }
            });

        } else if (type.equals(ApiConstants.AppAppointmentDetails)) {
            CommonSubmitJson commonSubmitJson=(CommonSubmitJson)objects;
            AppAppointmentDetails appAppointmentDetails = getRestAdapter().create(AppAppointmentDetails.class);
            appAppointmentDetails.appintment(commonSubmitJson,myUrl , new Callback<JsonObject>() {
                @Override
                public void success(JsonObject s, Response response) {
                    callbacks.onSuccess(s);

                }

                @Override
                public void failure(RetrofitError error) {
                    callbacks.onErrorMessage(errorMessage);
                    callbacks.onError(error);

                }
            });

        } else if (type.equals(ApiConstants.AppMessageDetails)) {
            CommonSubmitJson commonSubmitJson=(CommonSubmitJson)objects;
            AppMessageDetails appMessageDetails = getRestAdapter().create(AppMessageDetails.class);
            appMessageDetails.messageDetails(commonSubmitJson,myUrl , new Callback<JsonObject>() {
                @Override
                public void success(JsonObject s, Response response) {
                    callbacks.onSuccess(s);

                }

                @Override
                public void failure(RetrofitError error) {
                    callbacks.onErrorMessage(errorMessage);
                    callbacks.onError(error);

                }
            });

        } else if (type.equals(ApiConstants.AppSurveyDetails)) {
            HashMap<String, String> params = (HashMap) objects;
            AppSurveyDetails appSurveyDetails = getRestAdapter().create(AppSurveyDetails.class);
            appSurveyDetails.surveyDetails(params, myUrl ,new Callback<JsonObject>() {
                @Override
                public void success(JsonObject s, Response response) {
                    callbacks.onSuccess(s);

                }

                @Override
                public void failure(RetrofitError error) {
                    callbacks.onErrorMessage(errorMessage);
                    callbacks.onError(error);

                }
            });

        } else if (type.equals(ApiConstants.AppRoutePlanDetails)) {
            CommonSubmitJson commonSubmitJson=(CommonSubmitJson)objects;
            AppRoutePlanDetails appRoutePlanDetails = getRestAdapter().create(AppRoutePlanDetails.class);
            appRoutePlanDetails.routePlanDetails(commonSubmitJson,myUrl , new Callback<JsonObject>() {
                @Override
                public void success(JsonObject s, Response response) {
                    callbacks.onSuccess(s);

                }

                @Override
                public void failure(RetrofitError error) {
                    callbacks.onErrorMessage(errorMessage);
                    callbacks.onError(error);

                }
            });

        } else if (type.equals(ApiConstants.AppLogDetails)) {
            CommonSubmitJson commonSubmitJson=(CommonSubmitJson)objects;
            AppLogDetails appLogDetails = getRestAdapter().create(AppLogDetails.class);
            appLogDetails.appLogDetails(commonSubmitJson,myUrl , new Callback<JsonObject>() {
                @Override
                public void success(JsonObject s, Response response) {
                    callbacks.onSuccess(s);

                }

                @Override
                public void failure(RetrofitError error) {
                    callbacks.onErrorMessage(errorMessage);
                    callbacks.onError(error);

                }
            });

        } else if (type.equals(ApiConstants.GeocordinatedUpdat)) {
            CommonSubmitJson commonSubmitJson=(CommonSubmitJson)objects;
            GeocordinatedUpdat geocordinatedUpdat = getRestAdapter().create(GeocordinatedUpdat.class);
            geocordinatedUpdat.geoCordinatedUpdat(commonSubmitJson,myUrl , new Callback<JsonObject>() {
                @Override
                public void success(JsonObject s, Response response) {
                    callbacks.onSuccess(s);

                }

                @Override
                public void failure(RetrofitError error) {
                    callbacks.onErrorMessage(errorMessage);
                    callbacks.onError(error);

                }
            });

        }
        else if (type.equals(ApiConstants.AppViewProductTextFile)) {
            HashMap<String, String> params = (HashMap) objects;
            AppViewProductTextFile appViewProductTextFile = getRestAdapter().create(AppViewProductTextFile.class);
            appViewProductTextFile.getProductCSV(params,myUrl , new Callback<JsonObject>() {
                @Override
                public void success(JsonObject s, Response response) {
                    callbacks.onSuccess(s);

                }

                @Override
                public void failure(RetrofitError error) {
                    callbacks.onErrorMessage(errorMessage);
                    callbacks.onError(error);

                }
            });

        }
        else if (type.equals(ApiConstants.AppSendRemarks)) {
            CommonSubmitJson commonSubmitJson=(CommonSubmitJson)objects;
            AppSendRemark appSendRemark = getRestAdapter().create(AppSendRemark.class);
            appSendRemark.sendRemark(commonSubmitJson,myUrl , new Callback<JsonObject>() {
                @Override
                public void success(JsonObject s, Response response) {
                    callbacks.onSuccess(s);

                }

                @Override
                public void failure(RetrofitError error) {
                    callbacks.onErrorMessage(errorMessage);
                    callbacks.onError(error);

                }
            });

        }
    }
}
