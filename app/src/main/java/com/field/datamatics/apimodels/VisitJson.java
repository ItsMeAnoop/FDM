package com.field.datamatics.apimodels;

import java.util.ArrayList;

/**
 * Created by anoop on 8/11/15.
 */
public class VisitJson {
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<VisitModel> getBody() {
        return body;
    }

    public void setBody(ArrayList<VisitModel> body) {
        this.body = body;
    }

    private String status;
    private ArrayList<VisitModel>body;
    public VisitJson(String status,
            ArrayList<VisitModel>body){
        this.status=status;
        this.body=body;

    }
}
