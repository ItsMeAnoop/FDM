package com.field.datamatics.database;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by Jith on 10/8/2015.
 */
@Table(databaseName = AppDatabase.NAME)
public class SurveyMaster extends BaseModel {

    @Column
    @PrimaryKey
    public int Survey_Id;

    @Column
    public String surveynumber;

    @Column
    public String Question;

    @Column
    public String Option1;

    @Column
    public String Option2;

    @Column
    public String Option3;

    @Column
    public String Option4;

    @Column
    public int Type;

    @Column
    public String Remarks;

    @Column
    public String validFrom;

    @Column
    public String validTo;

    public transient int checkedItemPosition = 0;
}
