package com.field.datamatics.apimodels;

/**
 * Created by anoop on 28/10/15.
 */
public class WorkCalanderResponse
{
    private WorkCalanderResponseBody[] body;

    private String status;

    public WorkCalanderResponseBody[] getBody ()
    {
        return body;
    }

    public void setBody (WorkCalanderResponseBody[] body)
    {
        this.body = body;
    }

    public String getStatus ()
    {
        return status;
    }

    public void setStatus (String status)
    {
        this.status = status;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [body = "+body+", status = "+status+"]";
    }
}
