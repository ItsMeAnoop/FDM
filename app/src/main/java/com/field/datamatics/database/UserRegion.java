package com.field.datamatics.database;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.ForeignKeyAction;
import com.raizlabs.android.dbflow.annotation.ForeignKeyReference;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by Jith on 10/7/2015.
 */

@Table(databaseName = AppDatabase.NAME)
public class UserRegion extends BaseModel{

    @Column
    @PrimaryKey
    public int Userregion_Id;

    @Column
    @ForeignKey(references = {@ForeignKeyReference(columnName = "UserNumber",columnType = Integer.class,foreignColumnName = "UserNumber")},onDelete = ForeignKeyAction.SET_NULL,saveForeignKeyModel = false)
    public User user;

    @Column
    public String Region;

    @Column
    public String Reporting_Manager;

    @Column
    public String Date_From;

    @Column
    public String Date_To;

    @Column
    public String Remarks;
}
