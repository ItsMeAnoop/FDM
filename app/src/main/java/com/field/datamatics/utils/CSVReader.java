package com.field.datamatics.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;

import com.field.datamatics.Services.SaveProductService;
import com.field.datamatics.database.Product;
import com.field.datamatics.interfaces.OperationCallBacks;
import com.raizlabs.android.dbflow.data.Blob;
import com.raizlabs.android.dbflow.runtime.TransactionManager;
import com.raizlabs.android.dbflow.runtime.transaction.process.ProcessModelInfo;
import com.raizlabs.android.dbflow.runtime.transaction.process.SaveModelTransaction;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * Created by USER on 11/14/2015.
 */
public class CSVReader {
    private static CSVReader instance = new CSVReader();
    private ArrayList<Product> data = new ArrayList<>();
    private OperationCallBacks callBacks;
    private InputStream inputStream = null;
    private OutputStream outputStream = null;
    private Activity activity;


    public static CSVReader getInstance() {
        return instance;
    }

    private String filename;

    public void addProductsFromCSV(final Activity activity, String file_content, String name, OperationCallBacks call) {
        Log.i("FDM","addProductsFromCSV");
        filename = name;
        callBacks = call;
        this.activity=activity;
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    BufferedReader br = null;
                    String line = "";
                    try {
                        //sdcard csv
                        String imagePath = Environment.getExternalStorageDirectory().toString() + "/"+filename;
                        //File myfile = new File("/sdcard/" + filename);
                        File myfile = new File(imagePath);
                        FileInputStream fln = new FileInputStream(myfile);
                        br = new BufferedReader(new InputStreamReader(fln, "UTF-16"));

                        line = br.readLine();
                        while ((line = br.readLine()) != null) {
                            String brln = line, brlns = "", res = "";
                            if (brln.contains("\"")) {
                                String[] vals = brln.split("\"");
                                int counter = 0;
                                for (int i = 0; i < brln.length(); i++) {
                                    if (brln.charAt(i) == '\"') {
                                        counter++;
                                    }
                                }
                                for (int i = 1; i <= counter + 1; i++) {
                                    if ((i + 1) % 2 == 0)
                                        brlns = vals[i - 1];
                                    else
                                        brlns = vals[i - 1].replace(",", "~");
                                    res += brlns;
                                }

                            } else
                                res = line;
                            String[] val;
                            res = res.concat(",0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0");
                            if (!res.trim().equals(",0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0")) {
                                val = res.trim().split(",");
                                System.out.println(line.trim());
                                if ( !val[1].equals(""))
                                    addToList(
                                            val[0].contains("~") ? val[0].replace("~", ",")
                                                    : val[0],
                                            val[1].contains("~") ? val[1].replace("~", ",")
                                                    : val[1],
                                            val[2].contains("~") ? val[2].replace("~", ",")
                                                    : val[2],
                                            val[3].contains("~") ? val[3].replace("~", ",")
                                                    : val[3],
                                            val[4].contains("~") ? val[4].replace("~", ",")
                                                    : val[4],
                                            val[5].contains("~") ? val[5].replace("~", ",")
                                                    : val[5],
                                            val[6].contains("~") ? val[6].replace("~", ",")
                                                    : val[6],
                                            val[7].contains("~") ? val[7].replace("~", ",")
                                                    : val[7],
                                            val[8].contains("~") ? val[8].replace("~", ",")
                                                    : val[8],
                                            val[9].contains("~") ? val[9].replace("~", ",")
                                                    : val[9],
                                            val[10].contains("~") ? val[10].replace("~",
                                                    ",") : val[10],
                                            val[11].contains("~") ? val[11].replace("~",
                                                    ",") : val[11],
                                            val[12].contains("~") ? val[12].replace("~",
                                                    ",") : val[12],
                                            val[13].contains("~") ? val[13].replace("~",
                                                    ",") : val[13],
                                            val[14].contains("~") ? val[14].replace("~",
                                                    ",") : val[14],
                                            val[15].contains("~") ? val[15].replace("~",
                                                    ",") : val[15],
                                            val[16].contains("~") ? val[16].replace("~",
                                                    ",") : val[16],
                                            val[17].contains("~") ? val[17].replace("~",
                                                    ",") : val[17],
                                            val[18].contains("~") ? val[18].replace("~",
                                                    ",") : val[18],
                                            val[19].contains("~") ? val[19].replace("~",
                                                    ",") : val[19],
                                            val[20].contains("~") ? val[20].replace("~",
                                                    ",") : val[20],
                                            val[21].contains("~") ? val[21].replace("~",
                                                    ",") : val[21], "", "", true);
                            }
                        }
                        br.close();

                    } catch (IOException e) {
                        Log.d("FAIL", e.getMessage().toString());
                        System.out.println("errr read....." + e);
                        e.printStackTrace();
                        callBacks.onError();
                    }


                } catch (Exception e) {
                    callBacks.onError();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
               /* Bundle bundle=new Bundle();
                bundle.putSerializable("PRODUCT",data);
                Intent intent=new Intent(activity.getApplicationContext(), SaveProductService.class);
                intent.putExtras(bundle);
                activity.startService(intent);*/
                callBacks.onSuccess();
            }
        }.execute();
    }
    private String getValue(String value){
        return  value.contains("~") ?value.replace("~",
                ",") : value;

    }

    private void addToList(String productno, String description, String barcode, String category, String catdescr, String subcategory, String subcatdesc,
                           String uom, String packsize, String narration, String supplier, String suplierdesc, String departmant, String deptdesc, String section, String
                                   sectdescr, String family, String familydesc, String subfamily, String subfamilydesc, String po, String stockavailabilty, String usage, String remarks, Boolean status) {
        try {
            Product product = new Product();
            //product.Product_Number = data.size()+1;
            product.Product_Number = productno;
            product.Product_Description = description.contains("+") ? description.substring(0, description.lastIndexOf("+")) : description;
            product.Barcode = barcode;
            product.Category = category;
            product.Category_desc = catdescr;
            product.Subcategory = subcategory;
            product.Subcat_desc = subcatdesc;
            product.UOM = uom;
            product.Pack_Size = packsize;
            byte[] b = {0};
            product.Photo = new Blob(b);
            product.Narration = narration.contains("+")?narration.substring(0,narration.lastIndexOf("+")):narration;
            product.Supplier_Name = supplier;
            product.Supplier_desc = suplierdesc;
            product.Stock_Availability = stockavailabilty.equals("") || stockavailabilty.contains("[a-zA-Z]+") ? 0 : Integer.parseInt(stockavailabilty);
            //product.Usage = description.contains("+") ? description.substring(description.lastIndexOf("+") + 1, description.length()) : "";
            product.Usage=narration.contains("+")?narration.substring(narration.lastIndexOf("+")+1,narration.length()):"";
            try {
                product.On_PO = (po.equals("")) ? 0 : Integer.parseInt(po);
            } catch (Exception e) {

            }
            product.Department = departmant;
            product.Department_desc = deptdesc;
            product.section = section;
            product.Section_desc = sectdescr;
            product.Family = family;
            product.Family_desc = familydesc;
            product.Subfamily = subfamily;
            product.Subfamily_desc = subfamilydesc;
            product.Status = status;
            product.Remarks = remarks;
            product.save();
            //data.add(product);
        } catch (Exception e) {
            Log.e("Add-E", e.getMessage().toString());

        }

    }
}
