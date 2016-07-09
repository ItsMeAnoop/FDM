package com.field.datamatics.apimodels;

/**
 * Created by anoop on 8/11/15.
 */
public class Message {
    private String userid;
    private String date;
    private String message;
    private String pkey;//":  "<other table pkey, appointment, visit>"
    private String status;//:  "<appointment=1,visit=2,others=3>"
    private String clientno;
    private String customerid;
    private String remarks;
    public Message( String userid,
             String date,
             String message,
             String pkey,
             String status,
             String clientno,
             String customerid,
             String remarks){
         this.userid=userid;
        this.date=date;
        this.message=message;
        this.pkey=pkey;
        this.status=status;
        this.clientno=clientno;
        this.customerid=customerid;
        this.remarks=remarks;
    }
    public String getPkey() {
        return pkey;
    }

    public void setPkey(String pkey) {
        this.pkey = pkey;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
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

    public String getClientno() {
        return clientno;
    }

    public void setClientno(String clientno) {
        this.clientno = clientno;
    }

    public String getCustomerid() {
        return customerid;
    }

    public void setCustomerid(String customerid) {
        this.customerid = customerid;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
