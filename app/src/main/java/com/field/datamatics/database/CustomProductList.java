package com.field.datamatics.database;

import com.field.datamatics.database.AppDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.QueryModel;
import com.raizlabs.android.dbflow.structure.BaseQueryModel;

/**
 * Created by Jithz on 12/20/2015.
 */
@QueryModel(databaseName = AppDatabase.NAME)
public class CustomProductList extends BaseQueryModel {

    @Column
    public String product;

    public boolean isSelected;
}
