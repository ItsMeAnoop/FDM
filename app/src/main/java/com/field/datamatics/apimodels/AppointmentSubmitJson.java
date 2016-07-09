package com.field.datamatics.apimodels;

import java.util.ArrayList;

/**
 * Created by anoop on 8/11/15.
 */
public class AppointmentSubmitJson {
    private String status;

    public ArrayList<AppoinmentResponseBody> getBody() {
        return body;
    }

    public void setBody(ArrayList<AppoinmentResponseBody> body) {
        this.body = body;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    private ArrayList<AppoinmentResponseBody>body;
    public AppointmentSubmitJson(String status,
            ArrayList<AppoinmentResponseBody>body){
        this.status=status;
        this.body=body;

    }
}
