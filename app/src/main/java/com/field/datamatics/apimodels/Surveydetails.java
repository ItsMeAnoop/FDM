package com.field.datamatics.apimodels;

/**
 * Created by anoop on 8/11/15.
 */
public class Surveydetails {
    private String surveyid;
    private String question;
    private String surveynumber;
    private String option3;
    private String option4;
    private String option1;
    private String option2;
    private String remarks;
    private String status;
    private String dateto;
    private String datefrom;
    public Surveydetails(String surveyid,
            String question,
            String option3,
            String option4,
            String option1,
            String option2,
            String remarks,
            String status,
            String surveynumber,
            String dateto,
            String datefrom){
        this.surveyid=surveyid;
        this.question=question;
        this.option3=option3;
        this.option4=option4;
        this.option1=option1;
        this.option2=option2;
        this.remarks=remarks;
        this.status=status;//<1=option question,2=yes or no question>",
        this.surveynumber=surveynumber;
        this.dateto=dateto;
        this.datefrom=datefrom;
    }

    public String getSurveynumber() {
        return surveynumber;
    }

    public void setSurveynumber(String surveynumber) {
        this.surveynumber = surveynumber;
    }

    public String getDateto() {
        return dateto;
    }

    public void setDateto(String dateto) {
        this.dateto = dateto;
    }

    public String getDatefrom() {
        return datefrom;
    }

    public void setDatefrom(String datefrom) {
        this.datefrom = datefrom;
    }

    public String getSurveyid() {
        return surveyid;
    }

    public void setSurveyid(String surveyid) {
        this.surveyid = surveyid;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getOption3() {
        return option3;
    }

    public void setOption3(String option3) {
        this.option3 = option3;
    }

    public String getOption4() {
        return option4;
    }

    public void setOption4(String option4) {
        this.option4 = option4;
    }

    public String getOption1() {
        return option1;
    }

    public void setOption1(String option1) {
        this.option1 = option1;
    }

    public String getOption2() {
        return option2;
    }

    public void setOption2(String option2) {
        this.option2 = option2;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
