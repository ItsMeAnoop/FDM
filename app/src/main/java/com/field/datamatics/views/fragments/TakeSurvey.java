package com.field.datamatics.views.fragments;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.field.datamatics.R;
import com.field.datamatics.constants.Constants;
import com.field.datamatics.database.ProductSample$Table;
import com.field.datamatics.database.RoutePlan;
import com.field.datamatics.database.SurveyDetails;
import com.field.datamatics.database.SurveyMaster;
import com.field.datamatics.database.SurveyMaster$Table;
import com.field.datamatics.ui.DividerDecoration;
import com.field.datamatics.utils.AppControllerUtil;
import com.field.datamatics.utils.PreferenceUtil;
import com.field.datamatics.utils.Utilities;
import com.field.datamatics.views.MainActivity;
import com.field.datamatics.views.adapters.SurveyListAdapter;
import com.raizlabs.android.dbflow.runtime.TransactionManager;
import com.raizlabs.android.dbflow.runtime.transaction.BaseTransaction;
import com.raizlabs.android.dbflow.runtime.transaction.TransactionListener;
import com.raizlabs.android.dbflow.runtime.transaction.process.ProcessModelInfo;
import com.raizlabs.android.dbflow.runtime.transaction.process.SaveModelTransaction;
import com.raizlabs.android.dbflow.sql.builder.Condition;
import com.raizlabs.android.dbflow.sql.language.Delete;
import com.raizlabs.android.dbflow.sql.language.Select;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Jith on 17/10/2015.
 * Survey screen logic
 */
public class TakeSurvey extends BaseFragment {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private List<SurveyMaster> data;
    private SurveyListAdapter adapter;
    private Button saveButton;
    private TextView empty;

    private static TakeSurvey mInstance;

    private String startTime;
    private String startCoordinate;
    private MainActivity mContainer;

    public TakeSurvey() {
    }

    public interface OnSurveyFinishedListener {
        public void OnSurveyFinished(ArrayList<SurveyDetails> surveyResult);
    }

    private static OnSurveyFinishedListener mOnSurveyFinishedListerner;

    public static TakeSurvey getInstance(Bundle data, OnSurveyFinishedListener listener, boolean isNew) {
        if (mInstance == null || isNew) {
            mInstance = new TakeSurvey();
            mInstance.setArguments(data);
            mOnSurveyFinishedListerner = listener;
        }
        return mInstance;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        SharedPreferences.Editor edit = AppControllerUtil.getPrefsResume()
                .edit();
        edit.putString(Constants.PREF_DATE, Utilities.dateToString(Calendar.getInstance(), "yyyy-MM-dd"));
        edit.putInt(Constants.PREF_CURRENT_ACTIVITY, Constants.STAT_SURVEY);
        edit.commit();
        mContainer = (MainActivity) getActivity();
        View view = inflater.inflate(R.layout.fragment_survey, container, false);
        addFragmentTitle("TakeSurvey");
        initializeViews(view);
        if (data == null || data.size() == 0) {
            Log.i("FDM", "survey not resuming");
            new loadData().execute();
        } else Log.i("FDM", "survey resume");
        startTime = Utilities.dateToString(Calendar.getInstance(), "yyyy-MM-dd HH:mm:ss");
        Location loc = mContainer.mLastLocation;
        if (loc != null) {
            startCoordinate = loc.getLatitude() + "," + loc.getLongitude();
        }
        return view;
    }

    private void initializeViews(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.recylerView);
        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        saveButton = (Button) view.findViewById(R.id.save_button);
        empty = (TextView) view.findViewById(R.id.empty);
        if (data == null)
            data = new ArrayList<>();
        else if (PreferenceUtil.getIntsance().isSurvey() == false)
            data = new ArrayList<>();
        adapter = new SurveyListAdapter(getActivity(), (ArrayList<SurveyMaster>) data);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            recyclerView.addItemDecoration(new DividerDecoration(getActivity()));
        recyclerView.setAdapter(adapter);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PreferenceUtil.getIntsance().setSurvey(true);
                ArrayList<SurveyMaster> result = adapter.getAnsweredQuestions();
                RoutePlan routePlan = new RoutePlan();
                routePlan.Route_Plan_Number = getArguments().getInt("route_plan_number");
                ArrayList<SurveyDetails> list = null;
                if (result.size() > 0) {
                    list = new ArrayList<>();
                    for (SurveyMaster sm : result) {
                        SurveyDetails details = new SurveyDetails();
                        details.surveyMaster = sm;
                        details.visiteddate = Utilities.dateToString(Calendar.getInstance(), "yyyy-MM-dd");
                        details.routePlan = routePlan;
                        details.Survey_Date = details.visiteddate;
                        //details.Selected_Option = sm.checkedItemPosition + "";
                        details.Selected_Option = sm.checkedItemPosition + "";
                        details.Type = sm.Type;
                        Location loc = mContainer.mLastLocation;
                        if (loc != null) {
                            details.endCoordinates = loc.getLatitude() + "," + loc.getLongitude();
                        }
                        details.startCoordinates = startCoordinate;
                        details.startTime = startTime;
                        details.endTime = Utilities.dateToString(Calendar.getInstance(), "yyyy-MM-dd HH:mm:ss");
                        list.add(details);
                    }
                }
                if (list != null) {
                    final ProgressDialog pd = new ProgressDialog(getActivity());
                    pd.setMessage("Saving survey details... Please wait!");
                    pd.setCancelable(false);
                    pd.show();
                    final ArrayList<SurveyDetails> finalList = list;
                    new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected Void doInBackground(Void... voids) {
                            Delete.table(SurveyDetails.class, (Condition.column(ProductSample$Table.ROUTEPLAN_ROUTE_PLAN_NUMBER)
                                    .eq(getArguments().getInt("route_plan_number"))));
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void aVoid) {
                            super.onPostExecute(aVoid);
                            TransactionListener<List<SurveyDetails>> mListener = new TransactionListener<List<SurveyDetails>>() {
                                @Override
                                public void onResultReceived(List<SurveyDetails> result) {

                                }

                                @Override
                                public boolean onReady(BaseTransaction<List<SurveyDetails>> transaction) {
                                    return false;
                                }

                                @Override
                                public boolean hasResult(BaseTransaction<List<SurveyDetails>> transaction, List<SurveyDetails> result) {
                                    if (pd.isShowing()) pd.cancel();
                                    getActivity().onBackPressed();
                                    return true;
                                }
                            };
                            TransactionManager.getInstance()
                                    .addTransaction(new SaveModelTransaction<>(ProcessModelInfo.withModels(finalList).result(mListener)));
                        }
                    }.execute();
                } else {
                    getActivity().onBackPressed();
                }
            }
        });

    }

    private class loadData extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(Void... params) {
            String today = Utilities.dateInSqliteFormat(Calendar.getInstance());
            data = new Select().from(SurveyMaster.class)
                    .where(Condition.column(SurveyMaster$Table.VALIDFROM).lessThanOrEq(today))
                    .and(Condition.column(SurveyMaster$Table.VALIDTO).greaterThanOrEq(today))
                    .queryList();
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressBar.setVisibility(View.GONE);
            if (data == null || data.size() == 0) {
                empty.setVisibility(View.VISIBLE);
            } else
                adapter.setData((ArrayList<SurveyMaster>) data);
        }
    }
}
