package com.cypress.cysmart.CustomApp.utils;

import java.util.UUID;

public class UUIDDatabase {

    // __________________________________ Channel G1 ____________________________________________

    public static final UUID UUID_CHARACTERISTIC_CURRENT_G1        = UUID.fromString(GattAttributes.CHARACTERISTIC_CURRENT_G1) ;


    public static final UUID UUID_CHARACTERISTIC_BUS_VOL_G1        = UUID.fromString(GattAttributes.CHARACTERISTIC_BUS_VOL_G1) ;


    public static final UUID UUID_CHARACTERISTIC_SHUNT_VOL_G1      =  UUID.fromString( GattAttributes.CHARACTERISTIC_SHUNT_VOL_G1 ) ;


    // __________________________________ Channel G2 ____________________________________________
    public static final UUID UUID_CHARACTERISTIC_CURRENT_G2         = UUID.fromString(GattAttributes.CHARACTERISTIC_CURRENT_G2) ;

    public static final UUID UUID_CHARACTERISTIC_BUS_VOL_G2         = UUID.fromString( GattAttributes.CHARACTERISTIC_BUS_VOL_G2) ;

    public static final UUID UUID_CHARACTERISTIC_SHUNT_VOL_G2       = UUID.fromString(GattAttributes.CHARACTERISTIC_SHUNT_VOL_G2 );



    // __________________________________ Channel G3 ____________________________________________

    public static final UUID UUID_CHARACTERISTIC_CURRENT_G3         = UUID.fromString(GattAttributes.CHARACTERISTIC_CURRENT_G3) ;


    public static final UUID UUID_CHARACTERISTIC_BUS_VOL_G3         = UUID.fromString(GattAttributes.CHARACTERISTIC_BUS_VOL_G3) ;

    public static final UUID UUID_CHARACTERISTIC_SHUNT_VOL_G3       = UUID.fromString(GattAttributes.CHARACTERISTIC_SHUNT_VOL_G3) ;


    // __________________________________ Channel D ____________________________________________
    public static final UUID UUID_CHARACTERISTIC_CURRENT_D         = UUID.fromString(GattAttributes.CHARACTERISTIC_CURRENT_D );


    public static final UUID UUID_CHARACTERISTIC_BUS_VOL_D         = UUID.fromString(GattAttributes.CHARACTERISTIC_BUS_VOL_D) ;

    public static final UUID UUID_CHARACTERISTIC_SHUNT_VOL_D       = UUID.fromString(GattAttributes.CHARACTERISTIC_SHUNT_VOL_D ) ;

    //______________________________Time Channel ________________________________
    public static final UUID UUID_CHARACTERISTIC_TIME = UUID.fromString(GattAttributes.CHARACTERISTIC_TIME) ;

    // _______________________________________ Our service _________________________

    public static final UUID UUID_CUSTOM_SERVICE                   = UUID.fromString(GattAttributes.CUSTOM_SERVICE   ) ;
}

