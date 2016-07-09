package com.field.datamatics.apimodels;

/**
 * Created by anoop on 5/11/15.
 */
public class ActivityModel {
    private String activitydate;
    private String routplanno;
    private String visiteddate;
    private String activityname;
    private String activity;//<pdf=1;image=2;video:3;ppt:4>
    private String starttime;
    private String endtime;
    private String remarks;
    private String startcordinates;
    private String endcordinates;
    public ActivityModel(String activitydate,
             String routplanno,
             String visiteddate,
             String activityname,
             String activity,
             String starttime,
             String endtime,
             String remarks,
             String startcordinates,
             String endcordinates){
        this.activitydate=activitydate;
        this.routplanno=routplanno;
        this.visiteddate=visiteddate;
        this.activityname=activityname;
        this.activity=activity;
        this.starttime=starttime;
        this.endtime=endtime;
        this.remarks=remarks;
        this.startcordinates=startcordinates;
        this.endcordinates=endcordinates;

    }
    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public String getActivitydate() {
        return activitydate;
    }

    public void setActivitydate(String activitydate) {
        this.activitydate = activitydate;
    }

    public String getRoutplanno() {
        return routplanno;
    }

    public void setRoutplanno(String routplanno) {
        this.routplanno = routplanno;
    }

    public String getVisiteddate() {
        return visiteddate;
    }

    public void setVisiteddate(String visiteddate) {
        this.visiteddate = visiteddate;
    }

    public String getActivityname() {
        return activityname;
    }

    public void setActivityname(String activityname) {
        this.activityname = activityname;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
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
