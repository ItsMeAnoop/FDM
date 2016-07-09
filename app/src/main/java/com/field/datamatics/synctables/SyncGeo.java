package com.field.datamatics.synctables;

import com.field.datamatics.database.AppDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by anoop on 10/11/15.
 */
@Table(databaseName = AppDatabase.NAME)
public class SyncGeo extends BaseModel {

    @Column
    @PrimaryKey(autoincrement = true)
    public int row_id;

    @Column
    public String geo_details;


}
