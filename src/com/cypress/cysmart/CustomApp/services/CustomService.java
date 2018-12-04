package com.cypress.cysmart.CustomApp.services;

import android.app.Service;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.cypress.cysmart.BLEConnectionServices.BluetoothLeService;
import com.cypress.cysmart.CommonUtils.Constants;
import com.cypress.cysmart.CommonUtils.Utils;
import com.cypress.cysmart.CustomApp.data.models.CycleChannelG1;
import com.cypress.cysmart.CustomApp.data.models.CycleChannelG2;
import com.cypress.cysmart.CustomApp.data.models.CycleChannelG3;

import com.cypress.cysmart.CustomApp.utils.UUIDDatabase;

public class CustomService extends Service {


    private final static String TAG = "CustomService" ;
    private static final int MINIMUM_DURATION_OF_READING_VOLTAGE = 1000 ; // 1 Second
    private BluetoothGattCharacteristic mCharacteristicVoltageG1 ,
            mCharacteristicVoltageG2 ,
            mCharacteristicVoltageG3 ;

    private BluetoothGattCharacteristic mCharacteristicCurrentG1 ,
            mCharacteristicCurrentG2 ,
            mCharacteristicCurrentG3 ;

    private BluetoothGattCharacteristic mCharacteristicCurrentD ;

    private BluetoothGattService mCustomeService ;

    private CycleChannelG1 mCurrentCycleG1 = new CycleChannelG1() ;
    private CycleChannelG2 mCurrentCycleG2 = new CycleChannelG2() ;
    private CycleChannelG3 mCurrentCycleG3 = new CycleChannelG3()  ;


    private int mHexCurrentG1 = 0 ;
    private int mHexCurrentG2 = 0 ;
    private int mHexCurrentG3 = 0 ;
    private int mHexCurrentD =  0 ;
    private int mCurrentVoltageChannel = 1 ;
    private Handler mHandler = new Handler() ;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        super.onCreate();

        Log.i(TAG , "OnCreate Our custom service ") ;

        mCustomeService =BluetoothLeService.mBluetoothGatt.getService(UUIDDatabase.UUID_CUSTOM_SERVICE) ;

        if (mCustomeService != null ){

            // Voltage characteristics
            mCharacteristicVoltageG1 = mCustomeService.getCharacteristic( UUIDDatabase.UUID_CHARACTERISTIC_SHUNT_VOL_G1 ) ;
            mCharacteristicVoltageG2 = mCustomeService.getCharacteristic( UUIDDatabase.UUID_CHARACTERISTIC_SHUNT_VOL_G1 ) ;
            mCharacteristicVoltageG3 = mCustomeService.getCharacteristic( UUIDDatabase.UUID_CHARACTERISTIC_SHUNT_VOL_G3  )   ;


            // Current Characteristics
            mCharacteristicCurrentG1 = mCustomeService.getCharacteristic(UUIDDatabase.UUID_CHARACTERISTIC_CURRENT_G1 );
            mCharacteristicCurrentG2 = mCustomeService.getCharacteristic(UUIDDatabase.UUID_CHARACTERISTIC_CURRENT_G2 );
            mCharacteristicCurrentG3 = mCustomeService.getCharacteristic(UUIDDatabase.UUID_CHARACTERISTIC_CURRENT_G3 );

            mCharacteristicCurrentD = mCustomeService.getCharacteristic(UUIDDatabase.UUID_CHARACTERISTIC_CURRENT_D );

            registerReceiver( mGattUpdateReceiver, Utils.makeGattUpdateIntentFilter());

            mHandler.post(readVoltageRunnable);

        }else{

            Log.i(TAG , "Is not our service stop self will be called" );
            stopSelf();
        }
    }







    Runnable readVoltageRunnable = new Runnable() {
        @Override
        public void run() {
            Log.i(TAG , "Run starting")  ;

            // read current of channel D
             prepareBroadcastDataRead(mCharacteristicCurrentD);

             mHandler.postDelayed(this ,MINIMUM_DURATION_OF_READING_VOLTAGE ) ;
        }
    } ;



    /**
     * prepare broadcast receiver to broadcast read characteristics
     *
     *  @param bluetoothGattCharacteristic
     */
    private void prepareBroadcastDataRead (BluetoothGattCharacteristic bluetoothGattCharacteristic) {

        if (  ( bluetoothGattCharacteristic.getProperties() | BluetoothGattCharacteristic.PROPERTY_READ ) > 0 ){

            BluetoothLeService.readCharacteristic( bluetoothGattCharacteristic );
        }

    }

    /**
     * Broadcast receiver class to receives the broadcast from the service class
     */
    private BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String action =intent.getAction() ;
            Bundle extras = intent.getExtras() ;

            // check action type
            if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)){

                // New data available

                if (extras.containsKey(Constants.EXTRA_BYTE_VALUE)) { // contain value for characteristic

                    String receivedServiceUUID = "" ;
                    int receivedServiceInstanceId = -1 ;

                    String receivedCharacteristicUUID = "" ;
                    int receivedCharacteristicInstanceId = -1 ;

                    // check the uuid of characteristic data received
                    if (extras.containsKey(Constants.EXTRA_BYTE_UUID_VALUE )) {

                        receivedCharacteristicUUID = intent.getStringExtra(Constants.EXTRA_BYTE_UUID_VALUE) ;
                    }
                    // check instance id of characteristic received
                    if (extras.containsKey(Constants.EXTRA_BYTE_INSTANCE_VALUE)) {

                        receivedCharacteristicInstanceId = intent.getIntExtra(Constants.EXTRA_BYTE_INSTANCE_VALUE , -1 ) ;
                    }
                    // check uuid of service received
                    if (extras.containsKey(Constants.EXTRA_BYTE_SERVICE_UUID_VALUE) ) {
                        receivedServiceUUID = intent.getStringExtra(Constants.EXTRA_BYTE_SERVICE_UUID_VALUE) ;

                    }
                    // check instance id of  service received
                    if (extras.containsKey(Constants.EXTRA_BYTE_SERVICE_INSTANCE_VALUE)) {
                        receivedServiceInstanceId = intent.getIntExtra(Constants.EXTRA_BYTE_SERVICE_INSTANCE_VALUE , -1 ) ;
                    }

                    // check the data is belong to our service
                    if (receivedServiceUUID.equals(mCustomeService.getUuid().toString() ) &&
                            receivedServiceInstanceId == mCustomeService.getInstanceId()  ){
                        // this is our service
                        byte [] array = intent.getByteArrayExtra(Constants.EXTRA_BYTE_VALUE );


                        // check the reading characteristics
                        // __________________________________________Handle Current ______________________________________
                        // characteristic of current D
                        if (receivedCharacteristicUUID.equals(mCharacteristicCurrentD.getUuid().toString()) &&
                                receivedCharacteristicInstanceId== mCharacteristicCurrentD.getInstanceId() ){
                            handleCurrentOfD(array);
                        }
                        if (receivedCharacteristicUUID.equals(mCharacteristicCurrentG1.getUuid().toString()) &&
                                receivedCharacteristicInstanceId == mCharacteristicCurrentG1.getInstanceId() ){
                            handleCurrentOfG1(array);
                        }
                        if (receivedCharacteristicUUID.equals(mCharacteristicCurrentG2.getUuid().toString()) &&
                                receivedCharacteristicInstanceId == mCharacteristicCurrentG2.getInstanceId() ){
                            handleCurrentOfG2(array);
                        }

                        if (receivedCharacteristicUUID.equals(mCharacteristicCurrentG3.getUuid().toString()) &&
                                receivedCharacteristicInstanceId == mCharacteristicCurrentG3.getInstanceId() ){
                            handleCurrentOfG3(array);
                        }

                        //_______________________________________________Handle Voltage _________________________________________
                        // characteristic of voltage of g1
                        if (receivedCharacteristicUUID.equals(mCharacteristicVoltageG1.getUuid().toString()) &&
                                receivedCharacteristicInstanceId== mCharacteristicVoltageG1.getInstanceId() ){

                            // this voltage of G1
                            handleVoltageG1(array);
                        }

                        if (receivedCharacteristicUUID.equals(mCharacteristicVoltageG2.getUuid().toString()) &&
                                receivedCharacteristicInstanceId== mCharacteristicVoltageG2.getInstanceId() ){
                            // this Voltage of G2
                            handleVoltageG2(array);
                        }


                        if (receivedCharacteristicUUID.equals(mCharacteristicVoltageG3.getUuid().toString()) &&
                                receivedCharacteristicInstanceId== mCharacteristicVoltageG3.getInstanceId() ){
                            // this Voltage of G3
                            handleVoltageG3(array);

                        }
                    }

                }

            }

        }
    } ;

    /*
     ________________________________ Handle Voltage of Channels ___________________________________
     */
    private void handleVoltageG1 (byte[] value){


        //   int hexaValue = Utils.ByteArraytoHexInt( value );
        int hexaValue =  Utils.getCustomCharacteristicValue(mCharacteristicVoltageG1)/1000 ;
        Log.i(TAG  , "Voltage of G1 : " + hexaValue ) ;

        if (hexaValue >0 ){

            // check if that is max hex value to get ld and lg
            if (hexaValue >= mCurrentCycleG1.getMaxVoltage() && !mCurrentCycleG1.isLdsSeted() && !mCurrentCycleG1.isLgsSeted() ){
                // the voltage is increased
                mCurrentCycleG1.setMaxVoltage( hexaValue );
            }else {

                // the voltage start decreasing
                mCurrentCycleG1.setLds(mHexCurrentD);
                mCurrentCycleG1.setLgs(mHexCurrentG1);
                // we set  lgs and lds
                mCurrentCycleG1.setLgsSeted(true);
                mCurrentCycleG1.setLdsSeted(true);
                Log.i(TAG  , "ChannelG1 lds = " +mHexCurrentD ) ;
                Log.i(TAG ,  "ChannelG1 lgs =" + mHexCurrentG1 ) ;

            }


        }else{
            // Switch to next channel
            mCurrentVoltageChannel =2 ;
        }

        if (hexaValue == 0 ){
            // The voltage of g1 of get last d  and g (lgs0 , lds0 )
            mCurrentCycleG1.setLds0( mHexCurrentD );
            mCurrentCycleG1.setLgs0( mHexCurrentG1 );
            Log.i(TAG , "ChannelG1 lds0 = "+ mHexCurrentD ) ;
            Log.i(TAG , "ChannelG1 lgs0 = "+mHexCurrentG1 );
        }
    }

    private void handleVoltageG2 (byte[] value){

        //  int hexaValue = Utils.ByteArraytoHexInt( value );
        int hexaValue =  Utils.getCustomCharacteristicValue(mCharacteristicVoltageG2)/1000 ;
        Log.i(TAG  , "Voltage of G2 : " + hexaValue) ;


        if (hexaValue >0 ){

            // check if that is max hex value to get ld and lg
            if (hexaValue >= mCurrentCycleG2.getMaxVoltage() && !mCurrentCycleG2.isLdsSeted() && !mCurrentCycleG2.isLgsSeted() ){
                // the voltage is increased
                mCurrentCycleG2.setMaxVoltage( hexaValue );
            }else {

                // the voltage start decreasing
                mCurrentCycleG2.setLds(mHexCurrentD);
                mCurrentCycleG2.setLgs(mHexCurrentG2);
                // we set  lgs and lds
                mCurrentCycleG2.setLgsSeted(true);
                mCurrentCycleG2.setLdsSeted(true);
                Log.i(TAG  , "ChannelG2 lds = " +mHexCurrentD ) ;
                Log.i(TAG ,  "ChannelG2 lgs =" + mHexCurrentG2 ) ;

            }


        }else{
            // Switch to next channel
            mCurrentVoltageChannel =3 ;
        }

        if (hexaValue == 0 ){
            // The voltage of g1 of get last d  and g (lgs0 , lds0 )
            mCurrentCycleG2.setLds0( mHexCurrentD );
            mCurrentCycleG2.setLgs0( mHexCurrentG2 );

            Log.i(TAG , "ChannelG2 lds0 = "+ mHexCurrentD ) ;
            Log.i(TAG , "ChannelG2 lgs0 = "+mHexCurrentG2 );
        }

    }

    private void handleVoltageG3 (byte[] value){

        //  int hexaValue = Utils.ByteArraytoHexInt( value );
        int hexaValue =  Utils.getCustomCharacteristicValue(mCharacteristicVoltageG3)/1000 ;
        Log.i(TAG  , "Voltage of G3 : " + hexaValue) ;
        if (hexaValue >0 ){

            // check if that is max hex value to get ld and lg
            if (hexaValue >= mCurrentCycleG3.getMaxVoltage() && !mCurrentCycleG3.isLdsSeted() && !mCurrentCycleG3.isLgsSeted() ){
                // the voltage is increased
                mCurrentCycleG3.setMaxVoltage( hexaValue );
            }else {

                // the voltage start decreasing
                mCurrentCycleG3.setLds(mHexCurrentD);
                mCurrentCycleG3.setLgs(mHexCurrentG3);
                // we set  lgs and lds
                mCurrentCycleG3.setLgsSeted(true);
                mCurrentCycleG3.setLdsSeted(true);
                Log.i(TAG  , "ChannelG3 lds = " +mHexCurrentD ) ;
                Log.i(TAG ,  "ChannelG3 lgs =" + mHexCurrentG3 ) ;

            }


        }else{
            // Switch to next channel
            mCurrentVoltageChannel =1 ;
        }

        if (hexaValue == 0 ){
            // The voltage of g1 of get last d  and g (lgs0 , lds0 )
            mCurrentCycleG3.setLds0( mHexCurrentD );
            mCurrentCycleG3.setLgs0( mHexCurrentG3 );

            Log.i(TAG , "ChannelG3 lds0 = "+ mHexCurrentD ) ;
            Log.i(TAG , "ChannelG3 lgs0 = "+mHexCurrentG3  );
        }
    }


    /*
         ________________________________ Handle Current of Channels ___________________________________

     */
    private void handleCurrentOfD (byte [] array ){


        // mHexCurrentD = Utils.ByteArraytoHexInt( array ) ;
        mHexCurrentD= Utils.getCustomCharacteristicValue(mCharacteristicCurrentD) ;
        Log.i(TAG  , "Current of D : " + mHexCurrentD );


       switch (mCurrentVoltageChannel){

           case 1 :
               // read current of G1
               prepareBroadcastDataRead(mCharacteristicCurrentG1);
               break;

           case 2 :
               // read current of G2
               prepareBroadcastDataRead(mCharacteristicCurrentG2);
               break;

           case 3 :
               // read current of G3
               prepareBroadcastDataRead(mCharacteristicCurrentG3);
               break;


       }
    }
    private void handleCurrentOfG1 (byte [] array ){


        // mHexCurrentG1 = Utils.ByteArraytoHexInt( array ) ;
        mHexCurrentG1= Utils.getCustomCharacteristicValue(mCharacteristicCurrentG1) ;
        Log.i(TAG  , "Current of G1 : " + mHexCurrentG1);
        prepareBroadcastDataRead(mCharacteristicVoltageG1);
    }
    private void handleCurrentOfG2 (byte [] array ){


        //  mHexCurrentG2 = Utils.ByteArraytoHexInt( array ) ;
        mHexCurrentG2= Utils.getCustomCharacteristicValue(mCharacteristicCurrentG2) ;
        Log.i(TAG  , "Current of G2 : " + mHexCurrentG2);
        prepareBroadcastDataRead(mCharacteristicVoltageG2);
    }

    private void handleCurrentOfG3 (byte [] array) {


        // mHexCurrentG3 = Utils.ByteArraytoHexInt( array ) ;
        mHexCurrentG3= Utils.getCustomCharacteristicValue(mCharacteristicCurrentG3) ;
        Log.i(TAG  , "Current of G3 : " + mHexCurrentG3);
        prepareBroadcastDataRead(mCharacteristicVoltageG3);
    }

}
