package com.field.datamatics.views.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.field.datamatics.R;
import com.field.datamatics.database.ProductSample;
import com.field.datamatics.database.ProductSample$Table;
import com.field.datamatics.ui.DividerDecoration;
import com.field.datamatics.utils.Utilities;
import com.field.datamatics.views.adapters.SamplesIssuedAdapter;
import com.raizlabs.android.dbflow.sql.language.Select;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Jith on 11/8/2015.
 */
public class SamplesIssuedFragment extends BaseFragment {


    private RecyclerView recyclerView;
    private static SamplesIssuedAdapter mAdapter;
    private ProgressBar progressBar;
    private static TextView emptyView;
    private static List<ProductSample> data;

    public SamplesIssuedFragment() {
    }

    public static SamplesIssuedFragment getInstance() {
        SamplesIssuedFragment f = new SamplesIssuedFragment();
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.samples_issued_fragment, container, false);
        setTitle("Samples issued");
        addFragmentTitle(SamplesIssuedFragment.class.getName());

        recyclerView = (RecyclerView) view.findViewById(R.id.recyler_view);
        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        emptyView = (TextView) view.findViewById(R.id.empty);
        emptyView.setText("No samples issued!");

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(llm);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            recyclerView.addItemDecoration(new DividerDecoration(getActivity()));
        mAdapter = new SamplesIssuedAdapter();
        recyclerView.setAdapter(mAdapter);

        AppCompatButton btnFilter = (AppCompatButton) view.findViewById(R.id.btnFilter);
        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getFragmentManager(), "datePicker");
            }
        });

        new loadData().execute();

        return view;
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            Calendar c = Calendar.getInstance();
            c.set(year, month, day);
            filter(Utilities.dateInSqliteFormat(c));
        }
    }

    private static void filter(String date) {
        List<ProductSample> filteredItes = new ArrayList<>();
        for (ProductSample p : data) {
            if (p.date.equals(date)) {
                filteredItes.add(p);
            }
        }
        mAdapter.setData(new ArrayList<ProductSample>(filteredItes));
        if (filteredItes.size() == 0)
            emptyView.setVisibility(View.VISIBLE);
        else
            emptyView.setVisibility(View.GONE);
    }

    private class loadData extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            Calendar calendar = Calendar.getInstance();
            data = new Select()
                    .from(ProductSample.class)
                    .where("strftime('%m', date) = ? and strftime('%Y', date) = '" + calendar.get(Calendar.YEAR) + "' ", Utilities.getMonthAsString(calendar.get(Calendar.MONTH)))
                    .orderBy(true, ProductSample$Table.CATEGORY)
                    .queryList();
            return null;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            progressBar.setVisibility(View.GONE);
            if (data == null || data.size() == 0) {
                emptyView.setVisibility(View.VISIBLE);
            } else {
                emptyView.setVisibility(View.GONE);
                mAdapter.setData((ArrayList<ProductSample>) data);
            }
        }
    }
}
