package com.field.datamatics.apimodels;

/**
 * Created by USER on 11/15/2015.
 */
public class ClientResponseBodyOne {
    private String customerid;

    private String client_number;
    private String rowNo;

    public String getRowNo() {
        return rowNo;
    }

    public void setRowNo(String rowNo) {
        this.rowNo = rowNo;
    }

    public String getCustomerid ()
    {
        return customerid;
    }

    public void setCustomerid (String customerid)
    {
        this.customerid = customerid;
    }

    public String getClient_number ()
    {
        return client_number;
    }

    public void setClient_number (String client_number)
    {
        this.client_number = client_number;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [customerid = "+customerid+", client_number = "+client_number+"]";
    }
}
