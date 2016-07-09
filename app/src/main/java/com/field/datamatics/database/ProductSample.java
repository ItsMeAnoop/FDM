package com.field.datamatics.database;

import com.google.gson.annotations.Expose;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.ForeignKeyReference;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by Jith on 10/21/2015.
 */

@Table(databaseName = AppDatabase.NAME)
public class ProductSample extends BaseModel {

    @Column
    @PrimaryKey(autoincrement = true)
    public int id;

    @Column
    @ForeignKey(references = {@ForeignKeyReference(columnName = "Product_Number", columnType = String.class, foreignColumnName = "Product_Number")}, saveForeignKeyModel = false)
    public Product product;

    @Column
    public String visited_date;

    @Column
    @ForeignKey(references = {@ForeignKeyReference(columnName = "Route_Plan_Number", columnType = Integer.class, foreignColumnName = "Route_Plan_Number")}, saveForeignKeyModel = false)
    public RoutePlan routePlan;

    @Column
    public String date;

    @Expose
    @Column
    public int quantity;

    @Expose
    @Column
    public String narration;

    @Column
    public String startTime;

    @Column
    public String endTime;

    @Column
    public String startCoordinates;

    @Column
    public String endCoordinates;

    /* this column is only for sorting purpose */
    @Column
    public String category;
}
