package com.field.datamatics.apimodels;

/**
 * Created by anoop on 28/10/15.
 */
public class ClientResponseBody
{
    private String clientgender;

    private String clientfax;

    private String status;

    private String clientfirstname;

    private String clientlastname;

    private String clientprefix;

    private String remarks;

    private String visit;

    private String clientphone;

    private String type;

    private String accountmanager;

    private String nationality;

    private String clientmobile;

    private String clientemail;

    private String customerid;

    private String client_number;

    private String addressnumber;

    private String steclass;

    private String marketclass;

    private String specialty;

    public String getClientgender ()
    {
        return clientgender;
    }

    public void setClientgender (String clientgender)
    {
        this.clientgender = clientgender;
    }

    public String getClientfax ()
    {
        return clientfax;
    }

    public void setClientfax (String clientfax)
    {
        this.clientfax = clientfax;
    }

    public String getStatus ()
    {
        return status;
    }

    public void setStatus (String status)
    {
        this.status = status;
    }

    public String getClientfirstname ()
    {
        return clientfirstname;
    }

    public void setClientfirstname (String clientfirstname)
    {
        this.clientfirstname = clientfirstname;
    }

    public String getClientlastname ()
    {
        return clientlastname;
    }

    public void setClientlastname (String clientlastname)
    {
        this.clientlastname = clientlastname;
    }

    public String getClientprefix ()
    {
        return clientprefix;
    }

    public void setClientprefix (String clientprefix)
    {
        this.clientprefix = clientprefix;
    }

    public String getRemarks ()
    {
        return remarks;
    }

    public void setRemarks (String remarks)
    {
        this.remarks = remarks;
    }

    public String getVisit ()
    {
        return visit;
    }

    public void setVisit (String visit)
    {
        this.visit = visit;
    }

    public String getClientphone ()
    {
        return clientphone;
    }

    public void setClientphone (String clientphone)
    {
        this.clientphone = clientphone;
    }

    public String getType ()
    {
        return type;
    }

    public void setType (String type)
    {
        this.type = type;
    }

    public String getAccountmanager ()
    {
        return accountmanager;
    }

    public void setAccountmanager (String accountmanager)
    {
        this.accountmanager = accountmanager;
    }

    public String getNationality ()
    {
        return nationality;
    }

    public void setNationality (String nationality)
    {
        this.nationality = nationality;
    }

    public String getClientmobile ()
    {
        return clientmobile;
    }

    public void setClientmobile (String clientmobile)
    {
        this.clientmobile = clientmobile;
    }

    public String getClientemail ()
    {
        return clientemail;
    }

    public void setClientemail (String clientemail)
    {
        this.clientemail = clientemail;
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

    public String getAddressnumber ()
    {
        return addressnumber;
    }

    public void setAddressnumber (String addressnumber)
    {
        this.addressnumber = addressnumber;
    }

    public String getSteclass ()
    {
        return steclass;
    }

    public void setSteclass (String steclass)
    {
        this.steclass = steclass;
    }

    public String getMarketclass ()
    {
        return marketclass;
    }

    public void setMarketclass (String marketclass)
    {
        this.marketclass = marketclass;
    }

    public String getSpecialty ()
    {
        return specialty;
    }

    public void setSpecialty (String specialty)
    {
        this.specialty = specialty;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [clientgender = "+clientgender+", clientfax = "+clientfax+", status = "+status+", clientfirstname = "+clientfirstname+", clientlastname = "+clientlastname+", clientprefix = "+clientprefix+", remarks = "+remarks+", visit = "+visit+", clientphone = "+clientphone+", type = "+type+", accountmanager = "+accountmanager+", nationality = "+nationality+", clientmobile = "+clientmobile+", clientemail = "+clientemail+", customerid = "+customerid+", client_number = "+client_number+", addressnumber = "+addressnumber+", steclass = "+steclass+", marketclass = "+marketclass+", specialty = "+specialty+"]";
    }
}
