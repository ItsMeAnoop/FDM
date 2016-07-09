package com.field.datamatics.apimodels;

/**
 * Created by anoop on 28/10/15.
 */
public class RegionResponseBody
{
    private String regionid;
    private String reporting_manager;
    private String region;
    private String userid;
    private String remarks;
    private String date_from;
    private String date_to;

    public String getDate_from() {
        return date_from;
    }

    public void setDate_from(String date_from) {
        this.date_from = date_from;
    }

    public String getDate_to() {
        return date_to;
    }

    public void setDate_to(String date_to) {
        this.date_to = date_to;
    }

    public String getRegionid() {
        return regionid;
    }

    public void setRegionid(String regionid) {
        this.regionid = regionid;
    }

    public String getReporting_manager() {
        return reporting_manager;
    }

    public void setReporting_manager(String reporting_manager) {
        this.reporting_manager = reporting_manager;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
