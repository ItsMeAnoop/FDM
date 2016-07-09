package com.field.datamatics.apimodels;

import com.field.datamatics.constants.ApiConstants;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by anoop on 10/11/15.
 */
public class AppLogDetailsModel {
    private String status;
    private ArrayList<HashMap<String,String>>body;
    public AppLogDetailsModel(String user_id,String logdetailid,String logintime,String logouttime,
                              String geo_in,String geo_out,String remarks){
        body=new ArrayList<HashMap<String, String>>();
        HashMap<String,String >data=new HashMap<String,String>();
        data.put("userid",user_id);
        data.put("logdetailil",logdetailid);
        data.put("type","2");
        data.put("logintime",logintime);
        data.put("logouttime",logouttime);
        data.put("geocordinate_in",geo_in);
        data.put("geocordinate_out",geo_out);
        data.put("remarks",remarks);
        status= ApiConstants.STATUS;

    }
}
