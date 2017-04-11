package com.field.datamatics.views.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.field.datamatics.R;
import com.field.datamatics.database.AdditionalVisits;

/**
 * Created by Anoop on 10-08-2016.
 * Client detail dialog
 */
public class ClientDetailsDialog {
    private static ClientDetailsDialog instance;
    private Context context;
    private Dialog dialog;
    private TextView tv_name;
    private TextView tv_gender;
    private TextView tv_speciality;
    private TextView tv_market_class;
    private TextView tv_ste_class;
    private TextView tv_email;
    private TextView tv_phone;
    private TextView tv_mobile;
    private TextView tv_fax;
    private TextView tv_type;
    private TextView tv_nationality;
    private TextView tv_Customer;
    private TextView tvVisitedDate;
    private TextView tvCheckinTime;
    private TextView tvCheckOutTime;
    private TextView tvSessionEnd;
    private TextView tvFeedback;
    private TextView tvClientNumber;

    public static  ClientDetailsDialog getInstance(){
        if(instance==null)
            instance=new ClientDetailsDialog();
        return instance;
    }
    public void ClientDetailsDialog(final Context context, AdditionalVisits data){
        this.context=context;
        dialog = new Dialog(context, android.R.style.Theme_Translucent);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.dialog_client_details);
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        lp.dimAmount = 0.5f;
        dialog.getWindow().setAttributes(lp);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams
                .SOFT_INPUT_ADJUST_PAN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        tv_name = (TextView) dialog.findViewById(R.id.tv_client_name);
        tv_gender = (TextView) dialog.findViewById(R.id.tv_gender);
        tv_speciality = (TextView) dialog.findViewById(R.id.tv_speciality);
        tv_market_class = (TextView) dialog.findViewById(R.id.tv_market_class);
        tv_ste_class = (TextView) dialog.findViewById(R.id.tv_ste_class);
        tv_email = (TextView) dialog.findViewById(R.id.tv_email);
        tv_phone = (TextView) dialog.findViewById(R.id.tv_phno);
        tv_mobile = (TextView) dialog.findViewById(R.id.tv_mobile);
        tv_fax = (TextView) dialog.findViewById(R.id.tv_fax);
        tv_type = (TextView) dialog.findViewById(R.id.tv_type);
        tv_nationality = (TextView) dialog.findViewById(R.id.tv_nationality);
        tv_Customer = (TextView) dialog.findViewById(R.id.tv_customer_name);
        tvVisitedDate = (TextView) dialog.findViewById(R.id.tv_visited_date);
        tvCheckinTime = (TextView) dialog.findViewById(R.id.tv_check_in_time);
        tvCheckOutTime = (TextView) dialog.findViewById(R.id.tv_check_out_time);
        tvSessionEnd = (TextView) dialog.findViewById(R.id.tv_session_end);
        tvFeedback = (TextView) dialog.findViewById(R.id.tv_feedback);
        tvClientNumber = (TextView) dialog.findViewById(R.id.tv_client_number);
        tv_Customer.setText(data.getCustomer_name());
        tv_name.setText(data.getClientName());
        tv_gender.setText(data.getGender());
        tv_speciality.setText(data.getSpeciality());
        tv_market_class.setText(data.getMarketClass());
        tv_ste_class.setText(data.getSteClass());
        tv_email.setText(data.getEmail());
        tv_mobile.setText(data.getMob());
        tv_phone.setText(data.getPhone());
        tv_fax.setText(data.getFax());
        tv_type.setText(data.getTime_availability());
        tv_nationality.setText(data.getNationality());
        dialog.show();
    }
}
