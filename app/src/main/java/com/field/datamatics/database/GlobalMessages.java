package com.field.datamatics.database;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by sreejith on 10/27/2015.
 */

@Table(databaseName = AppDatabase.NAME)
public class GlobalMessages extends BaseModel {

    @Column
    @PrimaryKey(autoincrement = true)
    public int id;

    @Column
    public String senderName;

    @Column
    public String message;

    @Column
    public String date;

    @Column
    public String sendDate;

    @Column
    public int status;

    @Column(defaultValue = "false")
    public Boolean isOutgoing;
}
