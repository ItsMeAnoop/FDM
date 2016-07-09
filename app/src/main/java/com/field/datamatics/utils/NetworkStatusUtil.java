package com.field.datamatics.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by anoop on 15/8/15.
 */
public class NetworkStatusUtil {
    private static NetworkStatusUtil instance=new NetworkStatusUtil();
    private NetworkStatusUtil(){

    }
    public static NetworkStatusUtil getInstance(){
        return instance;
    }
    public  boolean isNetworkAvailable(){
        Context context=AppControllerUtil.getInstance();
        ConnectivityManager connectivityManager= (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager!=null){
            NetworkInfo[] info=connectivityManager.getAllNetworkInfo();
            if(info!=null){
                for(int i=0;i<info.length;i++){
                    if(info[i].getState()==NetworkInfo.State.CONNECTED){
                        return true;
                    }
                }
                return  false;
            }
        }
        return false;
    }

}
