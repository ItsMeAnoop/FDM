package com.field.datamatics.views.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.field.datamatics.R;

import java.io.File;

/**
 * Created by Anoop on 12-06-2016.
 */
public class AddToPendingDialog {
    private static AddToPendingDialog instance;
    private Context context;
    private Dialog dialog;
    private Button btnRemark;
    private Button btnRem;
    private Button btnAddTopend;

    public static  AddToPendingDialog getInstance(){
        if(instance==null)
            instance=new AddToPendingDialog();
        return instance;
    }
    public void showDialog(final Context context, final boolean isRem, final boolean isRemainder, final GetPositionCallBack callBack){
        this.context=context;
        dialog = new Dialog(context, android.R.style.Theme_Translucent);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.dialog_pending_decision);
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        lp.dimAmount = 0.5f;
        dialog.getWindow().setAttributes(lp);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams
                .SOFT_INPUT_ADJUST_PAN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        btnRemark= (Button) dialog.findViewById(R.id.btnRemark);
        btnRem= (Button) dialog.findViewById(R.id.btnRem);
        btnAddTopend= (Button) dialog.findViewById(R.id.btnAddTopend);
        btnRemark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isRem)
                    callBack.getPosition(0);
                else
                    Toast.makeText(context,"Remarks already added",Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
        });
        btnRem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isRemainder)
                    callBack.getPosition(1);
                else
                    Toast.makeText(context,"Remainder already added",Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
        });
        btnAddTopend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBack.getPosition(2);
                dialog.dismiss();
            }
        });
        dialog.show();
    }
    public interface GetPositionCallBack{
        public void getPosition(int position);
    }

}
