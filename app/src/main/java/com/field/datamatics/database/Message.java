package com.field.datamatics.database;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.ForeignKeyReference;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import retrofit.client.Client;

/**
 * Created by Jith on 10/8/2015.
 */
@Table(databaseName = AppDatabase.NAME)
public class Message extends BaseModel {

    @Column
    @PrimaryKey
    public int Message_Id;

    @Column
    @ForeignKey(references = {@ForeignKeyReference(columnName = "UserNumber", columnType = Integer.class, foreignColumnName = "UserNumber")}, saveForeignKeyModel = false)
    public User user;

    @Column
    public String Message_Date;

    @Column
    public int Pkey;

    @Column
    public int status;

    @Column
    @ForeignKey(references = {@ForeignKeyReference(columnName = "Client_Number", columnType = Integer.class, foreignColumnName = "Client_Number")}, saveForeignKeyModel = false)
    public com.field.datamatics.database.Client client;

    @Column
    @ForeignKey(references = {@ForeignKeyReference(columnName = "Customer_Id", columnType = Integer.class, foreignColumnName = "Customer_Id")}, saveForeignKeyModel = false)
    public Customer customer;

    @Column
    public String message;

    public int User_Id;
    public int Customer_Id;
    public int Client_Number;

}
