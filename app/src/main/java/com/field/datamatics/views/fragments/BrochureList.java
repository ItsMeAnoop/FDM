package com.field.datamatics.views.fragments;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.field.datamatics.R;
import com.field.datamatics.apimodels.ActivityModel;
import com.field.datamatics.constants.Constants;
import com.field.datamatics.database.*;
import com.field.datamatics.utils.AppControllerUtil;
import com.field.datamatics.utils.GPSTracker;
import com.field.datamatics.utils.Utilities;
import com.field.datamatics.views.MainActivity;
import com.field.datamatics.views.adapters.BrochureListAdapter;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Calendar;
import java.util.HashMap;

/**
 * Created by Jith on 22/10/2015.
 * Brochure list screen logic
 */
public class BrochureList extends BaseFragment {
    private GridView grid;
    private BrochureListAdapter adapter;
    private ProgressBar progressBar;
    private File[] files;
    private String productNumber;
    private String category;
    private int routePlanNumber;
    private static BrochureList mInstance;
    private Calendar visitedDate;

    private boolean isActivity;
    private Calendar activityDate;

    private MainActivity mContainer;
    private String startCoordinate = "";
    private String endCoordinate = "";

    private int activityCount = 1;
    private String activityName;
    private GPSTracker gps;
    private String corndnt_in, cordnt_out;


    public BrochureList() {
    }

    public static BrochureList getInstance(String productNumber,String category, int routePlanNumber, Calendar visitedDate, boolean isNew) {
        if (mInstance == null || isNew) {
            mInstance = new BrochureList();
            mInstance.productNumber = productNumber;
            mInstance.category = category;
            mInstance.routePlanNumber = routePlanNumber;
            mInstance.visitedDate = visitedDate;
        }
        return mInstance;

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_brochure, container, false);
        gps = new GPSTracker(getActivity());
        mContainer = (MainActivity) getActivity();
        initializeViews(view);
        new loadFiles().execute();
        return view;
    }

    /**
     * bind view elements
     * @param view
     */
    private void initializeViews(View view) {
        grid = (GridView) view.findViewById(R.id.grid);
        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        DisplayMetrics metrics = getActivity().getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int gridwidth = (int) width / 5;
        adapter = new BrochureListAdapter(getActivity(), gridwidth);
        grid.setEmptyView(view.findViewById(android.R.id.empty));
        grid.setAdapter(adapter);

        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                File file = (File) adapter.getItem(position);
                MimeTypeMap myMime = MimeTypeMap.getSingleton();
                Intent newIntent = new Intent(Intent.ACTION_VIEW);
                String mimeType = myMime.getMimeTypeFromExtension(Utilities.fileExt(file.getName()).substring(1));
                newIntent.setDataAndType(Uri.fromFile(file), mimeType);
                newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                try {
                    getActivity().startActivity(newIntent);
                    activityName = file.getName();
                    activityDate = Calendar.getInstance();
                    isActivity = true;
                    Location loc = mContainer.mLastLocation;
                    if (loc != null) {
                        startCoordinate = loc.getLatitude() + "," + loc.getLongitude();
                    }
                } catch (ActivityNotFoundException e) {
                    isActivity = false;
                    Toast.makeText(getActivity(), "No App found to open this file.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    /**
     * Load files service
     */
    private class loadFiles extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Boolean doInBackground(Void... params) {
          /*  String productDocLoc = AppControllerUtil.getPrefs().getString(Constants.PREF_PRODUCT_DOCUMENT_LOCATION, Constants.PRODUCT_DOCS);
            File imageDirectory = new File(productDocLoc + File.separator + productNumber);
            if (!imageDirectory.exists())
                return false;
            FilenameFilter filter = new FilenameFilter() {
                @Override
                public boolean accept(File dir, String filename) {
                    filename = filename.toLowerCase();
                    return (filename.endsWith(".pdf")
                            || filename.endsWith(".doc")
                            || filename.endsWith(".docx")
                            || filename.endsWith(".xls")
                            || filename.endsWith(".xlsx") || filename.endsWith(".ppt"));
                }
            };
            if (!imageDirectory.exists())
                imageDirectory.mkdirs();
            files = imageDirectory.listFiles(filter);*/

            String productDocLoc = AppControllerUtil.getPrefs().getString(Constants.PREF_PRODUCT_DOCUMENT_LOCATION, Constants.PRODUCT_DOCS);
            File imageDirectory = new File(productDocLoc);
            if (!imageDirectory.exists())
                return false;
            FilenameFilter filter = new FilenameFilter() {
                @Override
                public boolean accept(File dir, String filename) {
                    filename = filename.toLowerCase();
                    return ((filename.startsWith(productNumber.toLowerCase())
                            || filename.startsWith(category.toLowerCase()))
                            && (filename.endsWith(".pdf")
                            || filename.endsWith(".doc")
                            || filename.endsWith(".docx")
                            || filename.endsWith(".xls")
                            || filename.endsWith(".xlsx")
                            || filename.endsWith(".ppt")
                            || filename.endsWith(".txt")));
                }
            };
            if (!imageDirectory.exists())
                imageDirectory.mkdirs();
            files = imageDirectory.listFiles(filter);
            return true;
        }

        @Override
        protected void onPostExecute(Boolean s) {
            super.onPostExecute(s);
            progressBar.setVisibility(View.GONE);
            if (s)
                adapter.setData(files);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isActivity) {
            Location loc = mContainer.mLastLocation;
            if (loc != null) {
                endCoordinate = loc.getLatitude() + "," + loc.getLongitude();
            }
            Activities activity = new Activities();
            activity.visitdate = Utilities.dateInSqliteFormat(visitedDate);
            activity.activitydate = Utilities.dateInSqliteFormat(activityDate);
            activity.routePlan = new com.field.datamatics.database.RoutePlan();
            activity.routePlan.Route_Plan_Number = routePlanNumber;
            activity.Activity = Constants.ACTIVITY_DOC;
            activity.starttime = Utilities.dateToString(activityDate, "yyyy-MM-dd HH:mm:ss");
            activity.endtime = Utilities.dateToString(Calendar.getInstance(), "yyyy-MM-dd HH:mm:ss");
            activity.startCoOrdinates = startCoordinate;
            activity.endCoOrdinates = endCoordinate;
            activity.activityname = activityName;
            activity.save();
            isActivity = false;
            if (gps.canGetLocation()) {

                double latitude = gps.getLatitude();
                double longitude = gps.getLongitude();
                corndnt_in = latitude + "," + longitude;
                cordnt_out = latitude + "," + longitude;
            } else {
                corndnt_in = activity.startCoOrdinates;
                cordnt_out = activity.endCoOrdinates;
            }
        }
    }
}
