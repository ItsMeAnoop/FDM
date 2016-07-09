package com.field.datamatics.apimodels;

import android.os.Handler;

import com.field.datamatics.database.PendingRemarks;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by anoop on 5/11/15.
 */
public class VisitModel {
    private String clientno;
    private String customerid;
    private String userid;
    private String appointmentid;
    private String routplanno;
    private String visisteddate;
    private String checkintime;
    private String checkouttime;
    private String sessionendtime;
    private String feedback;
    private String geocordinate_in;
    private String geocordinate_out;
    private String geocordinate_sessout;
    private String status;//<visit=1;visit but not meet client=2;visit without appointment=3;>",
    private String signature;
    private String remarks;
    private String notes;
    private ArrayList<HashMap<String,ActivityModel>>activity;
    private ArrayList<HashMap<String,DocumentModel>>document;
    private ArrayList<HashMap<String,ReminderModel>>reminder;
    private ArrayList<HashMap<String,SurveyModel>>surveydetails;
    private ArrayList<HashMap<String,ProductQuatityModel>>sampleissued;
    private ArrayList<HashMap<String,SendPendingRemarks>>pendingRemarks;
    public VisitModel(String clientno,
            String customerid,
            String userid,String appointmentid,
            String routplanno,
            String visisteddate,
            String checkintime,
            String checkouttime,
            String sessionendtime,
            String feedback,
            String geocordinate_in,
            String geocordinate_out,
            String geocordinate_sessout,
            String status,
            String signature,
            String remarks,
            String notes,
            ArrayList<HashMap<String,ActivityModel>>activity,
            ArrayList<HashMap<String,DocumentModel>>document,
            ArrayList<HashMap<String,ReminderModel>>reminder,
            ArrayList<HashMap<String,SurveyModel>>surveydetails,
            ArrayList<HashMap<String,ProductQuatityModel>>sampleissued,
                      ArrayList<HashMap<String,SendPendingRemarks>>pendingRemarks){
                this.clientno=clientno;
                this.customerid=customerid;
                this.userid=userid;
                this.pendingRemarks=pendingRemarks;
                this.appointmentid=appointmentid;
                this.routplanno=routplanno;
                this.visisteddate=visisteddate;
                this.checkintime=checkintime;
                this.checkouttime=checkouttime;
                this.sessionendtime=sessionendtime;
                this.feedback=feedback;
                this.geocordinate_in=geocordinate_in;
                this.geocordinate_out=geocordinate_out;
                this.geocordinate_sessout=geocordinate_sessout;
                this.status=status;
                this.signature=signature;
                this.remarks=remarks;
                this.notes=notes;
                this.activity=activity;
                this.document=document;
                this.reminder=reminder;
                this.surveydetails=surveydetails;
                this.sampleissued=sampleissued;
    }
}
