package com.cypress.btion.CustomApp.utils;

import android.bluetooth.BluetoothGattService;

import com.cypress.btion.BLEConnectionServices.BluetoothLeService;

public final  class CommonUtils {

    private CommonUtils () {

    }


    public static  boolean isOurCustomService (){
         BluetoothGattService customService = null ;

        if (BluetoothLeService.mBluetoothGatt != null )
            customService = BluetoothLeService.mBluetoothGatt.getService(UUIDDatabase.UUID_CUSTOM_SERVICE);


        return  customService != null ? true : false ;
    }
}
