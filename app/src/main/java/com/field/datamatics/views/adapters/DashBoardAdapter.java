package com.field.datamatics.views.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.field.datamatics.R;
import com.field.datamatics.models.DashboardModel;

import java.util.ArrayList;

/**
 * Created by anoop on 7/10/15.
 */
public class DashBoardAdapter extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater = null;
    private ArrayList<DashboardModel> data;
    private int width;

    public DashBoardAdapter(Activity activity, ArrayList<DashboardModel> data, int width) {
        // TODO Auto-generated constructor stub
        this.activity = activity;
        this.data = data;
        this.width = width;
        inflater = (LayoutInflater) activity.
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

    public class Holder {
        TextView tv_item_name;
        ImageView iv_icon;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder = null;
        View rowView = convertView;
        int pos = position;
        if (rowView == null) {
            rowView = inflater.inflate(R.layout.sublist_dash_board, null);
            holder = new Holder();
            holder.tv_item_name= (TextView) rowView.findViewById(R.id.tv_item_name);
            holder.iv_icon= (ImageView) rowView.findViewById(R.id.iv_icon);
            rowView.setTag(holder);
        } else {
            holder = (Holder) rowView.getTag();
        }
        holder.tv_item_name.setText(data.get(pos).getName());
        try{
            holder.iv_icon.setBackgroundResource(data.get(pos).getIcon());
        }
        catch (Exception e){

        }

        rowView.setMinimumWidth(width);
        rowView.setMinimumHeight(width);
        return rowView;
    }
}

