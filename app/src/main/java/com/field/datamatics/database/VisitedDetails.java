package com.field.datamatics.database;

import android.support.annotation.Nullable;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.ForeignKeyReference;
import com.raizlabs.android.dbflow.annotation.NotNull;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.data.Blob;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by Jith on 10/8/2015.
 */

@Table(databaseName = AppDatabase.NAME)
public class VisitedDetails extends BaseModel {

    @Column
    @PrimaryKey(autoincrement = true)
    public int Visit_Id;

    @Column
    @ForeignKey(references = {@ForeignKeyReference(columnName = "Route_Plan_Number", columnType = Integer.class, foreignColumnName = "Route_Plan_Number")}, saveForeignKeyModel = false)
    public RoutePlan routePlan;

    @Column
    public String Visited_Date;

    @Column
    @ForeignKey(references = {@ForeignKeyReference(columnName = "Appointment_Id", columnType = Integer.class, foreignColumnName = "Appointment_Id")}, saveForeignKeyModel = false)
    public Appointment appointment;

    @Column
    public String checkintime;

    @Column
    public String checkouttime;

    @Column
    public String Sessionend;

    @Column
    public int status;

    @Column
    public String Feedback;

    @Column
    public String Geo_Cordinates_in;

    @Column
    public String Geo_Cordinates_out;

    @Column
    public String Geo_Cordinates_sessout;

    @Column
    public String notes;

    @Column
    @Nullable
    public Blob signature;

    @Column
    public String Remarks;

}
