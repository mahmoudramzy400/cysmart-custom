package com.cypress.cysmart.CustomApp.data.models;

import java.io.Serializable;
import java.util.ArrayList;

public class CycleChannelG1 implements Serializable {

    private int lds ; // last value of D when VG =ON
    private int lds0 ; // Last value of D when Vg = OFF
    private boolean isLdsSetted = false ;
    private boolean isLds0Setted = false ;

    private int maxVoltage =0 ;

    private int lgs ; // Last value of shunt of G1  when VG = ON
    private int lgs0 ; //Last value Of shunt of G1 when VG = OFF
    private boolean isLgsSetted = false ;
    private boolean isLgs0Setted = false ;

    private float M1a ;
    private float M1b  ;



    ArrayList<ArrayList<Integer>> timeAndCurrentDList = new ArrayList<>() ;


    public int getLds() {
        return lds;


    }

    public void setLds(int lds) {
        this.lds = lds;
    }

    public int getLds0() {
        return lds0;
    }

    public void setLds0(int lds0) {
        this.lds0 = lds0;
    }

    public int getLgs() {
        return lgs;
    }

    public void setLgs(int lgs) {
        this.lgs = lgs;
    }

    public int getLgs0() {
        return lgs0;
    }

    public void setLgs0(int lgs0) {
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


    public float getM1a() {
        if (lds0 == 0 ){
            return  0 ;
        }else {

            float x =   lds-lds0 ;
            M1a = (float)  x/lds0 ;
        }
        return M1a;
    }

    public Float getM1b() {
        M1b = (lgs - lgs0) ;
        return M1b;
    }
}
