package com.field.datamatics.database;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.QueryModel;
import com.raizlabs.android.dbflow.structure.BaseQueryModel;

/**
 * Created by Jith on 10/18/2015.
 */
@QueryModel(databaseName = AppDatabase.NAME)
public class JoinClientRoutePlan extends BaseQueryModel {
    @Column
    public int Client_Number;

    @Column
    public long Address_Number_JDE;

    @Column
    public String Client_Prefix;

    @Column
    public String Client_First_Name;

    @Column
    public String Client_Last_Name;

    @Column
    public String Client_Gender;

    @Column
    public String Client_Email;

    @Column
    public long Client_Phone;

    @Column
    public long Client_Mobile;

    @Column
    public String Client_Fax;

    @Column
    public String Speciality;

    @Column
    public String Marketclass;

    @Column
    public String STEclass;

    @Column
    public String Type;

    @Column
    public String Nationality;

    @Column
    public boolean Status;

    @Column
    public boolean Visit;

    @Column
    public String Account_Manager;

    @Column
    public String Remarks;
    @Column
    public int Route_Plan_Number;
    @Column
    public int Appointment_Id;

    @Column
    public String CustomerName;
    @Column
    public String Region;

    @Column
    public String RoutePlanDate;

    @Column
    public String VisitedDate;

    @Column
    public String CheckInTime;

    @Column
    public String CheckOutTime;

    @Column
    public String SessionEnd;

    @Column
    public String Feedback;

    @Column
    public int routeNumber;
    @Column
    public String Location;
}
