package com.field.datamatics.views.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.field.datamatics.R;

import java.util.ArrayList;

/**
 * Created by anoop on 25/10/15.
 * client adapter
 */
public class AddClientAdapter_ extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater=null;
    private ArrayList<com.field.datamatics.models.MakeRoutePlan> data;
    private ArrayList<String>status=new ArrayList<String>();
    private int width;
    public AddClientAdapter_(Activity activity, ArrayList<com.field.datamatics.models.MakeRoutePlan> data,ArrayList<String>status) {
        // TODO Auto-generated constructor stub
        this.activity=activity;
        this.data=data;
        this.status=status;
        inflater = ( LayoutInflater )activity.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }
    @Override
    public int getCount() {
        return status.size();
    }

    @Override
    public Object getItem(int position) {
        return status.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder=null;
        View rowView=convertView;
        final int pos=position;
        if(rowView==null){
            rowView = inflater.inflate(R.layout.sublist_make_plan, null);
            holder=new Holder();
            holder.chk_select= (CheckBox) rowView.findViewById(R.id.chk_select);
            holder.tv_client_name= (TextView) rowView.findViewById(R.id.tv_client_name);
            holder.tv_customer_name= (TextView) rowView.findViewById(R.id.tv_customer_name);
            rowView.setTag(holder);
        }
        else{
            holder= (Holder) rowView.getTag();
        }
        holder.tv_client_name.setText(data.get(pos).getClient().Client_First_Name+" "+data.get(pos).getClient().Client_Last_Name);
        holder.tv_customer_name.setText(data.get(pos).getCustomer().Customer_Name+"\n"+data.get(pos).getCustomer().Address1+"\n"+data.get(pos).getCustomer().Street);



        if(status.get(position).equals("1")){
            holder.chk_select.setChecked(true);
        }
        else{
            holder.chk_select.setChecked(false);
        }
        holder.chk_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(status.get(pos).equals("1")){
                    status.remove(pos);
                    status.add(pos,"0");

                }
                else{
                    status.remove(pos);
                    status.add(pos,"1");
                }
                notifyDataSetChanged();

            }
        });

        return rowView;
    }
    public class Holder
    {
        CheckBox chk_select;
        TextView tv_client_name;
        TextView tv_customer_name;
    }
}
