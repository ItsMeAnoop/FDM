package com.field.datamatics.apimodels;

/**
 * Created by anoop on 28/10/15.
 */
public class ClientResponse
{
    private ClientResponseBody[] body;

    private String status;

    private ClientResponseBodyOne[] body1;

    public ClientResponseBody[] getBody ()
    {
        return body;
    }

    public void setBody (ClientResponseBody[] body)
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

    public ClientResponseBodyOne[] getBody1 ()
    {
        return body1;
    }

    public void setBody1 (ClientResponseBodyOne[] body1)
    {
        this.body1 = body1;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [body = "+body+", status = "+status+", body1 = "+body1+"]";
    }
}


