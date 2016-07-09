package com.field.datamatics.database;

import com.raizlabs.android.dbflow.annotation.Database;

/**
 * Created by Jith on 10/7/2015.
 */
@Database(name = AppDatabase.NAME, version = AppDatabase.VERSION, foreignKeysSupported = false)
public class AppDatabase {

    public static final String NAME = "FieldDatamatics";
    public static final int VERSION = 2;
}
