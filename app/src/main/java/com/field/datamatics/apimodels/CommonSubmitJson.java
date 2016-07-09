package com.field.datamatics.apimodels;

import com.field.datamatics.constants.ApiConstants;

import java.util.Objects;

/**
 * Created by USER on 11/14/2015.
 */
public class CommonSubmitJson {
    private Object json;
    private String encription_key;
    public CommonSubmitJson(Object json){
        this.json=json;
        encription_key= ApiConstants.ENCRYPTION_KEY;
    }

    public Object getJson() {
        return json;
    }

    public void setJson(Object json) {
        this.json = json;
    }

    public String getEncription_key() {
        return encription_key;
    }

    public void setEncription_key(String encription_key) {
        this.encription_key = encription_key;
    }
}
