package com.field.datamatics.database;

import com.google.android.gms.maps.model.LatLng;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.QueryModel;
import com.raizlabs.android.dbflow.structure.BaseQueryModel;

/**
 * Created by Jith on 10/19/2015.
 */
@QueryModel(databaseName = AppDatabase.NAME)
public class CustomRoutePlan extends BaseQueryModel {

    @Column
    public int Customer_Id;

    @Column
    public int Client_Id;

    @Column
    public String Customer_Name;

    @Column
    public String Geo_Cordinates;

    @Column
    public int Visittype;

    @Column
    public String Client_First_Name;

    @Column
    public String Client_Last_Name;

    @Column
    public long Client_Phone;

    @Column
    public long Client_Mobile;

    @Column
    public String Marketclass;

    @Column
    public String STEclass;

    @Column
    public int RoutePlanNumber;

    @Column
    public String RoutePlanDate;

    public LatLng latLng;

    @Override
    public String toString() {
        return Client_First_Name + " " + Client_Last_Name;
    }
}
