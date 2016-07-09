package com.field.datamatics.views.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.field.datamatics.R;

import java.util.ArrayList;

/**
 * Created by anoop on 27/9/15.
 */
public class RemainderAdapter extends RecyclerView.Adapter<RemainderAdapter.Holder>{
    private ArrayList<String> data;
    private Activity activity;
    public static class Holder extends RecyclerView.ViewHolder{
        Holder(View view){
            super(view);
        }

    }
    public RemainderAdapter(Activity activity,ArrayList<String> data){
        this.data=data;
        this.activity=activity;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.sublist_remainder, viewGroup, false);
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

