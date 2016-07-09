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
public class SurveyDetails extends BaseModel {

    @Column
    @PrimaryKey(autoincrement = true)
    public int id;

    @Column
    @ForeignKey(references = {@ForeignKeyReference(columnName = "Survey_Id", columnType = Integer.class, foreignColumnName = "Survey_Id")}, saveForeignKeyModel = false)
    public SurveyMaster surveyMaster;

    @Column
    public String visiteddate;

    @Column
    @ForeignKey(references = {@ForeignKeyReference(columnName = "Route_Plan_Number", columnType = Integer.class, foreignColumnName = "Route_Plan_Number")}, saveForeignKeyModel = false)
    public RoutePlan routePlan;

    @Column
    public String Survey_Date;

    @Column
    public String Selected_Option;

    @Column
    public int Type;

    @Column
    public String Remarks;

    @Column
    public String startTime;

    @Column
    public String endTime;

    @Column
    public String startCoordinates;

    @Column
    public String endCoordinates;
}
