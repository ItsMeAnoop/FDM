package com.field.datamatics.views.fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.TextView;

import com.field.datamatics.R;
import com.field.datamatics.database.*;
import com.field.datamatics.database.RoutePlan;
import com.field.datamatics.utils.Utilities;
import com.field.datamatics.views.adapters.ScoreCardAdapter;
import com.github.lzyzsd.circleprogress.DonutProgress;
import com.raizlabs.android.dbflow.sql.language.Select;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Jith on 10/26/2015.
 */
public class ScoreCard extends BaseFragment {
    private GridView gridview;
    private Spinner spMonth, spYear;
    private ScoreCardAdapter adapter;
    private int gridwidth;
    private Calendar calendar;
    private volatile boolean isLoading;
    private String[] allMonths;
    private String[] allYears;
    private DonutProgress pgScore;


    private HashMap<Integer, String> score;

    private List<com.field.datamatics.database.RoutePlan> routePlans;

    private int percentageMonthly;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_score_card, container, false);
        setTitle("Scorecard");
        addFragmentTitle("ScoreCard");
        allMonths = getResources().getStringArray(R.array.months);
        //allYears = getResources().getStringArray(R.array.years);
        calendar = Calendar.getInstance();
        allYears = new String[2];
        allYears[0] = calendar.get(Calendar.YEAR)+"";
        allYears[1] = ((calendar.get(Calendar.YEAR)) + 1)+"";
        initializeViews(view);
        score = new HashMap<>();
        DisplayMetrics metrics = getActivity().getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        gridwidth = (int) width / 7;
        new loadData().execute();
        return view;
    }

    private void initializeViews(View view) {
        gridview = (GridView) view.findViewById(R.id.grid_monthly_visit);
        spMonth = (Spinner) view.findViewById(R.id.sp_month);
        spYear = (Spinner) view.findViewById(R.id.sp_year);
        pgScore = (DonutProgress) view.findViewById(R.id.pgMothlyScore);


        ArrayAdapter adapter = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item, allYears);
        spYear.setAdapter(adapter);
        spYear.setSelection(0);
        isLoading = true;// to prevent data loading when spinner is set for first time
        spMonth.setSelection(calendar.get(Calendar.MONTH));

        spMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.i("FDM month", "selected " + position);
                if (isLoading) return;
                calendar.set(Calendar.MONTH, position);
                new loadData().execute();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.i("FDM year", "selected " + position);
                if (isLoading) return;
                calendar.set(Calendar.YEAR, Integer.parseInt(allYears[position]));
                new loadData().execute();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private class loadData extends AsyncTask<Void, Void, Void> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Preparing Score Card... Please wait!");
            progressDialog.show();
            isLoading = true;
            score.clear();
            routePlans = new ArrayList<>();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            routePlans = new Select()
                    .from(com.field.datamatics.database.RoutePlan.class)
                    .where("strftime('%m', date) = ? and strftime('%Y', date) = '" + calendar.get(Calendar.YEAR) + "' ", Utilities.getMonthAsString(calendar.get(Calendar.MONTH)))
                    .orderBy(false, RoutePlan$Table.DATE)
                    .queryList();
            sort();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (progressDialog.isShowing())
                progressDialog.dismiss();
            adapter = new ScoreCardAdapter(getActivity(), score, gridwidth, calendar);
            gridview.setAdapter(adapter);
            isLoading = false;
            pgScore.setProgress(percentageMonthly);
        }


    }

    private void sort() {
        int visitCount = 0;
        int pendingCount = 0;
        int totalVisit = 0;
        int totalPending = 0;
        int pos = 0;
        int size = routePlans.size();
        for (RoutePlan r : routePlans) {
            if (pos == 0) {
                if (r.Visittype == 1 || r.Visittype == 3) {
                    visitCount++;
                    totalVisit++;
                } else {
                    pendingCount++;
                    totalPending++;
                }
            } else {
                String lastDate = routePlans.get(pos - 1).Date;
                if (lastDate.equals(r.Date)) {
                    if (r.Visittype == 1 || r.Visittype == 3) {
                        visitCount++;
                        totalVisit++;
                    } else {
                        pendingCount++;
                        totalPending++;
                    }
                } else {
                    int d = Integer.parseInt(Utilities.dateToString(lastDate, "yyyy-MM-dd", "dd"));
                    int prcentage = calculatePercentage(visitCount, size);
                    score.put(d, prcentage + "%");
                    visitCount = 0;
                    pendingCount = 0;
                    if (r.Visittype == 1 || r.Visittype == 3) {
                        visitCount++;
                        totalVisit++;
                    } else {
                        pendingCount++;
                        totalPending++;
                    }
                }
            }
            if (pos == size - 1) {
                int d = Integer.parseInt(Utilities.dateToString(r.Date, "yyyy-MM-dd", "dd"));
                int prcentage = calculatePercentage(visitCount, size);
                score.put(d, prcentage + "%");
            }
            pos++;
        }
        percentageMonthly = calculatePercentage(totalVisit, size);
    }

    private int calculatePercentage(int a, int b) {

        if (b == 0) return 0;
        float per = a * 100f / b;
        return Math.round(per);
    }
}
