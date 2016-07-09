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
public class Logindetails extends BaseModel {

    @Column
    @PrimaryKey
    public int Log_Id;

    @Column
    @ForeignKey(references = {@ForeignKeyReference(columnName = "UserNumber", columnType = Integer.class, foreignColumnName = "UserNumber")}, saveForeignKeyModel = false)
    public User user;

    @Column
    public String Login_Time;

    @Column
    public String Geo_Cordinate_in;

    @Column
    public String Logout_Time;

    @Column
    public String Geo_Cordinate_out;

    @Column
    public int Type;

    @Column
    public String remarks;

    public int User_Id;
}
