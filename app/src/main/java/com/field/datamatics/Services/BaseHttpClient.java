package com.field.datamatics.Services;

import com.field.datamatics.constants.ApiConstants;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * Created by USER on 11/15/2015.
 * Implement HttpClient object
 */
public class BaseHttpClient {
    private static String BASE_URL= ApiConstants.BASE_URL;
    private static AsyncHttpClient client=new AsyncHttpClient();
    public static void get(String url,RequestParams params,AsyncHttpResponseHandler responseHandler){
        client.get(getAbsolutePath(url),params,responseHandler);
    }
    public static void post(String url,RequestParams params,AsyncHttpResponseHandler responseHandler){
        client.post(getAbsolutePath(url),params,responseHandler);
    }
    private static String getAbsolutePath(String relativeUrl){
        return BASE_URL+relativeUrl;
    }

}
