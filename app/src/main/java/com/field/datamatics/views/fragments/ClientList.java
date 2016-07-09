package com.field.datamatics.views.fragments;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.field.datamatics.R;
import com.field.datamatics.database.JoinClientRoutePlan;
import com.field.datamatics.ui.DividerDecoration;
import com.field.datamatics.views.adapters.TodaysClientAdapter;

import java.util.ArrayList;

/**
 * Created by anoop on 22/9/15.
 */
public class ClientList extends BaseFragment {
    private RecyclerView rv_list;
    private ArrayList<JoinClientRoutePlan> data=new ArrayList<JoinClientRoutePlan>();
    private TodaysClientAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_client_list,container,false);
        addFragmentTitle("ClientList");
        rv_list= (RecyclerView) view.findViewById(R.id.rv_list);
        listSetUp();
        return view;
    }
    private void listSetUp(){
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv_list.setLayoutManager(llm);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            rv_list.addItemDecoration(new DividerDecoration(getActivity()));
        for(int i=0;i< MonthlyVisit.route_plan.size();i++){
            JoinClientRoutePlan d=new JoinClientRoutePlan();
            com.field.datamatics.database.RoutePlan rp= MonthlyVisit.route_plan.get(i);
            d.Client_First_Name=rp.client.Client_First_Name;
            d.Client_Last_Name=rp.client.Client_Last_Name;
            d.Speciality=rp.client.Speciality;
            d.Client_Email=rp.client.Client_Email;
            data.add(d);
        }

        adapter = new TodaysClientAdapter(getActivity(),data);
        rv_list.setAdapter(adapter);

    }
}
