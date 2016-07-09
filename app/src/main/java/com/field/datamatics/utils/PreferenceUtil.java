package com.field.datamatics.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by anoop on 11/11/15.
 */
public class PreferenceUtil {
    private static SharedPreferences pref;
    private static SharedPreferences.Editor editor;
    private static Context context;
    private static int PRIVATE_MODE=0;
    private static String PREFERENCE_NAME="my_preference";
    private String ISLOGIN="isLogin";
    private static PreferenceUtil intsance=new PreferenceUtil();
    private String ISTESTING="ISTESTING";
    private String LAST_SYNC_DATE="LASTSYNCDATE";
    private String SYNC="SYNC";
    private String SURVEY="survey";
    private String CLIENT_ID="clientID";
    private String CUSTOMER_ID="customer_id";
    private String USER_ID="user_id";

    private PreferenceUtil(){
    };
    public static PreferenceUtil getIntsance(){
        context=AppControllerUtil.getInstance();
        pref=context.getSharedPreferences(PREFERENCE_NAME,PRIVATE_MODE);
        editor=pref.edit();
        return intsance;
    }
    public boolean isLogin(){
        return  pref.getBoolean(ISLOGIN,false);
    }
    public void setIsLogin(boolean isLogin){
        editor.putBoolean(ISLOGIN,isLogin);
        editor.commit();
    }
    public boolean isTesting(){
        return  pref.getBoolean(ISTESTING,false);
    }
    public void setIsTesting(boolean isTesting){
        editor.putBoolean(ISTESTING,isTesting);
        editor.commit();
    }
    public String getLastSyncDate(){
        return  pref.getString(LAST_SYNC_DATE, null);
    }
    public void setLastSyncDate(String date){
        editor.putString(LAST_SYNC_DATE, date);
        editor.commit();
    }
    public boolean isSyncManual(){
        return  pref.getBoolean(SYNC, false);
    }
    public void setSyncManul(boolean sync){
        editor.putBoolean(SYNC, sync);
        editor.commit();
    }
    public boolean isSurvey(){
        return  pref.getBoolean(SURVEY,false);
    }
    public void setSurvey(boolean survey){
        editor.putBoolean(SURVEY,survey);
        editor.commit();
    }
    public String getCLIENT_ID(){
        return  pref.getString(CLIENT_ID, "");
    }
    public void setCLIENT_ID(String date){
        editor.putString(CLIENT_ID, date);
        editor.commit();
    }
    public String getCUSTOMER_ID(){
        return  pref.getString(CUSTOMER_ID, "");
    }
    public void setCUSTOMER_ID(String date){
        editor.putString(CUSTOMER_ID, date);
        editor.commit();
    }
    public String getUSER_ID(){
        return  pref.getString(USER_ID, "");
    }
    public void setUSER_ID(String date){
        editor.putString(USER_ID, date);
        editor.commit();
    }

}
