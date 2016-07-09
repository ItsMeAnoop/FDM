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
public class Client_work_cal extends BaseModel {

    @Column
    @PrimaryKey(autoincrement = true)
    public int Clientwork_Id;

   // @Column
    //@ForeignKey(references = {@ForeignKeyReference(columnName = "Client_Number", columnType = Integer.class, foreignColumnName = "Client_Number")}, saveForeignKeyModel = false)
   // public Client client;


    //@Column
    //@ForeignKey(references = {@ForeignKeyReference(columnName = "Customer_Id", columnType = Integer.class, foreignColumnName = "Customer_Id")}, saveForeignKeyModel = false)
    //public Customer customer;

    @Column
    public String Clientfirstname;
    @Column
    public String Customerid;

    @Column
    public String Availabledays;
    @Column
    public String Clientno;

    @Column
    public String Fromtime;
    @Column
    public String Customername;

    @Column
    public String Totime;

    @Column
    public String Created_Date;

    @Column
    public String remarks;

}
