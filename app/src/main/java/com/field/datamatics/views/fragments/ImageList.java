package com.field.datamatics.views.fragments;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.util.Log;
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
import com.field.datamatics.database.Activities;
import com.field.datamatics.database.RoutePlan;
import com.field.datamatics.utils.AppControllerUtil;
import com.field.datamatics.utils.GPSTracker;
import com.field.datamatics.utils.Utilities;
import com.field.datamatics.views.MainActivity;
import com.field.datamatics.views.adapters.ImageListAdapter;
import com.field.datamatics.views.dialogs.ImageViewer;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Calendar;
import java.util.HashMap;

/**
 * Created by Jith on 22/10/2015.
 * Image list view logic
 */
public class ImageList extends BaseFragment {
    private GridView grid;
    private ImageListAdapter adapter;
    private ProgressBar progressBar;
    private File[] imageFiles;

    private String productNumber;
    private String category;
    private int routePlanNumber;
    private static ImageList mInstance;

    private boolean isActivity;
    private int activityType;
    Calendar visitedDate;
    Calendar activityDate;
    private String activityName = "";

    private MainActivity mContainer;
    private String startCoordinate = "";
    private String endCoordinate = "";

    private int activityCount = 1;
    private GPSTracker gps;
    private String corndnt_in, cordnt_out;

    public ImageList() {
    }

    public static ImageList getInstance(String productNumber, String category, int routePlanNumber, Calendar visitedDate, boolean isNew) {
        if (mInstance == null || isNew) {
            mInstance = new ImageList();
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
        View view = inflater.inflate(R.layout.fragment_image_video_bro_list, container, false);
        gps = new GPSTracker(getActivity());

        mContainer = (MainActivity) getActivity();
        setUp(view);
        registerEvents();
        new loadImageFiles().execute();
        return view;
    }

    /**
     * Set up list and progress bar
     * @param view
     */
    private void setUp(View view) {
        grid = (GridView) view.findViewById(R.id.grid);
        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        DisplayMetrics metrics = getActivity().getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int gridwidth = (int) width / 3;
        adapter = new ImageListAdapter(getActivity(), gridwidth);
        grid.setEmptyView(view.findViewById(android.R.id.empty));
        grid.setAdapter(adapter);
    }

    /**
     * register events in screen
     */
    private void registerEvents() {

        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ImageViewer imageViewer=new ImageViewer();
                imageViewer.showImageViewer(getActivity(),imageFiles);
                /*File file = (File) adapter.getItem(position);
                MimeTypeMap myMime = MimeTypeMap.getSingleton();
                Intent newIntent = new Intent(Intent.ACTION_VIEW);
                String mimeType = myMime.getMimeTypeFromExtension(Utilities.fileExt(file.getName()).substring(1));
                newIntent.setDataAndType(Uri.fromFile(file), mimeType);
                newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                try {
                    getActivity().startActivity(newIntent);
                    activityName = file.getName();
                    isActivity = true;
                    activityType = Constants.DOC_TYPE_IMAGE;
                    activityDate = Calendar.getInstance();
                    Location loc = mContainer.mLastLocation;
                    if (loc != null) {
                        startCoordinate = loc.getLatitude() + "," + loc.getLongitude();
                    }
                    Log.i("FDM", "Started");
                } catch (ActivityNotFoundException e) {
                    isActivity = false;
                    Toast.makeText(getActivity(), "No App found to open this file.", Toast.LENGTH_LONG).show();
                }*/
            }
        });
    }


    /**
     * load image files
     */
    private class loadImageFiles extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            /*String productDocLoc = AppControllerUtil.getPrefs().getString(Constants.PREF_PRODUCT_DOCUMENT_LOCATION, Constants.PRODUCT_DOCS);
            log(productDocLoc);
            File imageDirectory = new File(productDocLoc + File.separator + productNumber);
            log("imge_path:" + imageDirectory.getAbsolutePath());
            if (!imageDirectory.exists())
                return false;

            FilenameFilter filter = new FilenameFilter() {
                @Override
                public boolean accept(File dir, String filename) {
                    filename = filename.toLowerCase();
                    return (filename.endsWith(".jpg")
                            || filename.endsWith(".png")
                            || filename.endsWith(".jpeg")
                            || filename.endsWith(".gif")
                            || filename.endsWith(".tif"));
                }
            };
            if (!imageDirectory.exists())
                imageDirectory.mkdirs();
            imageFiles = imageDirectory.listFiles(filter);*/
            String productDocLoc = AppControllerUtil.getPrefs().getString(Constants.PREF_PRODUCT_DOCUMENT_LOCATION, Constants.PRODUCT_DOCS);
            log(productDocLoc);
            File imageDirectory = new File(productDocLoc);
            if (!imageDirectory.exists())
                return false;
            if(productNumber.contains("/"))
                productNumber=productNumber.replaceFirst("/","A");
            FilenameFilter filter = new FilenameFilter() {
                @Override
                public boolean accept(File dir, String filename) {
                    filename = filename.toLowerCase();
                    return ((filename.startsWith(productNumber.toLowerCase())
                            || filename.startsWith(category.toLowerCase()))
                            && (filename.endsWith(".jpg")
                            || filename.endsWith(".png")
                            || filename.endsWith(".jpeg")
                            || filename.endsWith(".gif")
                            || filename.endsWith(".tif")));
                }
            };
            if (!imageDirectory.exists())
                imageDirectory.mkdirs();
            imageFiles = imageDirectory.listFiles(filter);
            return true;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            super.onPostExecute(success);
            progressBar.setVisibility(View.GONE);
            if (success)
                adapter.setData(imageFiles);
        }
    }

    public static String getMimeType(String url) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return type;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("FDM", "resumed");
        if (isActivity) {
            Location loc = mContainer.mLastLocation;
            if (loc != null) {
                endCoordinate = loc.getLatitude() + "," + loc.getLongitude();
            }
            Activities activity = new Activities();
            activity.visitdate = Utilities.dateInSqliteFormat(visitedDate);
            activity.activitydate = Utilities.dateInSqliteFormat(activityDate);
            activity.routePlan = new RoutePlan();
            activity.routePlan.Route_Plan_Number = routePlanNumber;
            activity.Activity = activityType;
            activity.activityname = activityName;
            activity.starttime = Utilities.dateToString(activityDate, "yyyy-MM-dd HH:mm:ss");
            activity.endtime = Utilities.dateToString(Calendar.getInstance(), "yyyy-MM-dd HH:mm:ss");
            activity.startCoOrdinates = startCoordinate;
            activity.endCoOrdinates = endCoordinate;
            activity.save();
            activityName = "";
            isActivity = false;


            //prepare date for sending to server
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
