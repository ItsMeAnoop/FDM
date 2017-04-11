package com.field.datamatics.views.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.field.datamatics.R;
import com.field.datamatics.utils.Utilities;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

/**
 * Created by anoop on 11/10/15.
 * Score card adapter
 */
public class ScoreCardAdapter extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater = null;
    private HashMap<Integer, String> data;
    private int width;


    // references to our items
    public String[] days;

    static final int FIRST_DAY_OF_WEEK = 0; // Sunday = 0, Monday = 1
    private Calendar month;

    public ScoreCardAdapter(Activity activity, HashMap<Integer, String> data, int width, Calendar month) {
        // TODO Auto-generated constructor stub
        this.activity = activity;
        this.data = data;
        this.width = width;
        inflater = (LayoutInflater) activity.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        month.set(Calendar.DAY_OF_MONTH, 1);
        this.month = month;
        refreshDays();

    }


    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return days.length;
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
        CardView cv;
        TextView tv_date;
        TextView tv_score_value;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder = null;
        View rowView = convertView;
        int pos = position;
        if (rowView == null) {
            rowView = inflater.inflate(R.layout.sublist_score_card, null);
            holder = new Holder();
            holder.tv_date = (TextView) rowView.findViewById(R.id.tv_date);
            holder.tv_score_value = (TextView) rowView.findViewById(R.id.tv_score_value);
//            holder.tv_date.setMinimumWidth(width);
//            holder.tv_date.setMinimumHeight(width);
            rowView.setMinimumWidth(width);
            rowView.setMinimumHeight(width);
            rowView.setTag(holder);
        } else {
            holder = (Holder) rowView.getTag();
        }
        int day = 0;
        if (!days[position].equals("")) {
            day = Integer.parseInt(days[position]);
        } else {

        }
        holder.tv_date.setText(days[position]);
        String score = data.containsKey(day) ? data.get(day) : "";
        holder.tv_score_value.setText(score);
        return rowView;
    }

    public void refreshDays() {

        int lastDay = month.getActualMaximum(Calendar.DAY_OF_MONTH);
        int firstDay = (int) month.get(Calendar.DAY_OF_WEEK);

        // figure size of the array
        if (firstDay == 1) {
            days = new String[lastDay + (FIRST_DAY_OF_WEEK * 6)];
        } else {
            days = new String[lastDay + firstDay - (FIRST_DAY_OF_WEEK + 1)];
        }

        int j = FIRST_DAY_OF_WEEK;

        // populate empty days before first real day
        if (firstDay > 1) {
            for (j = 0; j < firstDay - FIRST_DAY_OF_WEEK; j++) {
                days[j] = "";
            }
        } else {
            for (j = 0; j < FIRST_DAY_OF_WEEK * 6; j++) {
                days[j] = "";
            }
            j = FIRST_DAY_OF_WEEK * 6 + 1; // sunday => 1, monday => 7
        }

        // populate days
        int dayNumber = 1;
        for (int i = j - 1; i < days.length; i++) {
            days[i] = "" + dayNumber;
            dayNumber++;
        }
    }
}

