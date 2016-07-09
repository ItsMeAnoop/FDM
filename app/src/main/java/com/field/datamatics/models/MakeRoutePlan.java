package com.field.datamatics.models;

import com.field.datamatics.database.Client;
import com.field.datamatics.database.Client_work_cal;
import com.field.datamatics.database.Customer;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;

/**
 * Created by anoop on 12/10/15.
 */
public class MakeRoutePlan {
    private boolean isSelect;

    public Client_work_cal getClient_work_cal() {
        return client_work_cal;
    }

    public void setClient_work_cal(Client_work_cal client_work_cal) {
        this.client_work_cal = client_work_cal;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    private Client_work_cal client_work_cal;
    private Client client;
    private Customer customer;
    public MakeRoutePlan(boolean isSelect,Client_work_cal client_work_cal,Client client,
                         Customer customer){
        this.client_work_cal=client_work_cal;
        this.client=client;
        this.customer=customer;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setIsSelect(boolean isSelect) {
        this.isSelect = isSelect;
    }
}
