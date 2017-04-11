package com.field.datamatics.views.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.field.datamatics.R;
import com.field.datamatics.database.Customer;

import java.util.ArrayList;

/**
 * Created by Anoop on 26-11-2016.
 * Customer list adapter
 */
public class CustomerListAdapter extends BaseAdapter {
    Context activity;
    ArrayList<Customer>data;
    LayoutInflater inflater;
    public CustomerListAdapter(Context activity,ArrayList<Customer>data){
        this.activity=activity;
        this.data=data;
        inflater= (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder=null;
        if(convertView == null){
            holder=new Holder();
            convertView=inflater.inflate(R.layout.row_customer,parent,false);
            holder.tvLocation= (TextView) convertView.findViewById(R.id.tvLocation);
            holder.tv_customer_name= (TextView) convertView.findViewById(R.id.tv_customer_name);
            holder.tv_group= (TextView) convertView.findViewById(R.id.tv_group);
            holder.tv_address= (TextView) convertView.findViewById(R.id.tv_address);
            convertView.setTag(holder);
        }
        else{
            holder= (Holder) convertView.getTag();
        }

        holder.tvLocation.setVisibility(View.GONE);
        if(position==0){
            holder.tvLocation.setVisibility(View.VISIBLE);
            holder.tvLocation.setText(data.get(position).Location);
        }
        else{
            if(!data.get(position).Location.equals(data.get(position-1).Location)){
                holder.tvLocation.setVisibility(View.VISIBLE);
                holder.tvLocation.setText(data.get(position).Location);
            }
        }
        holder.tv_customer_name.setText(data.get(position).Customer_Name);
        holder.tv_group.setText(data.get(position).Customer_Group);
        holder.tv_group.setVisibility(View.GONE);
        String address="";
        if(data.get(position).Address1!= null && !data.get(position).Address1.trim().equals("")){
            address=data.get(position).Address1;
        }
        if(data.get(position).Street!= null && !data.get(position).Street.trim().equals("")){
            address= address+", "+data.get(position).Street;
        }
        if(data.get(position).Region!= null && !data.get(position).Region.trim().equals("")){
            address= address+", "+data.get(position).Region;
        }

        holder.tv_address.setText(address);
        return convertView;
    }
    public static class Holder{
        TextView tvLocation;
        TextView tv_customer_name;
        TextView tv_group;
        TextView tv_address;
    }
}
