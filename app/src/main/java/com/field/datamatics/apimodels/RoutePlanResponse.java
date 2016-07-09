package com.field.datamatics.apimodels;

/**
 * Created by anoop on 28/10/15.
 */
public class RoutePlanResponse
{
    private RoutePlanResponseBody[] body;

    private String status;

    public RoutePlanResponseBody[] getBody ()
    {
        return body;
    }

    public void setBody (RoutePlanResponseBody[] body)
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