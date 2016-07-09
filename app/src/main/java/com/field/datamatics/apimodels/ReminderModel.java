package com.field.datamatics.apimodels;

/**
 * Created by anoop on 5/11/15.
 */
public class ReminderModel {


    private String pkeyid;
    private String date;
    private String message;
    private String status;
    private String remarks;
    private String starttime;
    private String endtime;
    private String startcordinates;
    private String endcordinates;
    public ReminderModel(String pkeyid,
            String date,
            String message,
            String status,
            String remarks,
            String starttime,
            String endtime,
            String startcordinates,
            String endcordinates){
        this.pkeyid=pkeyid;
        this.date=date;
        this.message=message;
        this.status=status;
        this.remarks=remarks;
        this. starttime=starttime;
        this. endtime=endtime;
        this. startcordinates=startcordinates;
        this. endcordinates=endcordinates;
    }
    public String getPkeyid() {
        return pkeyid;
    }

    public void setPkeyid(String pkeyid) {
        this.pkeyid = pkeyid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public String getStartcordinates() {
        return startcordinates;
    }

    public void setStartcordinates(String startcordinates) {
        this.startcordinates = startcordinates;
    }

    public String getEndcordinates() {
        return endcordinates;
    }

    public void setEndcordinates(String endcordinates) {
        this.endcordinates = endcordinates;
    }
}
