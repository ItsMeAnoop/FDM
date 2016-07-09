package com.field.datamatics.apimodels;

/**
 * Created by anoop on 8/11/15.
 */
public class RoutePlanSubmit {
    private String date;
    private String clientno;
    private String customerid;
    private String userid;
    private String workcalanderid;
    private String preparedby;
    private String prepareduser;
    private String visittype;//":  "<default=0;visit=1;pending=2;visit after pending=3>"
    private String createddate;
    private String remarks;
    private String status;//": ""<1=current visit;2=based on previous visit>
    public RoutePlanSubmit(String date,
            String clientno,
            String customerid,
            String userid,
            String workcalanderid,
            String preparedby,
            String prepareduser,
            String visittype,
            String createddate,
            String remarks,
            String status){
        this.date=date;
        this.clientno=clientno;
        this.customerid=customerid;
        this.userid=userid;
        this.workcalanderid=workcalanderid;
        this.preparedby=preparedby;
        this.prepareduser=prepareduser;
        this.visittype=visittype;
        this.createddate=createddate;
        this.remarks=remarks;
        this.status=status;

    }



    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getWorkcalanderid() {
        return workcalanderid;
    }

    public void setWorkcalanderid(String workcalanderid) {
        this.workcalanderid = workcalanderid;
    }

    public String getPreparedby() {
        return preparedby;
    }

    public void setPreparedby(String preparedby) {
        this.preparedby = preparedby;
    }

    public String getPrepareduser() {
        return prepareduser;
    }

    public void setPrepareduser(String prepareduser) {
        this.prepareduser = prepareduser;
    }

    public String getVisittype() {
        return visittype;
    }

    public void setVisittype(String visittype) {
        this.visittype = visittype;
    }

    public String getCreateddate() {
        return createddate;
    }

    public void setCreateddate(String createddate) {
        this.createddate = createddate;
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
