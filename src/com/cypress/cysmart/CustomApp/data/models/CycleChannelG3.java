package com.cypress.cysmart.CustomApp.data.models;

import java.io.Serializable;
import java.util.ArrayList;

public class CycleChannelG3 implements Serializable {

    private float lds ; // last value of D when VG =ON
    private float lds0 ; // Last value of D when Vg = OFF
    private boolean isLdsSetted = false ;
    private boolean isLds0Setted = false ;

    private int maxVoltage =0 ;

    private float lgs ; // Last value of shunt of G3  when VG = ON
    private float lgs0 ; //Last value Of shunt of G3 when VG = OFF
    private boolean isLgsSetted = false ;
    private boolean isLgs0Setted = false ;

    private float M3a  ;
    private float M3b  ;


    ArrayList<ArrayList<Integer>> timeAndCurrentDList = new ArrayList<>() ;



    public float getLds() {
        return lds;
    }
    public void setLds(float lds) {
        lds =lds /1000;
        this.lds = lds;
    }

    public float getLds0() {
        return lds0;
    }

    public void setLds0(float lds0) {
        lds0 = lds0/1000;
        this.lds0 = lds0;
    }

    public float getLgs() {
        return lgs;
    }

    public void setLgs(float lgs) {
        lgs = Math.abs(lgs) /100 ;
        this.lgs = lgs;
    }

    public float getLgs0() {
        return lgs0;
    }

    public void setLgs0(float lgs0) {
        lgs0 = Math.abs(lgs0)/100;
        this.lgs0 = lgs0;
    }

    public int getMaxVoltage() {
        return maxVoltage;
    }

    public void setMaxVoltage(int maxVoltage) {
        this.maxVoltage = maxVoltage;
    }

    public boolean isLdsSetted() {
        return isLdsSetted;
    }

    public void setLdsSetted(boolean ldsSetted) {
        isLdsSetted = ldsSetted;
    }

    public boolean isLgsSetted() {
        return isLgsSetted;
    }

    public void setLgsSetted(boolean lgsSetted) {
        isLgsSetted = lgsSetted;
    }

    public ArrayList<ArrayList<Integer>> getTimeAndCurrentDList() {
        return timeAndCurrentDList;
    }

    public void setTimeAndCurrentDList(ArrayList<ArrayList<Integer>> timeAndCurrentDList) {
        this.timeAndCurrentDList = timeAndCurrentDList;
    }

    public boolean isLds0Setted() {
        return isLds0Setted;
    }

    public void setLds0Setted(boolean lds0Setted) {
        isLds0Setted = lds0Setted;
    }

    public boolean isLgs0Setted() {
        return isLgs0Setted;
    }

    public void setLgs0Setted(boolean lgs0Setted) {
        isLgs0Setted = lgs0Setted;
    }


    public float getM3a() {
        if (lds0 == 0 ){
            return  0 ;
         }else {
            float x = (lds - lds0) ;

            M3a = (float) x/lds0 ;
        }
        return M3a;
    }

    public float getM3b() {
       M3b= (lgs - lgs0 )-3 ;
        return M3b;
    }
}
