package com.field.datamatics.apimodels;

/**
 * Created by anoop on 28/10/15.
 */
public class AppoinmentResponseBody
{
    private String customerid;

    private String routplanno;

    private String Clientno;

    private String remarks;

    private String appdate;

    public String getCustomerid ()
    {
        return customerid;
    }

    public void setCustomerid (String customerid)
    {
        this.customerid = customerid;
    }

    public String getRoutplanno ()
    {
        return routplanno;
    }

    public void setRoutplanno (String routplanno)
    {
        this.routplanno = routplanno;
    }

    public String getClientno ()
    {
        return Clientno;
    }

    public void setClientno (String Clientno)
    {
        this.Clientno = Clientno;
    }

    public String getRemarks ()
    {
        return remarks;
    }

    public void setRemarks (String remarks)
    {
        this.remarks = remarks;
    }

    public String getAppdate ()
    {
        return appdate;
    }

    public void setAppdate (String appdate)
    {
        this.appdate = appdate;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [customerid = "+customerid+", routplanno = "+routplanno+", Clientno = "+Clientno+", remarks = "+remarks+", appdate = "+appdate+"]";
    }
}

