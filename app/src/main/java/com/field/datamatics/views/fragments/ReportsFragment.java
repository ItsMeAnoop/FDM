package com.field.datamatics.views.fragments;

import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.field.datamatics.R;
import com.field.datamatics.constants.Constants;
import com.field.datamatics.views.helper.ReportsAsync;

import java.io.File;

/**
 * Created by Jith on 10/26/2015.
 */
public class ReportsFragment extends BaseFragment implements View.OnClickListener {

    public ReportsFragment() {
    }

    public static ReportsFragment getInstance() {
        ReportsFragment f = new ReportsFragment();
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        addFragmentTitle(ReportsFragment.class.getName());
        View v = inflater.inflate(R.layout.fragment_reports, container, false);
        setTitle("Reports");
        v.findViewById(R.id.btn_report_visit).setOnClickListener(this);
        v.findViewById(R.id.btn_report_pending).setOnClickListener(this);
        v.findViewById(R.id.btn_report_schedule).setOnClickListener(this);
        v.findViewById(R.id.btn_report_todays_visit).setOnClickListener(this);

        TextView lblLocaiton = (TextView) v.findViewById(R.id.lblLocation);
        StringBuilder sb = new StringBuilder();
        sb.append("Reports are stored in : ");
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT && Environment.isExternalStorageRemovable())
            sb.append(getActivity().getExternalFilesDir(null).getAbsolutePath() + File.separator + "Reports");
        else sb.append(Constants.EXCEL_PATH);
        lblLocaiton.setText(sb);
        return v;
    }

    @Override
    public void onClick(View view) {
        ReportsAsync generator = new ReportsAsync(getActivity());
        switch (view.getId()) {
            case R.id.btn_report_visit:
                generator.generateVisitedDetails();
                break;
            case R.id.btn_report_pending:
                generator.generatePendingList();
                break;
            case R.id.btn_report_todays_visit:
                generator.generateTodaysVisit();
                break;
            case R.id.btn_report_schedule:
                generator.geneateSchedule();
                break;
            default:
                break;
        }
    }
}
