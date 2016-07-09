package com.field.datamatics.apimodels;

import java.util.ArrayList;

/**
 * Created by anoop on 8/11/15.
 */
public class MessageJson {
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<Message> getBody() {
        return body;
    }

    public void setBody(ArrayList<Message> body) {
        this.body = body;
    }

    private String status;
    private ArrayList<Message>body;
    public MessageJson(String status,
            ArrayList<Message>body){
        this.body=body;
        this.status=status;
    }
}
