package com.field.datamatics.Services.appservices;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.field.datamatics.views.DownloadProductListActivity;

/**
 * Created by Anoop on 10-07-2016.\
 * Service to set alarm to sycn
 */
public class MyAlaramService extends IntentService {
    public MyAlaramService(){
        super(MyAlaramService.class.getName());
    }
    @Override
    protected void onHandleIntent(Intent intent_) {
        //Toast.makeText(getApplicationContext(), "Service", Toast.LENGTH_SHORT).show();
        Bundle bundle = new Bundle();
        bundle.putString("syncType", "PULL");
        bundle.putString("isSyncAllData", "no");
        bundle.putString("from", "login");
        Intent intent = new Intent(getApplicationContext(), DownloadProductListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
