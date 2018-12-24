package com.cypress.cysmart.CustomApp.data.models;

import java.io.Serializable;
import java.util.HashMap;

public class SessionG2 implements Serializable {

    private HashMap<Long, Float> m2aValuesAndTime = new HashMap<Long, Float>() ;

    private HashMap<Long, Float> m2bValuesAndTime = new HashMap<Long, Float>();


    public HashMap<Long, Float> getM2aValuesAndTime() {
        return m2aValuesAndTime;
    }

    public void setM2aValuesAndTime(HashMap<Long, Float> m2aValuesAndTime) {
        this.m2aValuesAndTime = m2aValuesAndTime;
    }

    public HashMap<Long, Float> getM2bValuesAndTime() {
        return m2bValuesAndTime;
    }

    public void setM2bValuesAndTime(HashMap<Long, Float> m2bValuesAndTime) {
        this.m2bValuesAndTime = m2bValuesAndTime;
    }
}
