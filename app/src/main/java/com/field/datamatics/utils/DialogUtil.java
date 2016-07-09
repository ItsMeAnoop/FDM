package com.field.datamatics.utils;

import android.content.Context;
import android.view.View;

import com.field.datamatics.interfaces.DialogCallBacks;

import me.drakeet.materialdialog.MaterialDialog;

/**
 * Created by USER on 12/15/2015.
 */
public class DialogUtil {
    private MaterialDialog mMaterialDialog;
    private static DialogUtil instance=new DialogUtil();
    private DialogUtil(){}
    public static DialogUtil getInstance(){
        return instance;
    }
    public MaterialDialog getDialog(Context context,String title,String message, final DialogCallBacks callBacks){
        mMaterialDialog = new MaterialDialog(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        callBacks.onOk();
                        mMaterialDialog.dismiss();

                    }
                });
                /*.setNegativeButton("CANCEL", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mMaterialDialog.dismiss();

                    }
                });*/

        mMaterialDialog.show();
        return mMaterialDialog;
    }
}
