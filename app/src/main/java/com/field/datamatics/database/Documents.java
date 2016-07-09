package com.field.datamatics.database;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.ForeignKeyReference;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by Jith on 10/8/2015.
 */
@Table(databaseName = AppDatabase.NAME)
public class Documents extends BaseModel {

    @Column
    @PrimaryKey(autoincrement = true)
    public int Doc_Id;

    @Column
    public String visiteddate;

    @Column
    public String date;

    @Column
    @ForeignKey(references = {@ForeignKeyReference(columnName = "Route_Plan_Number", columnType = Integer.class, foreignColumnName = "Route_Plan_Number")}, saveForeignKeyModel = false)
    public RoutePlan routePlan;

    @Column
    public String Doc_Path;

    @Column
    public int Doc_Type;

    @Column
    public String startTime;

    @Column
    public String endTime;

    @Column
    public String startCoordinates;

    @Column
    public String endCoordinates;

    @Column
    public String Remarks;
}
