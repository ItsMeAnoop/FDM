package com.field.datamatics.Services;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;

import cz.msebera.android.httpclient.Header;

/**
 * Created by anoop on 14/11/15.
 * Service to upload medias to server
 */
public class UploadMediaService {
    private static UploadMediaService instance=new UploadMediaService();
    private UploadMediaService(){

    }
    public static UploadMediaService getInstance(){
        return instance;
    }
    public void uploadMedia(String path){
        File myFile = new File(path);
        RequestParams params = new RequestParams();
        try {
            params.put("file", myFile);
        } catch(FileNotFoundException e) {}

        BaseHttpClient.post("/webapp0710/AppMulitiPardData", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // If the response is JSONObject instead of expected JSONArray
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray timeline) {
                // Pull out the first event on the public timeline
                //JSONObject firstEvent = timeline.get(0);
                //String tweetText = firstEvent.getString("text");

                // Do something with the response
                //System.out.println(tweetText);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });

    }
}
