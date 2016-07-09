package com.field.datamatics.database;

import com.google.gson.annotations.Expose;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.data.Blob;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.io.Serializable;

/**
 * Created by Jith on 10/7/2015.
 */
@Table(databaseName = AppDatabase.NAME)
public class Product extends BaseModel implements Serializable{

    @Expose
    @Column
    @PrimaryKey
    public String Product_Number;


    @Expose
    @Column
    public String Product_Description;

    @Expose
    @Column
    public String Barcode;

    @Expose
    @Column
    public String Category;

    @Expose
    @Column
    public String Category_desc;

    @Expose
    @Column
    public String Subcategory;

    @Expose
    @Column
    public String Subcat_desc;

    @Expose
    @Column
    public String UOM;

    @Expose
    @Column
    public String Pack_Size;

    @Column
    public transient Blob Photo;

    @Expose
    @Column
    public String Narration;

    @Expose
    @Column
    public String Supplier_Name;

    @Expose
    @Column
    public String Supplier_desc;

    @Expose
    @Column
    public int Stock_Availability;

    @Expose
    @Column
    public String Usage;

    @Expose
    @Column
    public int On_PO;

    @Expose
    @Column
    public String Department;

    @Expose
    @Column
    public String Department_desc;

    @Expose
    @Column
    public String section;

    @Expose
    @Column
    public String Section_desc;

    @Expose
    @Column
    public String Family;

    @Expose
    @Column
    public String Family_desc;

    @Expose
    @Column
    public String Subfamily;

    @Expose
    @Column
    public String Subfamily_desc;

    @Expose
    @Column
    public boolean Status;

    @Expose
    @Column
    public String Remarks;
}
