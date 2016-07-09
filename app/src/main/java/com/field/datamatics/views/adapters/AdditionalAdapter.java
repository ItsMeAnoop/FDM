package com.field.datamatics.views.adapters;

import android.app.Activity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.field.datamatics.R;
import com.field.datamatics.database.Client_work_cal;
import com.field.datamatics.database.JoinClientRoutePlan;
import com.field.datamatics.database.PendingRemarks;
import com.field.datamatics.database.PendingRemarks$Table;
import com.field.datamatics.views.helper.ListDialogHelper;
import com.raizlabs.android.dbflow.sql.builder.Condition;
import com.raizlabs.android.dbflow.sql.language.Select;

import java.util.ArrayList;

/**
 * Created by Anoop on 12-06-2016.
 */
public class AdditionalAdapter extends RecyclerView.Adapter<AdditionalAdapter.ViewHolder> {
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

    public AdditionalAdapter(Activity activity, ArrayList<JoinClientRoutePlan> data) {
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
        holder.tvClienName.setText(client.Client_First_Name);
        holder.tvSpeciality.setText(client.CustomerName);
        holder.tvEmailPhone.setText(client.Client_Prefix);//indicate time of availability.
        holder.tv_location_head.setVisibility(View.GONE);
        try {
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
        } catch (Exception e) {
            e.printStackTrace();
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
