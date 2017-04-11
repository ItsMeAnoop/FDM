package com.field.datamatics.views.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.field.datamatics.R;

import java.util.ArrayList;

/**
 * Created by anoop on 12/10/15.
 * Make route plan adapter
 */
public class MakeRoutePlanAdapter extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater=null;
    private ArrayList<String> data;
    private int width;
    public MakeRoutePlanAdapter(Activity activity, ArrayList<String> data,int width) {
        // TODO Auto-generated constructor stub
        this.activity=activity;
        this.data=data;
        this.width=width;
        inflater = ( LayoutInflater )activity.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public class Holder
    {
        CardView cv;
        TextView tv_date;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder=null;
        View rowView=convertView;
        int pos=position;
        if(rowView==null){
            rowView = inflater.inflate(R.layout.sublist_monthly_visit, null);
            holder=new Holder();
            holder.tv_date= (TextView) rowView.findViewById(R.id.tv_date);
            rowView.setTag(holder);
        }
        else{
            holder= (Holder) rowView.getTag();
        }
        holder.tv_date.setTextColor(Color.BLACK);

        holder.tv_date.setText(data.get(pos)+"");
        holder.tv_date.setMinimumWidth(width);
        holder.tv_date.setMinimumHeight(width);
        rowView.setMinimumWidth(width);
        rowView.setMinimumHeight(width);
        return rowView;
    }

}
