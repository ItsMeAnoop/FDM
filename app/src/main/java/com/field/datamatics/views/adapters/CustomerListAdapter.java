package com.field.datamatics.views.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.field.datamatics.R;

import java.util.ArrayList;

/**
 * Created by anoop on 14/10/15.
 */
public class CustomerListAdapter extends RecyclerView.Adapter<CustomerListAdapter.Holder>{
    private ArrayList<String> data;
    private Activity activity;
    private OnItemClickListener onItemClickListener;
    public interface OnItemClickListener{
        public void onItemClick(View view, int position);
    }
    public class Holder extends RecyclerView.ViewHolder implements View.OnClickListener{
        Holder(View view){
            super(view);
            view.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            if(onItemClickListener!=null){
                onItemClickListener.onItemClick(v,getPosition());
            }
        }
    }
    public CustomerListAdapter(Activity activity,ArrayList<String> data){
        this.data=data;
        this.activity=activity;
    }
    public void setOnItemClickListener(final OnItemClickListener onItemClickListener){
        this.onItemClickListener=onItemClickListener;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.sublist_customer_list, viewGroup, false);
        Holder viewHolder = new Holder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        //holder.img_feed.setBackgroundResource(res[i]);
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

