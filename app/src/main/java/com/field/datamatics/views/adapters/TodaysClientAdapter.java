package com.field.datamatics.views.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.field.datamatics.R;
import com.field.datamatics.database.JoinClientRoutePlan;

import java.util.ArrayList;

/**
 * Created by Jith on 10/18/2015.
 * Today client list adapter
 */
public class TodaysClientAdapter extends RecyclerView.Adapter<TodaysClientAdapter.ViewHolder> {
    private ArrayList<JoinClientRoutePlan> data;
    private Activity activity;


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvClienName;
        TextView tvSpeciality;
        TextView tvEmailPhone;
        TextView tv_location_head;

        public ViewHolder(View v) {
            super(v);
            tvClienName = (TextView) v.findViewById(R.id.tv_client_name);
            tvSpeciality = (TextView) v.findViewById(R.id.tv_speciality);
            tvEmailPhone = (TextView) v.findViewById(R.id.tv_email_phno);
            tv_location_head= (TextView) v.findViewById(R.id.tv_location_head);
        }

    }

    public TodaysClientAdapter(Activity activity, ArrayList<JoinClientRoutePlan> data) {
        this.data = data;
        this.activity = activity;
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
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.sublist_clients, viewGroup, false);

        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        JoinClientRoutePlan client = data.get(position);
        holder.tvClienName.setText(client.Client_First_Name + " " + client.Client_Last_Name);
        holder.tvSpeciality.setText(client.Speciality);
        holder.tvEmailPhone.setText(client.Client_Email);
        holder.tv_location_head.setVisibility(View.GONE);
        if(position==0){
            holder.tv_location_head.setVisibility(View.VISIBLE);
            holder.tv_location_head.setText(client.Location);
        }
        else{
            if(!client.Location.equals(data.get(position-1).Location)){
                holder.tv_location_head.setVisibility(View.VISIBLE);
                holder.tv_location_head.setText(client.Location);
            }
        }

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
