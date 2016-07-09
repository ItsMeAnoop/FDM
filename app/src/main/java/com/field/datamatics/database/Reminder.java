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
public class Reminder extends BaseModel {

    @Column
    @PrimaryKey(autoincrement = true)
    public int Reminderid;

    @Column
    public int Pkey_id;

    @Column
    public String date;

    @Column
    public int status;

    @Column
    public String message;

    @Column
    public String Remarks;

    @Column
    @ForeignKey(references = {@ForeignKeyReference(columnName = "Client_Number", columnType = Integer.class, foreignColumnName = "Client_Number")}, saveForeignKeyModel = false)
    public Client client;

    @Column
    public String startTime;

    @Column
    public String endTime;

    @Column
    public String startCoordinates;

    @Column
    public String endCoordinates;

    @Column
    public int routePlanNumber;

}
