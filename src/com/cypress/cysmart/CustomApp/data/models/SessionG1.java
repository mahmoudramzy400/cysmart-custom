package com.cypress.cysmart.CustomApp.data.models;

import java.io.Serializable;
import java.util.HashMap;

public class SessionG1 implements Serializable {

    private HashMap<Long, Float> m1aValuesAndTime = new HashMap<Long, Float>() ;

    private HashMap<Long, Float> m1bValuesAndTime = new HashMap<Long, Float>();

    private float maxMavalue = 0 ;

    private float maxMbValue  = 0 ;


    public float getMaxMavalue() {
        return maxMavalue;
    }

    public void setMaxMavalue(float maxMavalue) {
        this.maxMavalue = maxMavalue;
    }

    public float getMaxMbValue() {
        return maxMbValue;
    }

    public void setMaxMbValue(float maxMbValue) {
        this.maxMbValue = maxMbValue;
    }

    public HashMap<Long, Float> getM1aValuesAndTime() {
        return m1aValuesAndTime;
    }

    public void setM1aValuesAndTime(HashMap<Long, Float> m1aValuesAndTime) {
        this.m1aValuesAndTime = m1aValuesAndTime;
    }

    public HashMap<Long, Float> getM1bValuesAndTime() {
        return m1bValuesAndTime;
    }

    public void setM1bValuesAndTime(HashMap<Long, Float> m1bValuesAndTime) {
        this.m1bValuesAndTime = m1bValuesAndTime;
    }
}
