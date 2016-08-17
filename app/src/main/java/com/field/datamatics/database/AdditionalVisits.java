package com.field.datamatics.database;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by Anoop on 07-08-2016.
 */
@Table(databaseName = AppDatabase.NAME)
public class AdditionalVisits extends BaseModel {
    @Column
    @PrimaryKey(autoincrement = true)
    public int id;
    @Column
    public String visitdate;
    @Column
    public String clientName;
    @Column
    public String speciality;
    @Column
    public String time_availability;
    @Column
    public String location;
    @Column
    public String customer_name;
    @Column
    public String gender;
    @Column
    public String marketClass;
    @Column
    public String steClass;
    @Column
    public String email;
    @Column
    public String phone;
    @Column
    public String mob;
    @Column
    public String fax;
    @Column
    public String type;
    @Column
    public String nationality;



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getVisitdate() {
        return visitdate;
    }

    public void setVisitdate(String visitdate) {
        this.visitdate = visitdate;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public String getTime_availability() {
        return time_availability;
    }

    public void setTime_availability(String time_availability) {
        this.time_availability = time_availability;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMarketClass() {
        return marketClass;
    }

    public void setMarketClass(String marketClass) {
        this.marketClass = marketClass;
    }

    public String getSteClass() {
        return steClass;
    }

    public void setSteClass(String steClass) {
        this.steClass = steClass;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMob() {
        return mob;
    }

    public void setMob(String mob) {
        this.mob = mob;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }
}
