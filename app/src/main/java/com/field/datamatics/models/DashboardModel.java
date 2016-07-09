package com.field.datamatics.models;

/**
 * Created by anoop on 7/10/15.
 */
public class DashboardModel {
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    private String name;
    private int icon;
    public DashboardModel(String name,int icon){
        this.name=name;
        this.icon=icon;
    }
}
