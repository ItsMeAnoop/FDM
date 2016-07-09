package com.field.datamatics.database;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by Jith on 10/7/2015.
 */
@Table(databaseName = AppDatabase.NAME)
public class Customer extends BaseModel {

    public Customer() {
    }

    public Customer(int id) {
        Customer_Id = id;
    }

    @Column
    @PrimaryKey
    public int Customer_Id;

    @Column
    public String Customer_Name;

    @Column
    public String Customer_Group;

    @Column
    public String Address1;

    @Column
    public String Street;

    @Column
    public String Location;

    @Column
    public int PO;

    @Column
    public String Region;

    @Column
    public String Geo_Cordinates;

    @Column
    public String Country;

    @Column
    public boolean status;

    @Column
    public String Remarks;
}
