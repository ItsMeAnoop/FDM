package com.field.datamatics.Services;

import com.field.datamatics.constants.ApiConstants;
import com.squareup.okhttp.OkHttpClient;

import java.util.concurrent.TimeUnit;

import retrofit.RestAdapter;
import retrofit.client.OkClient;

/**
 * Created by anoop on 15/10/15.
 * Retrofit base implementation
 */
public class BaseService {

    public RestAdapter getRestAdapter(){
        OkHttpClient httpClient = new OkHttpClient();
        httpClient.setReadTimeout(180, TimeUnit.SECONDS);
        httpClient.setConnectTimeout(180, TimeUnit.SECONDS);

        RestAdapter adapter=new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint(ApiConstants.BASE_URL)
                .setClient(new OkClient(httpClient))
                .build();
       return adapter;
    }

}
