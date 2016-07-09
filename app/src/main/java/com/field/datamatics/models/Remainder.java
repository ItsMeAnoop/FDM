package com.field.datamatics.models;

/**
 * Created by anoop on 11/10/15.
 */
public class Remainder {
    private String remainder;
    private String date;
    public Remainder(String remainder,
                     String date){
        this.remainder=remainder;
        this.date=date;

    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getRemainder() {
        return remainder;
    }

    public void setRemainder(String remainder) {
        this.remainder = remainder;
    }


}
