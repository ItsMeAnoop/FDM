package com.field.datamatics.apimodels;

/**
 * Created by anoop on 5/11/15.
 */
public class SurveyModel {
    private String surveyid;
    private String routplanno;
    private String visiteddate;
    private String date;
    private String selectedoption;
    private String remarks;
    private String option_yorno;
    private String narration;
    private String starttime;
    private String endtime;
    private String startcordinates;
    private String endcordinates;
    public SurveyModel(String surveyid,
            String routplanno,
            String visiteddate,
            String date,
            String selectedoption,
            String remarks,
            String option_yorno,
            String narration,
            String starttime,
            String endtime,
            String startcordinates,
            String endcordinates){
        this.surveyid=surveyid;
        this.routplanno=routplanno;
        this.visiteddate=visiteddate;
        this.date=date;
        this.selectedoption=selectedoption;
        this.remarks=remarks;
        this. option_yorno=option_yorno;
        this. narration=narration;
        this. starttime=starttime;
        this. endtime=endtime;
        this. startcordinates=startcordinates;
        this. endcordinates=endcordinates;
    }
    public String getSurveyid() {
        return surveyid;
    }

    public void setSurveyid(String surveyid) {
        this.surveyid = surveyid;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSelectedoption() {
        return selectedoption;
    }

    public void setSelectedoption(String selectedoption) {
        this.selectedoption = selectedoption;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getOption_yorno() {
        return option_yorno;
    }

    public void setOption_yorno(String option_yorno) {
        this.option_yorno = option_yorno;
    }

    public String getNarration() {
        return narration;
    }

    public void setNarration(String narration) {
        this.narration = narration;
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

    @Override
    public String toString() {
        return "SurveyModel{" +
                "surveyid='" + surveyid + '\'' +
                ", routplanno='" + routplanno + '\'' +
                ", visiteddate='" + visiteddate + '\'' +
                ", date='" + date + '\'' +
                ", selectedoption='" + selectedoption + '\'' +
                ", remarks='" + remarks + '\'' +
                '}';
    }
}
