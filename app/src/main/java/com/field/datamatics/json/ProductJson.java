package com.field.datamatics.json;

import com.field.datamatics.database.AppDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.data.Blob;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by Jith on 10/7/2015.
 */
public class ProductJson {


    public String Product_Number;


    public String Product_Description;


    public String Barcode;


    public String Category;


    public String Category_desc;


    public String Subcategory;


    public String Subcat_desc;


    public String UOM;


    public String Pack_Size;


    public Blob Photo;


    public String Narration;


    public String Supplier_Name;


    public String Supplier_desc;


    public int Stock_Availability;


    public String Usage;


    public int On_PO;


    public String Department;


    public String Department_desc;


    public String section;


    public String Section_desc;


    public String Family;


    public String Family_desc;


    public String Subfamily;


    public String Subfamily_desc;


    public boolean Status;


    public String Remarks;
}
