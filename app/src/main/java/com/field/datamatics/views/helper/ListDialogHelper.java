package com.field.datamatics.views.helper;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import java.util.ArrayList;

/**
 * Created by Anoop on 29-05-2016.
 */
public class ListDialogHelper {
    private static ListDialogHelper instance=new ListDialogHelper();
    private ListDialogHelper(){}
    public static ListDialogHelper getInstance(){
        return instance;
    }
    public void getAlertDialog(Context context, ArrayList<String> data){
        try {
            String[] items = new String[data.size()];
            items = data.toArray(items);
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            //builder.setTitle("Make your selection");
            builder.setItems(items, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int item) {
                   // callBack.getPosition(item);
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
