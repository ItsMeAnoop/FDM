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
public class RoutePlan extends BaseModel {

    @Column
    @PrimaryKey
    public int Route_Plan_Number;

    @Column
    public int routeno;

    @Column
    public String Date;

    @Column
    @ForeignKey(references = {@ForeignKeyReference(columnName = "Client_Number", columnType = Integer.class, foreignColumnName = "Client_Number")}, saveForeignKeyModel = false)
    public Client client;

    @Column
    @ForeignKey(references = {@ForeignKeyReference(columnName = "Customer_Id", columnType = Integer.class, foreignColumnName = "Customer_Id")}, saveForeignKeyModel = false)
    public Customer customer;

    @Column
    @ForeignKey(references = {@ForeignKeyReference(columnName = "UserNumber", columnType = Integer.class, foreignColumnName = "UserNumber")}, saveForeignKeyModel = false)
    public User user;

    @Column
    @ForeignKey(references = {@ForeignKeyReference(columnName = "Clientwork_Id", columnType = Integer.class, foreignColumnName = "Clientwork_Id")}, saveForeignKeyModel = false)
    public Client_work_cal client_work_cal;

    @Column
    public String Creation_Date;

    @Column
    @ForeignKey(references = {@ForeignKeyReference(columnName = "PerparedBy", columnType = Integer.class, foreignColumnName = "User_Id")}, saveForeignKeyModel = false)
    public User preparedBy;

    @Column
    public String Prepareduser;

    @Column
    public int AuthorizedBy;

    @Column
    public String authuser;

    @Column
    public String Auth_Date;

    @Column
    public int Visittype;

    @Column
    public String Remarks;
    @Column
    public int status;


    public int Customer_Id;
    public int User_Id;
    public int Clientwork_Id;
    public int PreparedBy;
    public int Client_Number;
}

