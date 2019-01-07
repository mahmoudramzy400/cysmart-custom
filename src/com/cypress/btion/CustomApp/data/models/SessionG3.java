package com.cypress.btion.CustomApp.data.models;

import java.io.Serializable;
import java.util.LinkedHashMap;

public class SessionG3 implements Serializable {

    private LinkedHashMap<Long, Float> m3aValuesAndTime = new LinkedHashMap<Long, Float>() ;

    private LinkedHashMap<Long, Float> m3bValuesAndTime = new LinkedHashMap<Long, Float>();

    private float maxMavalue  = 0 ;

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

    public LinkedHashMap<Long, Float> getM3aValuesAndTime() {
        return m3aValuesAndTime;
    }

    public void setM3aValuesAndTime(LinkedHashMap<Long, Float> m3aValuesAndTime) {
        this.m3aValuesAndTime = m3aValuesAndTime;
    }

    public LinkedHashMap<Long, Float> getM3bValuesAndTime() {
        return m3bValuesAndTime;
    }

    public void setM3bValuesAndTime(LinkedHashMap<Long, Float> m3bValuesAndTime) {
        this.m3bValuesAndTime = m3bValuesAndTime;
    }
}
