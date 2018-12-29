package com.cypress.cysmart.CustomApp.utils;

import android.bluetooth.BluetoothGattCharacteristic;
import android.util.Log;

import java.math.BigInteger;

public class CustomParser {

    private static final String TAG = "CustomParser" ;


    private CustomParser() {

    }
    /*
    Get value of shunt voltage
     */
    public static int getShuntVoltage(BluetoothGattCharacteristic characteristic) {
       // int customeValue = characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_SINT8, 1);
        int customeValue = characteristic.getIntValue( BluetoothGattCharacteristic.FORMAT_SINT8, 1);
        return customeValue ;

    }
    /*
    Get value of current
     */
    public static int getCurrentValue(BluetoothGattCharacteristic characteristic) {
        int customeValue = characteristic.getIntValue( BluetoothGattCharacteristic.FORMAT_SINT8 , 1);
        return customeValue;

    }
    /*
    Get value of bus voltage
     */
    public static int getButVoltage (BluetoothGattCharacteristic characteristic ){

        int customValue = characteristic.getIntValue( BluetoothGattCharacteristic.FORMAT_UINT8 , 1) ;

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


        if (value > 0){
            return true ;
        }else{
            return  false ;
        }
    }

    /*
   Check  wether the shunt voltage is on in this channel or not
    */
    public static boolean isVoltageOnString (BluetoothGattCharacteristic characteristic ,int channleNumber ){

        int value = characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_SINT8 , 0 ) ;

        Log.i(TAG , "channel"+channleNumber + "value is : " + value ) ;
        if (value > 0){
            return true ;
        }else{
            return  false ;
        }

    }
    /*
    Convert from hex to integer

     */
    public static int hexToInteger (String hex ){

        Integer decimal = Integer.parseInt(hex , 16);

        return decimal  ;
    }






    public static String byteArraytoHex(byte[] bytes) {
        BigInteger bi = new BigInteger(1, bytes);
        return String.format("%0" + (bytes.length << 1) + "X", bi);
    }





    public static int bytesToInteger(byte [] bytes ){
        return  new BigInteger(bytes).intValue() ;
    }

     /*
      public static String byteArraytoHex(byte[] bytes) {
        if(bytes!=null){
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                sb.append(String.format("%02X ", b));
            }
            return sb.toString();

        }
        return "";
    }

      */
}
