package com.field.datamatics.constants;

import android.content.Context;
import android.graphics.Color;
import android.os.Environment;

import java.io.File;

/**
 * Created by Jith on 10/18/2015.
 */
public final class Constants {
    public static final String VIDEO_DIRECTORY = "FieldDatamatics/Videos";
    public static final String PHOTO_DIRECTORY = "FieldDatamatics/Images";

    public static final String VIDEO_DIRECTORY_F = Environment.getExternalStorageDirectory()
            + File.separator + "FieldDatamatics" + File.separator + "Videos";
    public static final String PHOTO_DIRECTORY_F = Environment.getExternalStorageDirectory()
            + File.separator + "FieldDatamatics" + File.separator + "Images";
    public static final String AUDIO_DIRECTORY_F = Environment.getExternalStorageDirectory()
            + File.separator + "FieldDatamatics" + File.separator + "Audio";

    public static final String SD_PATH_IMAGE = "/sdcard/FDM/Images";
    public static final String SD_PATH_VIDEO = "/sdcard/FDM/Videos";

    public static final String PRODUCT_IMAGE = Environment.getExternalStorageDirectory()
            + File.separator + "FDM" + File.separator + "Images";

    public static final String PRODUCT_VIDEO = Environment.getExternalStorageDirectory()
            + File.separator + "FDM" + File.separator + "Videos";

    public static final String PRODUCT_DOCS = Environment.getExternalStorageDirectory()
            + File.separator + "FDM" + File.separator + "Products";

    public static final String EXCEL_PATH = Environment.getExternalStorageDirectory()
            + File.separator + "FDM" + File.separator + "Reports";


    public static final int ACTIVITY_VIDEO = 1;
    public static final int ACTIVITY_IMAGE = 2;
    public static final int ACTIVITY_DOC = 3;
    public static final int ACTIVITY_PPT = 4;
    public static final int ACTIVITY_PRODUCT = 5;
    public static final int TYPE_YES_OR_NO = 1;
    public static final int TYPE_MUTLTIPLE_CHOICE = 2;
    public static final int TYPE_FILTER_OFFICE = 1;
    public static final int TYPE_FILTER_DEFAULT = 2;
    public static final int TYPE_FILTER_CURRENT = 3;
    public static final int TYPE_FILTER_HOME = 4;

    public static final int DOC_TYPE_VIDEO = 3;
    public static final int DOC_TYPE_IMAGE = 2;
    public static final int DOC_TYPE_AUDIO = 4;

    public static final String PREF_GCM_TOKEN = "gcm_token";
    public static final String PREF_GCM_IS_SENT = "gcm_is_sent";
    public static final String PREF_TIME_SPENT = "time_spent";
    public static final String PREF_LAST_VISITED_MONTH = "last_visited_month";
    public static final String PREF_OFFICE_COORDINATES = "office_coordinates";
    public static final String PREF_HOME_COORDINATES = "home_coordinates";
    public static final String PREF_PRODUCT_DOCUMENT_LOCATION = "product_doc_location";
    public static final String PREF_STORAGE_LOCATION_IMAGE = "storage_location_image";
    public static final String PREF_STORAGE_LOCATION_VIDEO = "storage_location_video";
    public static final String PREF_STORAGE_LOCATION_AUDIO = "storage_location_audio";

    /* to retain data*/
    public static final String PREF_RESUME = "resume";
    public static final String PREF_SHOULD_RESUME = "should_resume";
    public static final String PREF_CURRENT_ACTIVITY = "current_activity";
    public static final String PREF_DATE = "date";
    public static final int STAT_TODAYS_VISIT = 1;
    public static final int STAT_VISIT_HOME = 2;
    public static final int STAT_MR_ACTIONS = 3;
    public static final int STAT_PRODUCT_LIST = 4;
    public static final int STAT_PRODUCT_DETAIL = 5;
    public static final int STAT_PRODUCT_SAMPLE = 6;
    public static final int STAT_PRODUCT_SAMPLE_DETAIL = 7;
    public static final int STAT_REMINDER = 8;
    public static final int STAT_SURVEY = 9;
    public static final int STAT_NOTES = 10;
    public static final int STAT_SIGNATURE = 11;
    public static final int STAT_PENDING_TASK = 12;
    public static final int STAT_ADDITIONAL_VISIT = 15;
    public static final int STAT_VISIT_HOME_PENDING = 13;
    //values
    public static final String VAL_VISIT_HOME_PAGE = "values_visit_home_page";
    public static final String VAL_MR_ACTION = "values_mr_action";
    public static final String VAL_PRDUCT_DETAIL = "product_detail";
    public static final String VAL_PRDUCTS = "value_products";
    public static final String VAL_SAMPLES = "value_samples";

    /* end retain data*/


    // Location updates intervals in sec
    public static int UPDATE_INTERVAL = 10000; // 10 sec
    public static int FATEST_INTERVAL = 5000; // 5 sec
    public static int DISPLACEMENT = 10; // 10 meters

    public static final String MESSAGE_RECEIVER = "message_receiver";

    public static final int[] CHART_COLORS = new int[]{Color.rgb(229, 115, 115), Color.rgb(179, 157, 219), Color.rgb(76, 175, 80), Color.rgb(188, 170, 164), Color.rgb(253, 216, 53)
            , Color.rgb(255, 112, 67), Color.rgb(244, 143, 177), Color.rgb(206, 147, 216), Color.rgb(159, 168, 218), Color.rgb(66, 165, 245), Color.rgb(38, 166, 154),
            Color.rgb(124, 179, 66), Color.rgb(158, 157, 36)};

    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }
}
