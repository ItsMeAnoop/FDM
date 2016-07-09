package com.field.datamatics.models;

/**
 * Created by anoop on 11/10/15.
 */
public class Message {
    private String message;
    private String date;
    private String sender_name;
    public Message(String message,String date,String sender_name){
        this.message=message;
        this.date=date;
        this.sender_name=sender_name;

    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSender_name() {
        return sender_name;
    }

    public void setSender_name(String sender_name) {
        this.sender_name = sender_name;
    }


}
