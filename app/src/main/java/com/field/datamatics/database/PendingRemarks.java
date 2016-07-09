package com.field.datamatics.database;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by Anoop on 29-05-2016.
 */
@Table(databaseName = AppDatabase.NAME)
public class PendingRemarks extends BaseModel {
    @Column
    @PrimaryKey(autoincrement = true)
    public int remark_id;
    @Column
    public int routeplanno;
    @Column
    public String datetime;
    @Column
    public String remarks;
    @Column
    public String client_ID;

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

    public int getRemark_id() {
        return remark_id;
    }

    public void setRemark_id(int remark_id) {
        this.remark_id = remark_id;
    }
}
