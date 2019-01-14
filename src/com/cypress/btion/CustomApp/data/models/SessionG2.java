package com.cypress.btion.CustomApp.data.models;

import java.io.Serializable;
import java.util.LinkedHashMap;

public class SessionG2 implements Serializable {

    private LinkedHashMap<Long, Float> m2aValuesAndTime = new LinkedHashMap<Long, Float>() ;
    private LinkedHashMap<Integer,Float> maValuesAndSeconds = new LinkedHashMap<>() ;

    private LinkedHashMap<Long, Float> m2bValuesAndTime = new LinkedHashMap<Long, Float>();


    private float maxMavalue = 0  ;

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
    public LinkedHashMap<Long, Float> getM2aValuesAndTime() {
        return m2aValuesAndTime;
    }

    public void setM2aValuesAndTime(LinkedHashMap<Long, Float> m2aValuesAndTime) {
        this.m2aValuesAndTime = m2aValuesAndTime;
    }

    public LinkedHashMap<Long, Float> getM2bValuesAndTime() {
        return m2bValuesAndTime;
    }

    public void setM2bValuesAndTime(LinkedHashMap<Long, Float> m2bValuesAndTime) {
        this.m2bValuesAndTime = m2bValuesAndTime;
    }

    public LinkedHashMap<Integer, Float> getMaValuesAndSeconds() {
        return maValuesAndSeconds;
    }
}
