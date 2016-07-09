package com.field.datamatics.models;

/**
 * Created by anoop on 11/10/15.
 */
public class Appointment {

    private String appointment;
    private String date;

    public Appointment(String appointment,
                       String date) {
        this.appointment = appointment;
        this.date = date;

    }
    public String getAppointment() {
        return appointment;
    }

    public void setAppointment(String appointment) {
        this.appointment = appointment;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
