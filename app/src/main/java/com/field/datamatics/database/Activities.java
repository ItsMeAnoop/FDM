package com.field.datamatics.database;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.ForeignKeyReference;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by user on 10/16/2015.
 */

@Table(databaseName = AppDatabase.NAME)
public class Activities extends BaseModel {

    @Column
    @PrimaryKey(autoincrement = true)
    public int activity_id;

    @Column
    public String activitydate;

    @Column
    public String visitdate;

    @Column
    @ForeignKey(references = {@ForeignKeyReference(columnName = "Route_Plan_Number", columnType = Integer.class, foreignColumnName = "Route_Plan_Number")}, saveForeignKeyModel = false)
    public RoutePlan routePlan;

    @Column
    public Integer Activity;

    @Column
    public String activityname;

    @Column
    public String starttime;

    @Column
    public String endtime;

    @Column
    public String startCoOrdinates;

    @Column
    public String endCoOrdinates;

    @Column
    public String Remarks;

}
