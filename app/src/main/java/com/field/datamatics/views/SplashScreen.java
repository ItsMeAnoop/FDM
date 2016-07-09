package com.field.datamatics.views;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.field.datamatics.R;
import com.field.datamatics.Services.ApiService;
import com.field.datamatics.apimodels.CommonSubmitJson;
import com.field.datamatics.apimodels.SendPendingRemarks;
import com.field.datamatics.constants.ApiConstants;
import com.field.datamatics.database.PendingRemarks;
import com.field.datamatics.interfaces.ApiCallbacks;
import com.field.datamatics.interfaces.OperationCallBacks;
import com.field.datamatics.synctables.SyncRemarks;
import com.field.datamatics.utils.CSVReader;
import com.field.datamatics.utils.PreferenceUtil;
import com.field.datamatics.utils.Utilities;
import com.google.gson.Gson;

import java.util.Calendar;
import java.util.HashMap;

/**
 * Created by anoop on 20/9/15.
 */
public class SplashScreen extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        if(PreferenceUtil.getIntsance().isLogin()){
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
        }else{
            //
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(getApplicationContext(),Login.class));
                    finish();
                }
            }, 1000);
        }

    }


}
