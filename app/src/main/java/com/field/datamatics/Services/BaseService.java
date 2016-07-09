package com.field.datamatics.Services;

import com.field.datamatics.constants.ApiConstants;

import retrofit.RestAdapter;

/**
 * Created by anoop on 15/10/15.
 */
public class BaseService {

    public RestAdapter getRestAdapter(){
        RestAdapter adapter=new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint(ApiConstants.BASE_URL)
                .build();
       return adapter;
    }

}
