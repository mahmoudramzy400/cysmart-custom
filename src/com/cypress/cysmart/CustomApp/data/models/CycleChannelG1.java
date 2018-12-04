package com.cypress.cysmart.CustomApp.data.models;

public class CycleChannelG1 {

    private int lds ;
    private boolean isLdsSeted =false ;

    private int lds0 ;

    private int maxVoltage =0 ;

    private int lgs ;
    private boolean isLgsSeted =false ;
    private int lgs0 ;

    private int M1a =lds0==0? 0 : (lds - lds0) / lds0 ;
    private int M1b = (lgs - lgs0) ;


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

    public boolean isLdsSeted() {
        return isLdsSeted;
    }

    public void setLdsSeted(boolean ldsSeted) {
        isLdsSeted = ldsSeted;
    }

    public boolean isLgsSeted() {
        return isLgsSeted;
    }

    public void setLgsSeted(boolean lgsSeted) {
        isLgsSeted = lgsSeted;
    }

    public int getMaxVoltage() {
        return maxVoltage;
    }

    public void setMaxVoltage(int maxVoltage) {
        this.maxVoltage = maxVoltage;
    }
}
