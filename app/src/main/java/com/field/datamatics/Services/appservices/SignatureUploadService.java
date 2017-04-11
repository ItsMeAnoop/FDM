package com.field.datamatics.Services.appservices;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.field.datamatics.constants.ApiConstants;
import com.field.datamatics.utils.AppControllerUtil;
import com.field.datamatics.utils.NetworkStatusUtil;
import com.field.datamatics.utils.Utilities;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Calendar;

/**
 * Created by USER on 11/29/2015.
 * Service to upload signature to server
 */
public class SignatureUploadService extends Service{
    public static String signature_path;
    public static String routplanno;
    /*public static String visiteddate;
    public static String date;*/

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (NetworkStatusUtil.getInstance().isNetworkAvailable() == true)
            upload();
        return super.onStartCommand(intent, flags, startId);
    }

    private void upload() {
        //UploadMediaService.getInstance().uploadMedia(file_path);
        try {
            String imagePath = signature_path;
            AsyncHttpClient client = new AsyncHttpClient();
            File myFile = new File(imagePath);
            RequestParams params = new RequestParams();
            try {
                params.put("file", new FileInputStream(myFile));
                params.put("routplanno", routplanno);
                params.put("visiteddate", Utilities.dateToString(Calendar.getInstance(), "yyyy-MM-dd"));
                params.put("date",Utilities.dateToString(Calendar.getInstance(), "yyyy-MM-dd"));

                client.post(ApiConstants.BASE_URL + ApiConstants.url + ApiConstants.AppSaveSignature, params, new AsyncHttpResponseHandler() {

                    @Override
                    public void onStart() {
                        super.onStart();
                    }

                    @Override
                    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                        Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
                        Toast.makeText(getApplicationContext(), "failure...!", Toast.LENGTH_LONG).show();

                    }

                });
            } catch (FileNotFoundException e) {
                Log.d("MyApp", "File not found!!!" + imagePath);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
