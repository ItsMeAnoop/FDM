package com.field.datamatics.database;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.QueryModel;
import com.raizlabs.android.dbflow.structure.BaseQueryModel;

/**
 * Created by sreejith on 11/6/2015.
 */

@QueryModel(databaseName = AppDatabase.NAME)
public class CustomVisitedDetails extends BaseQueryModel {

    @Column
    public String clientFirstName;

    @Column
    public String clientLastName;

    @Column
    public String customerName;

    @Column
    public String visitedDate;

    @Column
    public String checkinTime;

    @Column
    public String checkOutTime;

    @Column
    public String sessionEndTime;

    @Column
    public String feedback;

    @Column
    public String notes;

    public String getName() {
        return clientFirstName + " " + clientLastName;
    }
}
