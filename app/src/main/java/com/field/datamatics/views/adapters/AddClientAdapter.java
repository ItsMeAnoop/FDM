package com.field.datamatics.views.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.field.datamatics.R;
import com.field.datamatics.models.MakeRoutePlan;
import com.field.datamatics.views.fragments.RoutePlan;

import java.util.ArrayList;

/**
 * Created by anoop on 12/10/15.
 */
public class AddClientAdapter extends RecyclerView.Adapter<AddClientAdapter.Holder>{
    private ArrayList<MakeRoutePlan> data;
    private Activity activity;
    private OnItemClickListener onItemClickListener;
    public interface OnItemClickListener{
        public void onItemClick(View view, int position);
    }
    public class Holder extends RecyclerView.ViewHolder implements View.OnClickListener{
        CheckBox chk_select;
        Holder(View view){
            super(view);
            view.setOnClickListener(this);
            chk_select= (CheckBox) view.findViewById(R.id.chk_select);

        }

        @Override
        public void onClick(View v) {
            if(onItemClickListener!=null){
                onItemClickListener.onItemClick(v,getPosition());
            }
        }
    }
    public AddClientAdapter(Activity activity,ArrayList<MakeRoutePlan> data){
        this.data=data;
        this.activity=activity;
    }
    public void setOnItemClickListener(final OnItemClickListener onItemClickListener){
        this.onItemClickListener=onItemClickListener;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.sublist_make_plan, viewGroup, false);
        Holder viewHolder = new Holder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final Holder holder, final int position) {
        if(data.get(position).isSelect()){
            holder.chk_select.setChecked(true);
        }
        else{
            holder.chk_select.setChecked(false);
        }
        holder.chk_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(data.get(position).isSelect()){
                    data.get(position).setIsSelect(false);
                    holder.chk_select.setChecked(false);
                }
                else{
                    data.get(position).setIsSelect(true);
                    holder.chk_select.setChecked(true);
                }
            }
        });
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

