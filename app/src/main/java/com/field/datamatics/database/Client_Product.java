package com.field.datamatics.database;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.ForeignKeyReference;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by Jith on 10/7/2015.
 */
@Table(databaseName = AppDatabase.NAME)
public class Client_Product extends BaseModel {

    @Column
    @PrimaryKey
    public int Client_Productid_Id;

    @Column
    @ForeignKey(references = {@ForeignKeyReference(columnName = "Client_Number", columnType = Integer.class, foreignColumnName = "Client_Number")}, saveForeignKeyModel = false)
    public Client client;

    @Column
    @ForeignKey(references = {@ForeignKeyReference(columnName = "Customer_Id", columnType = Integer.class, foreignColumnName = "Customer_Id")}, saveForeignKeyModel = false)
    public Customer customer;

    @Column
    @ForeignKey(references = {@ForeignKeyReference(columnName = "Product_Number", columnType = String.class, foreignColumnName = "Product_Number")}, saveForeignKeyModel = false)
    public Product product;

    @Column
    public String ExpiryDate;

    @Column
    public String remarks;

    @Column
    public String status;

}
