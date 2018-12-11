package com.cypress.cysmart.CustomApp.utils;

import android.bluetooth.BluetoothGattCharacteristic;

public class CustomParser {

    private static int uint8 = 0x04;

    private static  int uint16 = 0x06;

    private static int sint8  = 0x0C ;

    private static int sint16 = 0x0E ;

    private CustomParser() {

    }
    /*
    Get value of shunt voltage
     */
    public static int getShuntVoltage(BluetoothGattCharacteristic characteristic) {
       // int customeValue = characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_SINT8, 1);
        int customeValue = characteristic.getIntValue( BluetoothGattCharacteristic.FORMAT_SINT16, 0);
        return customeValue ;

    }
    /*
    Get value of current
     */
    public static int getCurrentValue(BluetoothGattCharacteristic characteristic) {
        int customeValue = characteristic.getIntValue( BluetoothGattCharacteristic.FORMAT_SINT16 , 0);
        return customeValue;

    }
    /*
    Get value of bus voltage
     */
    public static int getButVoltage (BluetoothGattCharacteristic characteristic ){

        int customValue = characteristic.getIntValue( BluetoothGattCharacteristic.FORMAT_UINT16 , 0) ;

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
