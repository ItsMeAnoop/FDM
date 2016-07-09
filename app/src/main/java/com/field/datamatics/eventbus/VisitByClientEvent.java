package com.field.datamatics.eventbus;

/**
 * Created by Jithz on 12/12/2015.
 */
public class VisitByClientEvent {

    public String[] prefixes;

    public VisitByClientEvent(String[] prefixes) {
        this.prefixes = prefixes;
    }
}
