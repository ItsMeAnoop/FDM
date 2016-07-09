package com.field.datamatics.eventbus;

/**
 * Created by Jithz on 12/12/2015.
 */
public class ReminderEvent {
    public final String message;

    public ReminderEvent(String message) {
        this.message = message;
    }
}
