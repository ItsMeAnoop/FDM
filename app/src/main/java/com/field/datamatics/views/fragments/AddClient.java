package com.field.datamatics.views.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.field.datamatics.R;
import com.field.datamatics.models.*;
import com.field.datamatics.views.adapters.AddClientAdapter_;
import com.field.datamatics.views.adapters.ClientListAdapter;

import java.util.ArrayList;

/**
 * Created by anoop on 12/10/15.
 * Add client screen logic
 */
public class AddClient extends BaseFragment {
    //private RecyclerView rv_list;
    private ListView list;
    private TextView tv_add;
    private ArrayList<com.field.datamatics.models.MakeRoutePlan> data = new ArrayList<com.field.datamatics.models.MakeRoutePlan>();
    private ArrayList<String> data_status = new ArrayList<String>();
    private AddClientAdapter_ adapter;
    public static int position_;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_client, container, false);
        addFragmentTitle("AddClient");
        list = (ListView) view.findViewById(R.id.list);
        tv_add = (TextView) view.findViewById(R.id.tv_add);
        listSetUp();
        registerEvents();
        return view;
    }

    private void listSetUp() {
        data.addAll(MakeRoutePlan.route_plan.get(position_));
        data_status.addAll(MakeRoutePlan.data_status.get(position_));
        adapter = new AddClientAdapter_(getActivity(), data, data_status);
        list.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    private void registerEvents() {
        tv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MakeRoutePlan.route_plan.remove(position_);
                MakeRoutePlan.route_plan.add(position_, data);
                MakeRoutePlan.data_status.remove(position_);
                MakeRoutePlan.data_status.add(position_, data_status);
                addFragment(new MakeRoutePlan());
            }
        });

    }
}

