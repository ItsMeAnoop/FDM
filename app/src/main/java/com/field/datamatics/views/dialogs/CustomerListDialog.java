package com.field.datamatics.views.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.field.datamatics.R;
import com.field.datamatics.comparators.CustomerLocationComparator;
import com.field.datamatics.constants.Constants;
import com.field.datamatics.database.Client;
import com.field.datamatics.database.Client_Customer;
import com.field.datamatics.database.Client_Customer$Table;
import com.field.datamatics.database.Client_work_cal;
import com.field.datamatics.database.Client_work_cal$Table;
import com.field.datamatics.database.Customer;
import com.field.datamatics.database.Customer$Table;
import com.field.datamatics.database.JoinClientRoutePlan;
import com.field.datamatics.utils.AppControllerUtil;
import com.field.datamatics.utils.PreferenceUtil;
import com.field.datamatics.views.adapters.CustomerListAdapter;
import com.field.datamatics.views.fragments.VisitHomePage;
import com.google.gson.Gson;
import com.raizlabs.android.dbflow.sql.builder.Condition;
import com.raizlabs.android.dbflow.sql.language.Select;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Anoop on 22-11-2016.
 */
public class CustomerListDialog {
    private static CustomerListDialog instance;
    private Context context;
    private Dialog dialog;
    private ListView listView;
    private JoinClientRoutePlan joinClientRoutePlan;
    private GetCompleteData callBack;

    public static  CustomerListDialog getInstance(){
        if(instance==null)
            instance=new CustomerListDialog();
        return instance;
    }
    public void showDialog(final Context context, JoinClientRoutePlan joinClientRoutePlan, final GetCompleteData callBack){
        this.context=context;
        this.joinClientRoutePlan=joinClientRoutePlan;
        this.callBack=callBack;
        dialog = new Dialog(context, android.R.style.Theme_Translucent);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.dialog_list);
        /*WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        lp.dimAmount = 0.1f;
        dialog.getWindow().setAttributes(lp);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams
                .SOFT_INPUT_ADJUST_PAN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);*/
        listView= (ListView) dialog.findViewById(R.id.list);
        /*final ArrayList<Client_work_cal>client_work_cals= (ArrayList<Client_work_cal>) new Select().from(Client_work_cal.class)
                .where(Condition.column(Client_work_cal$Table.CLIENTNO).eq(joinClientRoutePlan.Client_Number))
                .queryList();*/
        /*for(int k=0;k<client_work_cals.size();k++){
            String firstRel=client_work_cals.get(k).Clientno+"_"+client_work_cals.get(k).Customerid;
            for(int j=k+1;j<client_work_cals.size();j++){
                String secondRel=client_work_cals.get(j).Clientno+"_"+client_work_cals.get(j).Customerid;
                if(firstRel.equalsIgnoreCase(secondRel)){
                    client_work_cals.remove(j--);
                }else{
                    break;
                }
            }
        }*/
        ArrayList<Client_Customer> client_customers=(ArrayList<Client_Customer>) new Select().from(Client_Customer.class)
                .where(Condition.column(Client_Customer$Table.CLIENT_CLIENT_NUMBER).eq(joinClientRoutePlan.Client_Number))
                .queryList();
        if(client_customers  == null || client_customers.size() == 0){
            Toast.makeText(context, "No Customers available for the clients.", Toast.LENGTH_LONG).show();
            return;
        }
        final ArrayList<Customer>customers=new ArrayList<>();
        for(int i=0;i<client_customers.size();i++){
            Customer c=getCustomer(client_customers.get(i).customer.Customer_Id);
            if(c !=null){
                customers.add(c);
            }
        }
        if(customers.size() == 0){
            Toast.makeText(context, "No Customers available for the clients.", Toast.LENGTH_LONG).show();
            return;
        }
        /*else if( customers.size() == 1){
            moveToVisitHomePage(customers.get(0));
        }*/
        else{
            boolean asc=true;
            Comparator<Customer> comp = asc ? new CustomerLocationComparator() : Collections.reverseOrder(new CustomerLocationComparator());
            Collections.sort(customers, comp);

            CustomerListAdapter adapter=new CustomerListAdapter(context,customers);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    moveToVisitHomePage(customers.get(position));
                    dialog.dismiss();
                }
            });
            dialog.show();
        }

    }
    public interface GetCompleteData{
        public void dataReceived(JoinClientRoutePlan data);
    }
    private void moveToVisitHomePage(Customer customer){
        joinClientRoutePlan.Client_Prefix= "";
        String location="";
        joinClientRoutePlan.CustomerName="";
        if(customer!=null&&customer.Location!=null){
            location=customer.Location;
            joinClientRoutePlan.CustomerName=customer.Customer_Name;
        }
        joinClientRoutePlan.Location=location;
        joinClientRoutePlan.RoutePlanDate=customer.Customer_Id+"";
        callBack.dataReceived(joinClientRoutePlan);
    }
    private Customer getCustomer(int customer_id){
        return   new Select().from(Customer.class)
                .where(Condition.column(Customer$Table.CUSTOMER_ID).eq(customer_id))
                .querySingle();
    }

}
