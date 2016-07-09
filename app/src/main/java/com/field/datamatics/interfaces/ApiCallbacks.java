package com.field.datamatics.interfaces;

import java.util.Objects;

/**
 * Created by anoop on 15/10/15.
 */
public interface ApiCallbacks {
    public void onSuccess(Object objects);
    public void onError(Object objects);
    public void onErrorMessage(String message);
}
