package com.field.datamatics.apimodels;

/**
 * Created by anoop on 28/10/15.
 */
public class ClientproductResponseBody
{
    private String customerid;

    private String status;

    private String clproductid;

    private String clientno;

    private String productid;

    private String remarks;

    private String expirydate;

    public String getCustomerid ()
    {
        return customerid;
    }

    public void setCustomerid (String customerid)
    {
        this.customerid = customerid;
    }

    public String getStatus ()
    {
        return status;
    }

    public void setStatus (String status)
    {
        this.status = status;
    }

    public String getClproductid ()
    {
        return clproductid;
    }

    public void setClproductid (String clproductid)
    {
        this.clproductid = clproductid;
    }

    public String getClientno ()
    {
        return clientno;
    }

    public void setClientno (String clientno)
    {
        this.clientno = clientno;
    }

    public String getProductid ()
    {
        return productid;
    }

    public void setProductid (String productid)
    {
        this.productid = productid;
    }

    public String getRemarks ()
    {
        return remarks;
    }

    public void setRemarks (String remarks)
    {
        this.remarks = remarks;
    }

    public String getExpirydate ()
    {
        return expirydate;
    }

    public void setExpirydate (String expirydate)
    {
        this.expirydate = expirydate;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [customerid = "+customerid+", status = "+status+", clproductid = "+clproductid+", clientno = "+clientno+", productid = "+productid+", remarks = "+remarks+", expirydate = "+expirydate+"]";
    }
}