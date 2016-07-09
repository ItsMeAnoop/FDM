package com.field.datamatics.apimodels;

import java.util.ArrayList;

/**
 * Created by anoop on 8/11/15.
 */
public class SurveyDetailsJson {
    private String status;
    private ArrayList<Surveydetails>body;
    public SurveyDetailsJson(String status,
            ArrayList<Surveydetails>body){
        this.status=status;
        this.body=body;

    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<Surveydetails> getBody() {
        return body;
    }

    public void setBody(ArrayList<Surveydetails> body) {
        this.body = body;
    }
}
