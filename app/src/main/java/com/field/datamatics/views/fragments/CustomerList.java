package com.field.datamatics.views.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.field.datamatics.R;
import com.field.datamatics.views.adapters.ClientListAdapter;
import com.field.datamatics.views.adapters.CustomerListAdapter;

import java.util.ArrayList;

/**
 * Created by anoop on 14/10/15.
 */
public class CustomerList extends BaseFragment {
    private RecyclerView rv_list;
    private ArrayList<String> data=new ArrayList<String>();
    private CustomerListAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_list,container,false);
        addFragmentTitle("CustomerList");
        rv_list= (RecyclerView) view.findViewById(R.id.rv_list);
        listSetUp();
        registerEvents();
        return view;
    }
    private void listSetUp(){
        for(int i=0;i<20;i++)
            data.add("");
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv_list.setLayoutManager(llm);
        adapter=new CustomerListAdapter(getActivity(),data);
        rv_list.setAdapter(adapter);

    }
    private void registerEvents(){
        adapter.setOnItemClickListener(new CustomerListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                addFragment(new VisitHomePage());
            }
        });

    }
}
