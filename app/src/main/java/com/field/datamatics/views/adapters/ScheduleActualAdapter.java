package com.field.datamatics.views.adapters;

import android.app.Activity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.field.datamatics.R;
import com.field.datamatics.database.JoinClientRoutePlan;

import java.util.ArrayList;

/**
 * Created by Jith on 10/18/2015.
 */
public class ScheduleActualAdapter extends RecyclerView.Adapter<ScheduleActualAdapter.ViewHolder> {
    private ArrayList<JoinClientRoutePlan> data;
    private Activity activity;
    private int filterType;

    public ScheduleActualAdapter(int filterType) {
        this.filterType = filterType;
        data = new ArrayList<>();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvClienName;
        TextView tvSpeciality;
        TextView tvEmailPhone;
        TextView header;
        CardView cvHeader;

        public ViewHolder(View v) {
            super(v);
            tvClienName = (TextView) v.findViewById(R.id.tv_client_name);
            tvSpeciality = (TextView) v.findViewById(R.id.tv_speciality);
            tvEmailPhone = (TextView) v.findViewById(R.id.tv_email_phno);
            header = (TextView) v.findViewById(R.id.header);
            cvHeader = (CardView) v.findViewById(R.id.cv_header);
        }

    }

    public void setData(ArrayList<JoinClientRoutePlan> data) {
        this.data = new ArrayList<>(data);
        notifyDataSetChanged();
    }

    public JoinClientRoutePlan getItem(int position) {
        return data.get(position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_schedule_actual, viewGroup, false);

        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    String previous = null;

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        JoinClientRoutePlan client = data.get(position);

        if (filterType == 0) {
            if (position == 0) {
                holder.cvHeader.setVisibility(View.VISIBLE);
                holder.header.setText(client.CustomerName);
            } else {
                String previous = data.get(position - 1).CustomerName;
                if (!previous.equals(client.CustomerName)) {
                    holder.cvHeader.setVisibility(View.VISIBLE);
                    holder.header.setText(client.CustomerName);
                } else {
                    holder.cvHeader.setVisibility(View.GONE);
                }
            }
        } else {
            if (position == 0) {
                holder.cvHeader.setVisibility(View.VISIBLE);
                holder.header.setText(client.Region);
            } else {
                String previous = data.get(position - 1).Region;
                if (!previous.equals(client.Region)) {
                    holder.cvHeader.setVisibility(View.VISIBLE);
                    holder.header.setText(client.Region);
                } else {
                    holder.cvHeader.setVisibility(View.GONE);
                }
            }
        }

        holder.tvClienName.setText(client.Client_First_Name + " " + client.Client_Last_Name);
        holder.tvSpeciality.setText(client.Speciality);
        holder.tvEmailPhone.setText(client.Client_Email);
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
