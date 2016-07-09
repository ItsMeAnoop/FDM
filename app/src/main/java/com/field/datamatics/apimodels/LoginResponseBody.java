package com.field.datamatics.apimodels;

/**
 * Created by anoop on 28/10/15.
 */
public class LoginResponseBody
{
    private String sex;

    private String usercode;

    private String status;

    private String department;

    private String emailid1;

    private String userid;

    private String emailid2;

    private String lastname;

    private String remarks;

    private String phone2;

    private String firstname;

    private String phone1;

    private String geocordinatehome;

    private String username;

    private String geocordinateoffice;

    private String addressno;

    private String userstatus;

    private String profile;

    public String getSex ()
    {
        return sex;
    }

    public void setSex (String sex)
    {
        this.sex = sex;
    }

    public String getUsercode ()
    {
        return usercode;
    }

    public void setUsercode (String usercode)
    {
        this.usercode = usercode;
    }

    public String getStatus ()
    {
        return status;
    }

    public void setStatus (String status)
    {
        this.status = status;
    }

    public String getDepartment ()
    {
        return department;
    }

    public void setDepartment (String department)
    {
        this.department = department;
    }

    public String getEmailid1 ()
    {
        return emailid1;
    }

    public void setEmailid1 (String emailid1)
    {
        this.emailid1 = emailid1;
    }

    public String getUserid ()
    {
        return userid;
    }

    public void setUserid (String userid)
    {
        this.userid = userid;
    }

    public String getEmailid2 ()
    {
        return emailid2;
    }

    public void setEmailid2 (String emailid2)
    {
        this.emailid2 = emailid2;
    }

    public String getLastname ()
    {
        return lastname;
    }

    public void setLastname (String lastname)
    {
        this.lastname = lastname;
    }

    public String getRemarks ()
    {
        return remarks;
    }

    public void setRemarks (String remarks)
    {
        this.remarks = remarks;
    }

    public String getPhone2 ()
    {
        return phone2;
    }

    public void setPhone2 (String phone2)
    {
        this.phone2 = phone2;
    }

    public String getFirstname ()
    {
        return firstname;
    }

    public void setFirstname (String firstname)
    {
        this.firstname = firstname;
    }

    public String getPhone1 ()
    {
        return phone1;
    }

    public void setPhone1 (String phone1)
    {
        this.phone1 = phone1;
    }

    public String getGeocordinatehome ()
    {
        return geocordinatehome;
    }

    public void setGeocordinatehome (String geocordinatehome)
    {
        this.geocordinatehome = geocordinatehome;
    }

    public String getUsername ()
    {
        return username;
    }

    public void setUsername (String username)
    {
        this.username = username;
    }

    public String getGeocordinateoffice ()
    {
        return geocordinateoffice;
    }

    public void setGeocordinateoffice (String geocordinateoffice)
    {
        this.geocordinateoffice = geocordinateoffice;
    }

    public String getAddressno ()
    {
        return addressno;
    }

    public void setAddressno (String addressno)
    {
        this.addressno = addressno;
    }

    public String getUserstatus ()
    {
        return userstatus;
    }

    public void setUserstatus (String userstatus)
    {
        this.userstatus = userstatus;
    }

    public String getProfile ()
    {
        return profile;
    }

    public void setProfile (String profile)
    {
        this.profile = profile;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [sex = "+sex+", usercode = "+usercode+", status = "+status+", department = "+department+", emailid1 = "+emailid1+", userid = "+userid+", emailid2 = "+emailid2+", lastname = "+lastname+", remarks = "+remarks+", phone2 = "+phone2+", firstname = "+firstname+", phone1 = "+phone1+", geocordinatehome = "+geocordinatehome+", username = "+username+", geocordinateoffice = "+geocordinateoffice+", addressno = "+addressno+", userstatus = "+userstatus+", profile = "+profile+"]";
    }
}
