package com.field.datamatics.apimodels;

/**
 * Created by anoop on 28/10/15.
 */
public class WorkCalanderResponseBody
{
    private String totime;

    private String customerid;

    private String clientno;

    private String clientfirstname;

    private String fromtime;

    private String workcalenderid;

    private String remarks;

    private String createddate;

    private String customername;

    private String availabledays;

    public String getTotime ()
    {
        return totime;
    }

    public void setTotime (String totime)
    {
        this.totime = totime;
    }

    public String getCustomerid ()
    {
        return customerid;
    }

    public void setCustomerid (String customerid)
    {
        this.customerid = customerid;
    }

    public String getClientno ()
    {
        return clientno;
    }

    public void setClientno (String clientno)
    {
        this.clientno = clientno;
    }

    public String getClientfirstname ()
    {
        return clientfirstname;
    }

    public void setClientfirstname (String clientfirstname)
    {
        this.clientfirstname = clientfirstname;
    }

    public String getFromtime ()
    {
        return fromtime;
    }

    public void setFromtime (String fromtime)
    {
        this.fromtime = fromtime;
    }

    public String getWorkcalenderid ()
    {
        return workcalenderid;
    }

    public void setWorkcalenderid (String workcalenderid)
    {
        this.workcalenderid = workcalenderid;
    }

    public String getRemarks ()
    {
        return remarks;
    }

    public void setRemarks (String remarks)
    {
        this.remarks = remarks;
    }

    public String getCreateddate ()
    {
        return createddate;
    }

    public void setCreateddate (String createddate)
    {
        this.createddate = createddate;
    }

    public String getCustomername ()
    {
        return customername;
    }

    public void setCustomername (String customername)
    {
        this.customername = customername;
    }

    public String getAvailabledays ()
    {
        return availabledays;
    }

    public void setAvailabledays (String availabledays)
    {
        this.availabledays = availabledays;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [totime = "+totime+", customerid = "+customerid+", clientno = "+clientno+", clientfirstname = "+clientfirstname+", fromtime = "+fromtime+", workcalenderid = "+workcalenderid+", remarks = "+remarks+", createddate = "+createddate+", customername = "+customername+", availabledays = "+availabledays+"]";
    }
}