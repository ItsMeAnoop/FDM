package com.field.datamatics.synctables;

import com.field.datamatics.database.AppDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by Anoop on 19-06-2016.
 */
@Table(databaseName = AppDatabase.NAME)
public class SyncRemarks extends BaseModel {

    @Column
    @PrimaryKey(autoincrement = true)
    public int row_id;

    @Column
    public String remarks;


}
