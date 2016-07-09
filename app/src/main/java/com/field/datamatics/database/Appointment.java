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
public class Appointment extends BaseModel {

    @Column
    @PrimaryKey
    public Integer Appointment_Id;

    @Column
    public String Appointment_Date;

    @Column
    @ForeignKey(references = {@ForeignKeyReference(columnName = "Route_Plan_Number", columnType = Integer.class, foreignColumnName = "Route_Plan_Number")}, saveForeignKeyModel = false)
    public RoutePlan routePlan;

    @Column
    @ForeignKey(references = {@ForeignKeyReference(columnName = "Client_Number", columnType = Integer.class, foreignColumnName = "Client_Number")}, saveForeignKeyModel = false)
    public Client client;

    @Column
    @ForeignKey(references = {@ForeignKeyReference(columnName = "Customer_Id", columnType = Integer.class, foreignColumnName = "Customer_Id")}, saveForeignKeyModel = false)
    public Customer customer;

    @Column
    public String Remarks;

}
