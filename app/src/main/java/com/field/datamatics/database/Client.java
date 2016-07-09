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
public class Client extends BaseModel {

    public Client() {
    }

    public Client(int id) {
        Client_Number = id;
    }


    @Column
    @PrimaryKey
    public int Client_Number;

    @Column
    public long Address_Number_JDE;

    @Column
    public String Client_Prefix;

    @Column
    public String Client_First_Name;

    @Column
    public String Client_Last_Name;

    @Column
    public String Client_Gender;

    @Column
    public String Client_Email;

    @Column
    public long Client_Phone;

    @Column
    public long Client_Mobile;

    @Column
    public String Client_Fax;

    @Column
    public String Speciality;

    @Column
    public String Marketclass;

    @Column
    public String STEclass;

    @Column
    public String Type;

    @Column
    public String Nationality;

    @Column
    public Boolean Status;

    @Column
    public Boolean Visit;

    @Column
    public String Account_Manager;

    @Column
    public String Remarks;

    public String getName() {
        return Client_First_Name + " " + Client_Last_Name;
    }

}
