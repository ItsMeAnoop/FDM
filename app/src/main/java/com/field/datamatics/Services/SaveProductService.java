package com.field.datamatics.Services;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;

import com.field.datamatics.database.Product;

import java.util.ArrayList;

/**
 * Created by Anoop on 06-07-2016.
 * Service to save product list in DB
 */
public class SaveProductService extends IntentService {
    public SaveProductService(){
        super(SaveProductService.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle bundle=intent.getExtras();
        ArrayList<Product>products= (ArrayList<Product>) bundle.getSerializable("PRODUCT");
        for(int i=0;i<products.size();i++){
            products.get(i).save();
        }
    }
}
