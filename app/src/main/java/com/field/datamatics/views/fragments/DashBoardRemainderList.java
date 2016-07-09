package com.field.datamatics.views.fragments;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.field.datamatics.R;
import com.field.datamatics.database.Reminder;
import com.field.datamatics.database.Reminder$Table;
import com.field.datamatics.ui.DividerDecoration;
import com.field.datamatics.utils.AppControllerUtil;
import com.field.datamatics.views.adapters.MainRemainderListAdapter;
import com.raizlabs.android.dbflow.sql.language.Select;

import java.util.ArrayList;

/**
 * Created by Jith on 20/10/2015.
 */
public class DashBoardRemainderList extends BaseFragment {
    private RecyclerView recyclerView;
    private ArrayList<Reminder> data;
    private MainRemainderListAdapter adapter;
    private TextView emptyView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        setTitle("Reminders");
        AppControllerUtil.getInstance().setCurrent_fragment("DashBoardRemainderList");

        data = new ArrayList<Reminder>();
        emptyView = (TextView) view.findViewById(R.id.empty);
        recyclerView = (RecyclerView) view.findViewById(R.id.rv_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            recyclerView.addItemDecoration(new DividerDecoration(getActivity()));
        adapter = new MainRemainderListAdapter(getActivity(), data);
        recyclerView.setAdapter(adapter);

        new loadData().execute();
        return view;
    }

    private class loadData extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {
            data = (ArrayList<Reminder>) new Select().from(Reminder.class)
                    .orderBy(true, Reminder$Table.DATE).queryList();
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            adapter.setData(data);
            if (data == null || data.size() == 0) {
                recyclerView.setVisibility(View.GONE);
                emptyView.setVisibility(View.VISIBLE);
            }
        }
    }

}

