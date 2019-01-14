package com.cypress.btion.CustomApp.data.models;

import java.io.Serializable;
import java.util.LinkedHashMap;

public class SessionG1 implements Serializable {


    private LinkedHashMap<Long, Float> m1aValuesAndTime = new LinkedHashMap<>() ;

    private LinkedHashMap<Integer,Float> maValuesAndSeconds = new LinkedHashMap<>() ;

    private LinkedHashMap<Long, Float> m1bValuesAndTime = new LinkedHashMap<Long, Float>();

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

    public LinkedHashMap<Long, Float> getM1aValuesAndTime() {
        return m1aValuesAndTime;
    }

    public void setM1aValuesAndTime(LinkedHashMap<Long, Float> m1aValuesAndTime) {
        this.m1aValuesAndTime = m1aValuesAndTime;
    }

    public LinkedHashMap<Long, Float> getM1bValuesAndTime() {
        return m1bValuesAndTime;
    }

    public void setM1bValuesAndTime(LinkedHashMap<Long, Float> m1bValuesAndTime) {
        this.m1bValuesAndTime = m1bValuesAndTime;
    }


    public LinkedHashMap<Integer, Float> getMaValuesAndSeconds() {
        return maValuesAndSeconds;
    }
}
