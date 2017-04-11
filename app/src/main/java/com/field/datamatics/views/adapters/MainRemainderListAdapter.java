package com.field.datamatics.views.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.field.datamatics.R;
import com.field.datamatics.database.Reminder;
import com.field.datamatics.models.Remainder;
import com.field.datamatics.utils.Utilities;

import java.util.ArrayList;

/**
 * Created by anoop on 11/10/15.
 * Reminder list adapter
 */
public class MainRemainderListAdapter extends RecyclerView.Adapter<MainRemainderListAdapter.Holder> {
    private ArrayList<Reminder> data;
    private Activity activity;

    public static class Holder extends RecyclerView.ViewHolder {
        RelativeLayout container_date;
        TextView tv_date;
        TextView tv_message;
        TextView tv_time;

        Holder(View view) {
            super(view);
            container_date = (RelativeLayout) view.findViewById(R.id.container_date);
            tv_date = (TextView) view.findViewById(R.id.tv_date);
            tv_message = (TextView) view.findViewById(R.id.message);
            tv_time = (TextView) view.findViewById(R.id.date);
        }

    }

    public MainRemainderListAdapter(Activity activity, ArrayList<Reminder> data) {
        this.data = data;
        this.activity = activity;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.sublist_remainder_list, viewGroup, false);
        Holder viewHolder = new Holder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        //holder.img_feed.setBackgroundResource(res[i]);
        Reminder reminder = data.get(position);
        holder.tv_message.setText(reminder.message);
        holder.tv_time.setText(Utilities.dateToString(reminder.date, "yyyy-MM-dd HH:mm", "hh:mm a"));
        String date = Utilities.dateToString(reminder.date, "yyyy-MM-dd HH:mm", "MMM dd, yyyy");
        if (position == 0) {
            holder.container_date.setVisibility(View.VISIBLE);
            holder.tv_date.setText(date);
        } else {
            String dateNext = Utilities.dateToString(data.get(position - 1).date, "yyyy-MM-dd HH:mm", "MMM dd, yyyy");
            if (!date.equals(dateNext)) {
                holder.container_date.setVisibility(View.VISIBLE);
                holder.tv_date.setText(date);
            } else {
                holder.container_date.setVisibility(View.GONE);
            }
        }
    }

    public void setData(ArrayList<Reminder> data) {
        this.data = new ArrayList<>(data);
        notifyDataSetChanged();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}


