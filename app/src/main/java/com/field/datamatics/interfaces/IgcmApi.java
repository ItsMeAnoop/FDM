package com.field.datamatics.interfaces;

import com.field.datamatics.constants.ApiConstants;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import retrofit.Callback;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;
import retrofit.http.Path;

/**
 * Created by sreejith on 10/27/2015.
 */
public interface IgcmApi {


    @FormUrlEncoded
    @POST("/{link}/" + ApiConstants.SendGCM)
    void sendMessage(@Field("encription_key") String value, @Path("link") String link, @Field("userid") String senderId, @Field("sender") String sender, @Field("message") String message, @Field("date") String date, Callback<JsonObject> response
    );

    @FormUrlEncoded
    @POST("/{link}/" + ApiConstants.RegisterGCM)
    void sendGcmToken(@Field("encription_key") String value, @Path("link") String link, @Field("userid") String userId, @Field("token") String token, Callback<JsonObject> response
    );
}
