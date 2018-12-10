package com.cypress.cysmart.CustomApp.utils;

import android.bluetooth.BluetoothGattCharacteristic;

public class CustomParser {

    private CustomParser() {

    }
    /*
    Get value of shunt voltage
     */
    public static int getShuntVoltage(BluetoothGattCharacteristic characteristic) {
        int customeValue = characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_SINT8, 1);
        return customeValue ;

    }
    /*
    Get value of current
     */
    public static int getCurrentValue(BluetoothGattCharacteristic characteristic) {
        int customeValue = characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_SINT8, 1);
        return customeValue;

    }
    /*
    Get value of bus voltage
     */
    public static int getButVoltage (BluetoothGattCharacteristic characteristic ){

        int customValue = characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT8 , 1) ;

        return  customValue ;
    }
    public static int getTimeCharacteristicValue(BluetoothGattCharacteristic characteristic) {
        int customeValue = characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT32, 4);
        return customeValue;

    }


    /*
    Check  wether the shunt voltage is on in this channel or not
     */
    public static boolean isVoltageOn (BluetoothGattCharacteristic characteristic ){

        int value = characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_SINT8 , 0 ) ;

        if (value == 1){
            return true ;
        }
        return  false ;
    }

}
