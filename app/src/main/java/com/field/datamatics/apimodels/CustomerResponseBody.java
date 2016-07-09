package com.field.datamatics.apimodels;

/**
 * Created by anoop on 28/10/15.
 */
public class CustomerResponseBody
{
    private String region;

    private String remarks_customer;

    private String geocordinates;

    private String customerid;

    private String status;

    private String location;

    private String street;

    private String address1;

    private String po;

    private String customergroup;

    private String customername;

    private String country;

    public String getRegion ()
    {
        return region;
    }

    public void setRegion (String region)
    {
        this.region = region;
    }

    public String getRemarks_customer ()
    {
        return remarks_customer;
    }

    public void setRemarks_customer (String remarks_customer)
    {
        this.remarks_customer = remarks_customer;
    }

    public String getGeocordinates ()
    {
        return geocordinates;
    }

    public void setGeocordinates (String geocordinates)
    {
        this.geocordinates = geocordinates;
    }

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

    public String getLocation ()
    {
        return location;
    }

    public void setLocation (String location)
    {
        this.location = location;
    }

    public String getStreet ()
    {
        return street;
    }

    public void setStreet (String street)
    {
        this.street = street;
    }

    public String getAddress1 ()
    {
        return address1;
    }

    public void setAddress1 (String address1)
    {
        this.address1 = address1;
    }

    public String getPo ()
    {
        return po;
    }

    public void setPo (String po)
    {
        this.po = po;
    }

    public String getCustomergroup ()
    {
        return customergroup;
    }

    public void setCustomergroup (String customergroup)
    {
        this.customergroup = customergroup;
    }

    public String getCustomername ()
    {
        return customername;
    }

    public void setCustomername (String customername)
    {
        this.customername = customername;
    }

    public String getCountry ()
    {
        return country;
    }

    public void setCountry (String country)
    {
        this.country = country;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [region = "+region+", remarks_customer = "+remarks_customer+", geocordinates = "+geocordinates+", customerid = "+customerid+", status = "+status+", location = "+location+", street = "+street+", address1 = "+address1+", po = "+po+", customergroup = "+customergroup+", customername = "+customername+", country = "+country+"]";
    }
}
