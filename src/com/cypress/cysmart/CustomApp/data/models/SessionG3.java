package com.cypress.cysmart.CustomApp.data.models;

import java.io.Serializable;
import java.util.HashMap;

public class SessionG3 implements Serializable {

    private HashMap<Long, Float> m3aValuesAndTime = new HashMap<Long, Float>() ;

    private HashMap<Long, Float> m3bValuesAndTime = new HashMap<Long, Float>();


    public HashMap<Long, Float> getM3aValuesAndTime() {
        return m3aValuesAndTime;
    }

    public void setM3aValuesAndTime(HashMap<Long, Float> m3aValuesAndTime) {
        this.m3aValuesAndTime = m3aValuesAndTime;
    }

    public HashMap<Long, Float> getM3bValuesAndTime() {
        return m3bValuesAndTime;
    }

    public void setM3bValuesAndTime(HashMap<Long, Float> m3bValuesAndTime) {
        this.m3bValuesAndTime = m3bValuesAndTime;
    }
}