package com.field.datamatics.apimodels;

/**
 * Created by anoop on 5/11/15.
 */
public class DocumentModel {
    private String visiteddate;//": " visited date same as above visited date so you need not send and also routplanno ",
    private String routplanno;
    private String date;
    private String docpath;
    private String doctype;//"<pdf=1;image=2;video/audio=3;>",
    private String remarks;
    private String starttime;
    private String endtime;
    private String startcordinates;
    private String endcordinates;
    public DocumentModel(String visiteddate,
            String routplanno,
            String date,
            String docpath,
            String doctype,
            String remarks,
            String starttime,
            String endtime,
            String startcordinates,
            String endcordinates){
        this.visiteddate=visiteddate;
        this.routplanno=routplanno;
        this.date=date;
        this.docpath=docpath;
        this.doctype=doctype;
        this.remarks=remarks;
        this.starttime=starttime;
        this.endtime=endtime;
        this.startcordinates=startcordinates;
        this.endcordinates=endcordinates;

    }
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getVisiteddate() {
        return visiteddate;
    }

    public void setVisiteddate(String visiteddate) {
        this.visiteddate = visiteddate;
    }

    public String getRoutplanno() {
        return routplanno;
    }

    public void setRoutplanno(String routplanno) {
        this.routplanno = routplanno;
    }

    public String getDocpath() {
        return docpath;
    }

    public void setDocpath(String docpath) {
        this.docpath = docpath;
    }

    public String getDoctype() {
        return doctype;
    }

    public void setDoctype(String doctype) {
        this.doctype = doctype;
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
