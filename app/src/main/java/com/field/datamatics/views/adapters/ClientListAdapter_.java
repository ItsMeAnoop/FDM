package com.field.datamatics.views.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.field.datamatics.R;
import com.field.datamatics.database.RoutePlan;

import java.util.ArrayList;

/**
 * Created by anoop on 28/10/15.
 * Client list adapter
 */
public class ClientListAdapter_ extends RecyclerView.Adapter<ClientListAdapter_.ViewHolder> {
    private ArrayList<RoutePlan> data;
    private Activity activity;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tv_client_name;
        TextView tv_customer_name;
        CheckBox chk_select;


        public ViewHolder(View v) {
            super(v);
            tv_client_name = (TextView) v.findViewById(R.id.tv_client_name);
            tv_customer_name = (TextView) v.findViewById(R.id.tv_customer_name);
            chk_select= (CheckBox) v.findViewById(R.id.chk_select);
            v.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(v, getPosition());
            }
        }
    }

    public void setOnItemClickListener(final OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public ClientListAdapter_(Activity activity, ArrayList<RoutePlan> data) {
        this.data = data;
        this.activity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.sublist_make_plan, viewGroup, false);

        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //holder.img_feed.setBackgroundResource(res[i]);
        holder.chk_select.setVisibility(View.GONE);
        String client_name="";
        String customer_name="";
        try {
            if(data.get(position).client.Client_First_Name!=null)
                client_name=data.get(position).client.Client_First_Name;
            if(data.get(position).client.Client_Last_Name!=null)
                client_name=client_name+" "+data.get(position).client.Client_Last_Name;
        } catch (Exception e) {
            e.printStackTrace();
        }
        holder.tv_client_name.setText(client_name);

        try {
            if(data.get(position).customer.Customer_Name!=null)
                customer_name=customer_name+" "+data.get(position).customer.Customer_Name;
            if(data.get(position).customer.Address1!=null)
                customer_name=customer_name+"\n"+data.get(position).customer.Address1;
            if(data.get(position).customer.Street!=null)
                customer_name=customer_name+"\n"+data.get(position).customer.Street;
        } catch (Exception e) {
            e.printStackTrace();
        }
        holder.tv_customer_name.setText(customer_name);

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

