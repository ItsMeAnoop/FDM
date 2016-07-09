package com.field.datamatics.utils;

import android.app.Activity;
import android.app.Application;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Environment;
import android.support.multidex.MultiDex;

import com.field.datamatics.apimodels.ActivityModel;
import com.field.datamatics.apimodels.ProductQuatityModel;
import com.field.datamatics.apimodels.ReminderModel;
import com.field.datamatics.constants.ApiConstants;
import com.field.datamatics.constants.Constants;
import com.field.datamatics.database.User;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.Select;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by anoop on 27/9/15.
 */
public class AppControllerUtil extends Application {
    private static AppControllerUtil instance;
    private String current_fragment = "";
    private boolean isCheckIn;
    private boolean isSessionStart;
    private static User user;

    private static SharedPreferences mPrefs;
    private static SharedPreferences resumePrefs;
    private int routePlanNumber;

    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
        this.instance = this;
        FlowManager.init(this);
        createProductDir();

    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        FlowManager.destroy();
    }


    public static AppControllerUtil getInstance() {
        return instance;
    }

    public String getCurrent_fragment() {
        return current_fragment;
    }

    public void setCurrent_fragment(String current_fragment) {
        this.current_fragment = current_fragment;
    }

    public boolean isCheckIn() {
        return isCheckIn;
    }

    public void setIsCheckIn(boolean isCheckIn) {
        this.isCheckIn = isCheckIn;
    }

    public boolean isSessionStart() {
        return isSessionStart;
    }

    public void setIsSessionStart(boolean isSessionStart) {
        this.isSessionStart = isSessionStart;
    }

    public static SharedPreferences getPrefs() {
        if (mPrefs == null)
            mPrefs = instance.getSharedPreferences("fdm", Activity.MODE_PRIVATE);
        return mPrefs;
    }

    public static User getUser() {
        if (user == null) {
            user = new Select().from(User.class).querySingle();
        }
        return user;
    }

    public static void setLoginStatus(boolean isLogined) {
        SharedPreferences.Editor editor = getPrefs().edit();
        editor.putBoolean("is_logined", isLogined);
        editor.commit();
    }

    public static boolean isLogined() {
        return getPrefs().getBoolean("is_logined", false);
    }

    public static void setCheckinTime(long time) {
        getPrefs().edit().putLong("checkintime", time).commit();
    }

    public static long getCheckinTime() {
        return getPrefs().getLong("checkintime", 0);
    }

    public int getRoutePlanNumber() {
        return routePlanNumber;
    }

    public void setRoutePlanNumber(int routePlanNumber) {
        this.routePlanNumber = routePlanNumber;
    }

    private void createProductDir() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                File f;
                if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT && Environment.isExternalStorageRemovable())
                    f = new File(getExternalFilesDir(null).getAbsolutePath() + File.separator + "Products");
                else
                    f = new File(Constants.PRODUCT_DOCS);
                if (!f.exists()) f.mkdirs();
            }
        }).start();
    }

    public static SharedPreferences getPrefsResume() {
        if (resumePrefs == null)
            resumePrefs = instance.getSharedPreferences(Constants.PREF_RESUME, Activity.MODE_PRIVATE);
        return resumePrefs;
    }
}
