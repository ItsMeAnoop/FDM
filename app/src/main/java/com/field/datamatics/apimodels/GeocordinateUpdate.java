package com.field.datamatics.apimodels;

import com.field.datamatics.constants.ApiConstants;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by anoop on 10/11/15.
 */
public class GeocordinateUpdate {
    private String status;
    private ArrayList<HashMap<String,String>> body;
    public GeocordinateUpdate(String geo,String type,String id,String remarks) {
        body = new ArrayList<HashMap<String, String>>();
        HashMap<String, String> data = new HashMap<String, String>();
        data.put("geocordinate_office", geo);
        //home-1,office-2
        data.put("type", type);
        data.put("id", id);
        data.put("remarks", remarks);
        body.add(data);
        status = ApiConstants.STATUS;
    }

}
