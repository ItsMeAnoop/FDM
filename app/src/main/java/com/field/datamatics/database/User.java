package com.field.datamatics.database;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by Jith on 10/7/2015.
 */

@Table(databaseName = AppDatabase.NAME)
public class User extends BaseModel {

    public User(int id) {
        User_Id = id;
    }

    public User() {
    }

    @Column
    @PrimaryKey(autoincrement = true)
    public int User_Id;

    @Column
    public Integer UserNumber;

    @Column
    public String Password;

    @Column
    public String Profile;

    @Column
    public String First_Name;

    @Column
    public String Last_Name;

    @Column
    public int AddressNo;

    @Column
    public String Department;

    @Column
    public String Emailid1;

    @Column
    public String Emailid2;

    @Column
    public long Phone1;

    @Column
    public long Phone2;

    @Column
    public boolean Userstatus;

    @Column
    public boolean Status;

    @Column
    public String Geocordinat_home;

    @Column
    public String Geocordinate_office;

    @Column
    public String Remarks;

    public String getName() {
        return First_Name + " " + Last_Name;
    }

    public User(int userNumber, String password, String profile, String first_Name, String last_Name, int addressNo, String department, String emailid1, String emailid2, long phone1, long phone2, boolean userstatus, boolean status, String geocordinat_home, String geocordinate_office, String remarks) {

        UserNumber = userNumber;
        Password = password;
        Profile = profile;
        First_Name = first_Name;
        Last_Name = last_Name;
        AddressNo = addressNo;
        Department = department;
        Emailid1 = emailid1;
        Emailid2 = emailid2;
        Phone1 = phone1;
        Phone2 = phone2;
        Userstatus = userstatus;
        Status = status;
        Geocordinat_home = geocordinat_home;
        Geocordinate_office = geocordinate_office;
        Remarks = remarks;
    }
}
