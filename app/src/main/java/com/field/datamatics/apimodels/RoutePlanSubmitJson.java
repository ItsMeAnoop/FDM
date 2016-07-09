package com.field.datamatics.apimodels;

import java.util.ArrayList;

/**
 * Created by anoop on 8/11/15.
 */
public class RoutePlanSubmitJson {
    private String status;
    private ArrayList<RoutePlanSubmit>body;
    public RoutePlanSubmitJson(String status,
            ArrayList<RoutePlanSubmit>body){
        this.status=status;
        this.body=body;

    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<RoutePlanSubmit> getBody() {
        return body;
    }

    public void setBody(ArrayList<RoutePlanSubmit> body) {
        this.body = body;
    }
}
