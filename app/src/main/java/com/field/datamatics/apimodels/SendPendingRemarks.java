package com.field.datamatics.apimodels;

import com.raizlabs.android.dbflow.annotation.Column;

/**
 * Created by Anoop on 14-06-2016.
 */
public class SendPendingRemarks {
    private int remark_id;
    private int routeplanno;
    private String datetime;
    private String remarks;
    private String client_ID;
    public SendPendingRemarks(int remark_id,
            int routeplanno,
            String datetime,
            String remarks,
            String client_ID){
        this.remark_id=remark_id;
        this.routeplanno=routeplanno;
        this.datetime=datetime;
        this.remarks=remarks;
        this.client_ID=client_ID;
            }

    public int getRemark_id() {
        return remark_id;
    }

    public void setRemark_id(int remark_id) {
        this.remark_id = remark_id;
    }

    public int getRouteplanno() {
        return routeplanno;
    }

    public void setRouteplanno(int routeplanno) {
        this.routeplanno = routeplanno;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getClient_ID() {
        return client_ID;
    }

    public void setClient_ID(String client_ID) {
        this.client_ID = client_ID;
    }
}
