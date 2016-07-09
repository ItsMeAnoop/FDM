package com.field.datamatics.interfaces;

import com.field.datamatics.database.CustomProductList;

import java.util.ArrayList;

/**
 * Created by Jithz on 12/20/2015.
 */
public interface ProductSelectedFinishedListener {
    void onProductSelectedFinished(ArrayList<CustomProductList> selectedProducts, int type);
}
