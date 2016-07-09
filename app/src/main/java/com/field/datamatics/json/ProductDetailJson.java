package com.field.datamatics.json;

import android.location.Location;

import com.field.datamatics.database.Product;
import com.field.datamatics.utils.Utilities;
import com.field.datamatics.views.fragments.ProductDetails;

import java.util.Calendar;

/**
 * Created by Jithz on 1/2/2016.
 */
public class ProductDetailJson {
    public Calendar activityDate;
    public String activityName;
    public String startCoordinates;

    public ProductDetailJson(Calendar activityDate, String activityName, String startCoordinates) {
        this.activityDate = activityDate;
        this.activityName = activityName;
        this.startCoordinates = startCoordinates;
    }
}
