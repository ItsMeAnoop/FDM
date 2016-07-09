package com.field.datamatics.interfaces;

import com.field.datamatics.constants.ApiConstants;
import com.google.gson.JsonObject;

import retrofit.Callback;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;
import retrofit.http.Path;

/**
 * Created by Jithz on 1/18/2016.
 */
public interface IrequestPassword {

    @FormUrlEncoded
    @POST("/{link}/" + ApiConstants.RequestPassword)
    void sendPassword(@Field("encription_key") String value, @Path("link") String link, @Field("username") String userName, @Field("usercode") String employeeCode, Callback<JsonObject> response
    );
}
