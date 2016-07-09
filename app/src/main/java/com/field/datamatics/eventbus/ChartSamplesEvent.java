package com.field.datamatics.eventbus;

/**
 * Created by Jithz on 12/12/2015.
 */
public class ChartSamplesEvent {
    public final String[] labels;

    public ChartSamplesEvent(String[] labels) {
        this.labels = labels;
    }
}
