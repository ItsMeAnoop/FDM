package com.field.datamatics.apimodels;

/**
 * Created by anoop on 28/10/15.
 */
public class ReminderResponseBody
{
    private String message;

    private String status;

    private String routplanno;

    private String remarks;

    private String pkeyid;

    private String date;

    public String getMessage ()
    {
        return message;
    }

    public void setMessage (String message)
    {
        this.message = message;
    }

    public String getStatus ()
    {
        return status;
    }

    public void setStatus (String status)
    {
        this.status = status;
    }

    public String getRoutplanno ()
    {
        return routplanno;
    }

    public void setRoutplanno (String routplanno)
    {
        this.routplanno = routplanno;
    }

    public String getRemarks ()
    {
        return remarks;
    }

    public void setRemarks (String remarks)
    {
        this.remarks = remarks;
    }

    public String getPkeyid ()
    {
        return pkeyid;
    }

    public void setPkeyid (String pkeyid)
    {
        this.pkeyid = pkeyid;
    }

    public String getDate ()
    {
        return date;
    }

    public void setDate (String date)
    {
        this.date = date;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [message = "+message+", status = "+status+", routplanno = "+routplanno+", remarks = "+remarks+", pkeyid = "+pkeyid+", date = "+date+"]";
    }
}