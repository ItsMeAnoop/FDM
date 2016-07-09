package com.field.datamatics.views.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.field.datamatics.R;
import com.field.datamatics.utils.AppControllerUtil;
import com.field.datamatics.views.adapters.ClientListAdapter;
import com.field.datamatics.views.adapters.RemainderAdapter;

import java.util.ArrayList;

/**
 * Created by anoop on 27/9/15.
 */
public class ReminderList extends BaseFragment {
    private RecyclerView rv_list;
    private Button btn_add;
    private ArrayList<String> data = new ArrayList<String>();
    private RemainderAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_remainder_list, container, false);
        setTitle("Reminders");
        addFragmentTitle("ReminderList");
        rv_list = (RecyclerView) view.findViewById(R.id.rv_list);
        btn_add= (Button) view.findViewById(R.id.btn_add);
        listSetUp();
        registerEvents();
        return view;
    }

    private void listSetUp() {
        for (int i = 0; i < 10; i++)
            data.add("");
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv_list.setLayoutManager(llm);
        adapter = new RemainderAdapter(getActivity(), data);
        rv_list.setAdapter(adapter);

    }

    private void registerEvents() {
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addRemainder();
            }
        });


    }
    private void addRemainder(){
        Button btn_add;
        final Dialog dialog=new Dialog(getActivity());
        dialog.setContentView(R.layout.dialog_remainder);
        dialog.setTitle("Set Remainder");
        btn_add= (Button) dialog.findViewById(R.id.btn_add);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
