package com.field.datamatics.json;

import android.os.Bundle;

/**
 * Created by Jithz on 1/2/2016.
 */
public class VisitHomePageJson {
    public int routePlanNumber;
    public int appointmentId;
    public int clientId;
    public String checkInTime;
    public String checkoutTime;
    public String checkinCoordinates;
    public String checkoutCoordinates;
    public boolean isPending;

    public VisitHomePageJson(int routePlanNumber, int appointmentId, int clientId, String checkInTime, String checkoutTime, String checkinCoordinates, String checkoutCoordinates, boolean isPending) {
        this.routePlanNumber = routePlanNumber;
        this.appointmentId = appointmentId;
        this.clientId = clientId;
        this.checkInTime = checkInTime;
        this.checkoutTime = checkoutTime;
        this.checkinCoordinates = checkinCoordinates;
        this.checkoutCoordinates = checkoutCoordinates;
        this.isPending = isPending;
    }
}
