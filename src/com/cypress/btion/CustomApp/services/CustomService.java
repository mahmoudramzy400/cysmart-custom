package com.cypress.btion.CustomApp.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.cypress.btion.BLEConnectionServices.BluetoothLeService;
import com.cypress.btion.CommonUtils.Constants;
import com.cypress.btion.CommonUtils.Utils;
import com.cypress.btion.CustomApp.data.models.CycleChannelG1;
import com.cypress.btion.CustomApp.data.models.CycleChannelG2;
import com.cypress.btion.CustomApp.data.models.CycleChannelG3;

import com.cypress.btion.CustomApp.data.models.SessionG1;
import com.cypress.btion.CustomApp.data.models.SessionG2;
import com.cypress.btion.CustomApp.data.models.SessionG3;
import com.cypress.btion.CustomApp.ui.channelsdata.ChannelsDataActivity;
import com.cypress.btion.CustomApp.utils.BroadCastHandler;
import com.cypress.btion.CustomApp.utils.CustomParser;
import com.cypress.btion.CustomApp.utils.DateTime;
import com.cypress.btion.CustomApp.utils.FilesManager;
import com.cypress.btion.CustomApp.utils.GattAttributes;
import com.cypress.btion.CustomApp.utils.UUIDDatabase;
import com.cypress.btion.R;

import java.util.ArrayList;

public class CustomService extends Service {


    private final static String TAG = "CustomService";
    private static final int MINIMUM_DURATION_OF_READING_VOLTAGE = 1000; // 1 Second
    // Shunt voltage of G1 , G2 , G3
    private BluetoothGattCharacteristic mCharacteristicVoltageG1,
            mCharacteristicVoltageG2,
            mCharacteristicVoltageG3;

    // current of G1 , G2 G3
    private BluetoothGattCharacteristic mCharacteristicCurrentG1,
            mCharacteristicCurrentG2,
            mCharacteristicCurrentG3;

    // Bus voltage of G1 , G2 , G3
    private BluetoothGattCharacteristic mCharacteristicBusVG1,
            mCharacteristicBusVG2,
            mCharacteristicBusVG3;

    // time
    private BluetoothGattCharacteristic mCharacteristicTime;

    // current of D
    private BluetoothGattCharacteristic mCharacteristicCurrentD;
    // bus of D
    private BluetoothGattCharacteristic mCharacteristicBusD;

    private BluetoothGattService mCustomService;

    private CycleChannelG1 mCurrentCycleG1  ;
    private CycleChannelG2 mCurrentCycleG2  ;
    private CycleChannelG3 mCurrentCycleG3  ;

    private CycleChannelG1 mLastCycleG1 ;
    private CycleChannelG2 mLastCycleG2 ;
    private CycleChannelG3 mLastCycleG3 ;

    private NotificationCompat.Builder mBuilder ;
    private NotificationManager mMananger ;

    //___________ hex  values of characteristic
    private String mHexCurrentG1 = "";
    private String mHexCurrentG2 = "";
    private String mHexCurrentG3 = "";
    private String mHexCurrentD = "";

    private String mHexVoltageG1 = "";
    private String mHexVoltageG2 = "";
    private String mHexVoltageG3 = "";

    private String mHexBusG1 = "";
    private String mHexBusG2 = "";
    private String mHexBusG3 = "";
    private String mHexBusD = "";

    private String mHexCurrentTime = "";


    //___________ Integer values of characteristic


    private int mCurrentG1 = 0;
    private int mCurrentG2 = 0;
    private int mCurrentG3 = 0;
    private int mCurrentD = 0;

    private int mVoltageG1 = 0;
    private int mVoltageG2 = 0;
    private int mVoltageG3 = 0;

    private int mBusG1 = 0;
    private int mBusG2 = 0;
    private int mBusG3 = 0;
    private int mBusD = 0;

    private int mTimeInSeconds = -1;

    private int mCurrentChannelVoltageOn = -1;
    private Handler mHandler = new Handler();


    private String fileBody ="\n" ;


    private SessionG1 sessionG1 ;
    private SessionG2 sessionG2 ;
    private SessionG3 sessionG3 ;

    private CustomBinder mCustomBinder = new CustomBinder();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mCustomBinder ;
    }


    @Override
    public void onCreate() {
        super.onCreate();

        Log.i(TAG, "OnCreate Our custom service ");

        if (BluetoothLeService.mBluetoothGatt != null )
        mCustomService = BluetoothLeService.mBluetoothGatt.getService(UUIDDatabase.UUID_CUSTOM_SERVICE);

        if (mCustomService != null) {



            for (BluetoothGattCharacteristic gattCharacteristic : mCustomService.getCharacteristics()) {
                // ___________________Voltage characteristics_______________________
                if (gattCharacteristic.getUuid().equals(UUIDDatabase.UUID_CHARACTERISTIC_SHUNT_VOL_G1) &&
                        gattCharacteristic.getInstanceId() == GattAttributes.CHARACTERISTIC_SHUNT_VOL_G1_INSTANCE) {
                    mCharacteristicVoltageG1 = gattCharacteristic;
                }

                if (gattCharacteristic.getUuid().equals(UUIDDatabase.UUID_CHARACTERISTIC_SHUNT_VOL_G2) &&
                        gattCharacteristic.getInstanceId() == GattAttributes.CHARACTERISTIC_SHUNT_VOL_G2_INSTANCE) {
                    mCharacteristicVoltageG2 = gattCharacteristic;
                }

                if (gattCharacteristic.getUuid().equals(UUIDDatabase.UUID_CHARACTERISTIC_SHUNT_VOL_G3) &&
                        gattCharacteristic.getInstanceId() == GattAttributes.CHARACTERISTIC_SHUNT_VOL_G3_INSTANCE) {
                    mCharacteristicVoltageG3 = gattCharacteristic;
                }

                // ___________________________Bus Voltage _________________________________________

                if (gattCharacteristic.getUuid().equals(UUIDDatabase.UUID_CHARACTERISTIC_BUS_VOL_G1) &&
                        gattCharacteristic.getInstanceId() == GattAttributes.CHARACTERISTIC_BUS_VOL_G1_INSTANCE) {
                    mCharacteristicBusVG1 = gattCharacteristic;
                }

                if (gattCharacteristic.getUuid().equals(UUIDDatabase.UUID_CHARACTERISTIC_BUS_VOL_G2) &&
                        gattCharacteristic.getInstanceId() == GattAttributes.CHARACTERISTIC_BUS_VOL_G2_INSTANCE) {
                    mCharacteristicBusVG2 = gattCharacteristic;
                }

                if (gattCharacteristic.getUuid().equals(UUIDDatabase.UUID_CHARACTERISTIC_BUS_VOL_G3) &&
                        gattCharacteristic.getInstanceId() == GattAttributes.CHARACTERISTIC_BUS_VOL_G3_INSTANCE) {
                    mCharacteristicBusVG3 = gattCharacteristic;
                }

                // ___________________________Current Characteristics _____________________________

                if (gattCharacteristic.getUuid().equals(UUIDDatabase.UUID_CHARACTERISTIC_CURRENT_G1) &&
                        gattCharacteristic.getInstanceId() == GattAttributes.CHARACTERISTIC_CURRENT_G1_INSTANCE) {
                    mCharacteristicCurrentG1 = gattCharacteristic;
                }

                if (gattCharacteristic.getUuid().equals(UUIDDatabase.UUID_CHARACTERISTIC_CURRENT_G2) &&
                        gattCharacteristic.getInstanceId() == GattAttributes.CHARACTERISTIC_CURRENT_G2_INSTANCE) {
                    mCharacteristicCurrentG2 = gattCharacteristic;
                }

                if (gattCharacteristic.getUuid().equals(UUIDDatabase.UUID_CHARACTERISTIC_CURRENT_G3) &&
                        gattCharacteristic.getInstanceId() == GattAttributes.CHARACTERISTIC_CURRENT_G3_INSTANCE) {
                    mCharacteristicCurrentG3 = gattCharacteristic;
                }


                if (gattCharacteristic.getUuid().equals(UUIDDatabase.UUID_CHARACTERISTIC_CURRENT_D) &&
                        gattCharacteristic.getInstanceId() == GattAttributes.CHARACTERISTIC_CURRENT_D_INSTANCE) {
                    mCharacteristicCurrentD = gattCharacteristic;
                }

                if (gattCharacteristic.getUuid().equals(UUIDDatabase.UUID_CHARACTERISTIC_BUS_VOL_D) &&
                        gattCharacteristic.getInstanceId() == GattAttributes.CHARACTERISTIC_BUS_VOL_D_INSTANCE) {
                    mCharacteristicBusD = gattCharacteristic;
                }
                if (gattCharacteristic.getUuid().equals(UUIDDatabase.UUID_CHARACTERISTIC_TIME) &&
                        gattCharacteristic.getInstanceId() == GattAttributes.CHARACTERISTIC_TIME_INSTANCE) {
                    mCharacteristicTime = gattCharacteristic;
                }

            }


            registerReceiver(mGattUpdateReceiver, Utils.makeGattUpdateIntentFilter());

            fileBody += DateTime.getDateAndTime() +"\n" ;
            mHandler.post(readVoltageRunnable);

        } else {

            Log.i(TAG, "Is not our service stop self will be called");
            stopSelf();
        }
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        disconnected();
    }

    Runnable readVoltageRunnable = new Runnable() {
        @Override
        public void run() {
            Log.i(TAG, "Run starting");
            prepareBroadcastDataRead(mCharacteristicVoltageG1);
            mTimeInSeconds++;
           // mHandler.postDelayed(this, MINIMUM_DURATION_OF_READING_VOLTAGE);
        }
    };


    /**
     * prepare broadcast receiver to broadcast read characteristics
     *
     * @param bluetoothGattCharacteristic
     */
    private void prepareBroadcastDataRead(BluetoothGattCharacteristic bluetoothGattCharacteristic) {

        if ((bluetoothGattCharacteristic.getProperties() | BluetoothGattCharacteristic.PROPERTY_READ) > 0) {

            BluetoothLeService.readCharacteristic(bluetoothGattCharacteristic);
        }

    }

    /**
     * Broadcast receiver class to receives the broadcast from the service class
     */
    private BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            Bundle extras = intent.getExtras();

            // check action type
            if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {

                // New data available

                if (extras.containsKey(Constants.EXTRA_BYTE_VALUE)) { // contain value for characteristic

                    String receivedServiceUUID = "";
                    int receivedServiceInstanceId = -1;

                    String receivedCharacteristicUUID = "";
                    int receivedCharacteristicInstanceId = -1;

                    // check the uuid of characteristic data received
                    if (extras.containsKey(Constants.EXTRA_BYTE_UUID_VALUE)) {

                        receivedCharacteristicUUID = intent.getStringExtra(Constants.EXTRA_BYTE_UUID_VALUE);
                    }
                    // check instance id of characteristic received
                    if (extras.containsKey(Constants.EXTRA_BYTE_INSTANCE_VALUE)) {

                        receivedCharacteristicInstanceId = intent.getIntExtra(Constants.EXTRA_BYTE_INSTANCE_VALUE, -1);
                    }
                    // check uuid of service received
                    if (extras.containsKey(Constants.EXTRA_BYTE_SERVICE_UUID_VALUE)) {
                        receivedServiceUUID = intent.getStringExtra(Constants.EXTRA_BYTE_SERVICE_UUID_VALUE);

                    }
                    // check instance id of  service received
                    if (extras.containsKey(Constants.EXTRA_BYTE_SERVICE_INSTANCE_VALUE)) {
                        receivedServiceInstanceId = intent.getIntExtra(Constants.EXTRA_BYTE_SERVICE_INSTANCE_VALUE, -1);
                    }

                    // check the data is belong to our service
                    if (receivedServiceUUID.equals(mCustomService.getUuid().toString()) &&
                            receivedServiceInstanceId == mCustomService.getInstanceId()) {
                        // this is our service
                        byte[] array = intent.getByteArrayExtra(Constants.EXTRA_BYTE_VALUE);


                        // check the reading characteristics
                        // __________________________________________Handle Current ______________________________________
                        // characteristic of current D
                        if (receivedCharacteristicUUID.equals(mCharacteristicCurrentD.getUuid().toString()) &&
                                receivedCharacteristicInstanceId == mCharacteristicCurrentD.getInstanceId()) {
                            handleCurrentOfD(array);

                        }
                        if (receivedCharacteristicUUID.equals(mCharacteristicCurrentG1.getUuid().toString()) &&
                                receivedCharacteristicInstanceId == mCharacteristicCurrentG1.getInstanceId()) {
                            handleCurrentOfG1(array);
                        }
                        if (receivedCharacteristicUUID.equals(mCharacteristicCurrentG2.getUuid().toString()) &&
                                receivedCharacteristicInstanceId == mCharacteristicCurrentG2.getInstanceId()) {
                            handleCurrentOfG2(array);
                        }

                        if (receivedCharacteristicUUID.equals(mCharacteristicCurrentG3.getUuid().toString()) &&
                                receivedCharacteristicInstanceId == mCharacteristicCurrentG3.getInstanceId()) {
                            handleCurrentOfG3(array);
                        }

                        //_______________________________________________Handle Voltage _________________________________________
                        // characteristic of voltage of g1
                        if (receivedCharacteristicUUID.equals(mCharacteristicVoltageG1.getUuid().toString()) &&
                                receivedCharacteristicInstanceId == mCharacteristicVoltageG1.getInstanceId()) {
                            // this voltage of G1
                            handleVoltageG1(array);
                        }

                        if (receivedCharacteristicUUID.equals(mCharacteristicVoltageG2.getUuid().toString()) &&
                                receivedCharacteristicInstanceId == mCharacteristicVoltageG2.getInstanceId()) {
                            // this Voltage of G2
                            handleVoltageG2(array);
                        }


                        if (receivedCharacteristicUUID.equals(mCharacteristicVoltageG3.getUuid().toString()) &&
                                receivedCharacteristicInstanceId == mCharacteristicVoltageG3.getInstanceId()) {
                            // this Voltage of G3
                            handleVoltageG3(array);

                        }

                        // _________________________________ Handle Bus Voltage __________________________________

                        // characteristic of Bus of g1
                        if (receivedCharacteristicUUID.equals(mCharacteristicBusVG1.getUuid().toString()) &&
                                receivedCharacteristicInstanceId == mCharacteristicBusVG1.getInstanceId()) {
                            // this bus of G1
                            handleBusG1(array);
                        }

                        if (receivedCharacteristicUUID.equals(mCharacteristicBusVG2.getUuid().toString()) &&
                                receivedCharacteristicInstanceId == mCharacteristicBusVG2.getInstanceId()) {
                            // this bus of G2
                            handleBusG2(array);
                        }


                        if (receivedCharacteristicUUID.equals(mCharacteristicBusVG3.getUuid().toString()) &&
                                receivedCharacteristicInstanceId == mCharacteristicBusVG3.getInstanceId()) {
                            // this bus of G3
                            handleBusG3(array);

                        }

                        if (receivedCharacteristicUUID.equals(mCharacteristicBusD.getUuid().toString()) &&
                                receivedCharacteristicInstanceId == mCharacteristicBusD.getInstanceId()) {
                            // this bus of D
                            handleBusD(array);

                        }
                        //________________________Handle Time __________________________________
                       /*
                        if (receivedCharacteristicUUID.equals(mCharacteristicTime.getUuid().toString()) &&
                                receivedCharacteristicInstanceId == mCharacteristicTime.getInstanceId()) {
                            handleTime(array);
                        }
                        */
                    }

                }

            }


            if (action.equals(BluetoothLeService.ACTION_GATT_DISCONNECTED)){

                disconnected();

            }

        }
    };

    private void disconnected(){

        mHandler.removeCallbacks(readVoltageRunnable);
       // FilesManager.closeWriter();
        stopSelf();
       // startActivity(new Intent(this , SplashPageActivity.class));

    }
    /*
     ________________________________ Handle Voltage of Channels ___________________________________
     */
    private void handleVoltageG1(byte[] value) {



        //   int hexValue = Utils.ByteArraytoHexInt( value );
        mHexVoltageG1 = CustomParser.byteArraytoHex(value);
        mVoltageG1 = CustomParser.bytesToInteger(value);


        prepareBroadcastDataRead(mCharacteristicVoltageG2);
    }

    private void handleVoltageG2(byte[] value) {

        //  int hexaValue = Utils.ByteArraytoHexInt( value );
        mHexVoltageG2 = CustomParser.byteArraytoHex(value);
        mVoltageG2 = CustomParser.bytesToInteger(value);

        prepareBroadcastDataRead(mCharacteristicVoltageG3);


    }

    private void handleVoltageG3(byte[] value) {

        //  int hexaValue = Utils.ByteArraytoHexInt( value );
        mHexVoltageG3 = CustomParser.byteArraytoHex(value);
        mVoltageG3 = CustomParser.bytesToInteger(value);
        checkCurrentVoltages();
    }

    private void checkCurrentVoltages() {


        if (CustomParser.isVoltageOn(mCharacteristicVoltageG1)) { // Voltage ON in G1
            Log.i(TAG , "Channel G1 is ON ") ;

            if (mCurrentCycleG1 == null)
                mCurrentCycleG1 = new CycleChannelG1() ;

            if (mCurrentChannelVoltageOn != 1)
                mCurrentChannelVoltageOn = 1 ;

            // get last current value of D and G  in channel G3
            if (mCurrentCycleG3 != null && !mCurrentCycleG3.isLds0Setted() && !mCurrentCycleG3.isLgs0Setted()) {
                // the channel G3 is ended
                mTimeInSeconds = 0;

                mCurrentCycleG3.setLds0(mCurrentD);
                mCurrentCycleG3.setLgs0(mCurrentG3);
                // save value of M3a and value of M3b and time
                if (sessionG3 == null )
                    sessionG3 = new SessionG3() ;

                sessionG3.getM3aValuesAndTime().put(System.currentTimeMillis() , mCurrentCycleG3.getM3a()) ;
                sessionG3.getM3bValuesAndTime().put( System.currentTimeMillis() ,mCurrentCycleG3.getM3b());
                checkMa3andMb3( mCurrentCycleG3.getM3a(), mCurrentCycleG3.getM3b() );

                mCurrentCycleG3.setLds0Setted(true);
                mCurrentCycleG3.setLgs0Setted(true);
                mLastCycleG3 = mCurrentCycleG3 ;

                // send broadcast to the register context
                BroadCastHandler.broadcastSessionG3(this , sessionG3 );
                BroadCastHandler.broadcastCyclG3(this , mCurrentCycleG3 );
                BroadCastHandler.broadCastActiviteChannel(this , mCurrentChannelVoltageOn );

                mCurrentCycleG2 = null;
                mCurrentCycleG3 = null;

            }


            prepareBroadcastDataRead(mCharacteristicBusVG1);

        } else if (CustomParser.isVoltageOn(mCharacteristicVoltageG2)) { // Voltage ON in G2
            Log.i(TAG , "Channel G2 is ON ") ;
            if (mCurrentCycleG2 == null)
                mCurrentCycleG2 = new CycleChannelG2() ;

            if (mCurrentChannelVoltageOn !=  2 )
                mCurrentChannelVoltageOn = 2 ;

            // get last current value of D and G  in channel G1
            if (mCurrentCycleG1 != null && !mCurrentCycleG1.isLds0Setted() && !mCurrentCycleG1.isLgs0Setted()) {
                // the channel G1 is ended
                mTimeInSeconds = 0;

                mCurrentCycleG1.setLds0(mCurrentD);
                mCurrentCycleG1.setLgs0(mCurrentG1);

                // save value of M1a and value of M1b and time
                if (sessionG1 == null )
                    sessionG1 = new SessionG1() ;
                sessionG1.getM1aValuesAndTime().put( System.currentTimeMillis() ,mCurrentCycleG1.getM1a()) ;
                sessionG1.getM1bValuesAndTime().put(System.currentTimeMillis()  ,mCurrentCycleG1.getM1b()   );
                checkma1Andmb1( mCurrentCycleG1.getM1a() , mCurrentCycleG1.getM1b() );

                mCurrentCycleG1.setLgs0Setted(true);
                mCurrentCycleG1.setLds0Setted(true);
                mLastCycleG1 = mCurrentCycleG1 ;
                // send broadcast to the register context
                BroadCastHandler.broadcastSessionG1(this , sessionG1 );
                BroadCastHandler.broadcastCyclG1(this , mCurrentCycleG1 );
                BroadCastHandler.broadCastActiviteChannel(this , mCurrentChannelVoltageOn);


                mCurrentCycleG1 = null;
                mCurrentCycleG3 = null;
            }


            prepareBroadcastDataRead(mCharacteristicBusVG2);


        } else if (CustomParser.isVoltageOn(mCharacteristicVoltageG3)) { // Voltage ON in G3
            Log.i(TAG , "Channel G3 is ON ") ;

            if (mCurrentCycleG3 == null)
                mCurrentCycleG3 = new CycleChannelG3() ;

            if (mCurrentChannelVoltageOn != 3 )
                mCurrentChannelVoltageOn = 3 ;

            // get last current value of D and G  in channel G2
            if (mCurrentCycleG2 != null && !mCurrentCycleG2.isLds0Setted() && !mCurrentCycleG2.isLgs0Setted()) {

                // the channel G2 is ended
                mTimeInSeconds = 0;

                mCurrentCycleG2.setLds0(mCurrentD);
                mCurrentCycleG2.setLgs0(mCurrentG2);

                // save value of M2a and value of M2b and time
                if(sessionG2 == null )
                    sessionG2 = new SessionG2() ;
                sessionG2.getM2aValuesAndTime().put( System.currentTimeMillis(), mCurrentCycleG2.getM2a()  ) ;
                sessionG2.getM2bValuesAndTime().put( System.currentTimeMillis() , mCurrentCycleG2.getM2b() );
                // alert for high value
                checkMa2AndMb2( mCurrentCycleG2.getM2a() ,mCurrentCycleG2.getM2b() );

                mCurrentCycleG2.setLds0Setted(true);
                mCurrentCycleG2.setLgs0Setted(true);
                mLastCycleG2 = mCurrentCycleG2 ;

                //send Broadcast to the register context
                BroadCastHandler.broadcastSessionG2(this , sessionG2);
                BroadCastHandler.broadcastCyclG2(this , mCurrentCycleG2);
                BroadCastHandler.broadCastActiviteChannel(this , mCurrentChannelVoltageOn);

                mCurrentCycleG1 = null;
                mCurrentCycleG2 = null;
            }


            prepareBroadcastDataRead(mCharacteristicBusVG3);

        } else {
            // this mean some of channel was on for period and now is off

            handleVoltageOFF();

        }
    }


    private void handleVoltageOFF() {

        Log.i(TAG , "handleVoltageOff") ;
        if (mCurrentChannelVoltageOn == 1) {

            if (mCurrentCycleG1 != null &&  !mCurrentCycleG1.isLdsSetted() && !mCurrentCycleG1.isLgsSetted()) {
                // set last value of current D
                mCurrentCycleG1.setLds(mCurrentD);
                mCurrentCycleG1.setLdsSetted(true);

                // set last value of current G1
                mCurrentCycleG1.setLgs(mCurrentG1);
                mCurrentCycleG1.setLgsSetted(true);
            }

            prepareBroadcastDataRead(mCharacteristicBusVG1);

        } else if (mCurrentChannelVoltageOn == 2) {

            if (mCurrentCycleG2 != null &&  !mCurrentCycleG2.isLdsSetted() && !mCurrentCycleG2.isLgsSetted()) {
                // set last value of current D
                mCurrentCycleG2.setLds(mCurrentD);
                mCurrentCycleG2.setLdsSetted(true);

                // set last value of current G2
                mCurrentCycleG2.setLgs(mCurrentG2);
                mCurrentCycleG2.setLgsSetted(true);
            }
            prepareBroadcastDataRead(mCharacteristicBusVG2);

        } else if (mCurrentChannelVoltageOn == 3) {


            if (mCurrentCycleG3 != null &&  !mCurrentCycleG3.isLdsSetted() && !mCurrentCycleG3.isLgsSetted()) {
                // set last value of current D
                mCurrentCycleG3.setLds(mCurrentD);
                mCurrentCycleG3.setLdsSetted(true);

                // set last value of current G3
                mCurrentCycleG3.setLgs(mCurrentG3);
                mCurrentCycleG3.setLgsSetted(true);
            }

            prepareBroadcastDataRead(mCharacteristicBusVG3);
        }
    }

    private void handleBusG1(byte[] value) {

        mHexBusG1 = CustomParser.byteArraytoHex(value);
        mBusG1 = CustomParser.bytesToInteger(value);
        prepareBroadcastDataRead(mCharacteristicCurrentG1);


    }

    private void handleBusG2(byte[] value) {
        // int customeValue = CustomParser.getButVoltage(mCharacteristicBusVG2) ;
        mHexBusG2 = CustomParser.byteArraytoHex(value);
        mBusG2 = CustomParser.bytesToInteger(value);

        prepareBroadcastDataRead(mCharacteristicCurrentG2);
    }

    private void handleBusG3(byte[] value) {

        // int customeValue = CustomParser.getButVoltage(mCharacteristicBusVG3) ;
        mHexBusG3 = CustomParser.byteArraytoHex(value);
        mBusG3 = CustomParser.bytesToInteger(value);

        prepareBroadcastDataRead(mCharacteristicCurrentG3);
    }


    private void handleCurrentOfG1(byte[] array) {


        // mHexCurrentG1 = Utils.ByteArraytoHexInt( array ) ;
        mHexCurrentG1 = CustomParser.byteArraytoHex(array);
        mCurrentG1 = CustomParser.bytesToInteger(array);

        prepareBroadcastDataRead(mCharacteristicCurrentD);
    }

    private void handleCurrentOfG2(byte[] array) {


        //  mHexCurrentG2 = Utils.ByteArraytoHexInt( array ) ;
        mHexCurrentG2 = CustomParser.byteArraytoHex(array);
        mCurrentG2 = CustomParser.bytesToInteger(array);

        prepareBroadcastDataRead(mCharacteristicCurrentD);
    }

    private void handleCurrentOfG3(byte[] array) {


        // mHexCurrentG3 = Utils.ByteArraytoHexInt( array ) ;
        mHexCurrentG3 = CustomParser.byteArraytoHex(array);
        mCurrentG3 = CustomParser.bytesToInteger(array);

        prepareBroadcastDataRead(mCharacteristicCurrentD);
    }

    /*       ________________________________ Handle Current of Channels _________________________________
     */
    private void handleCurrentOfD(byte[] array) {


        // mHexCurrentD = Utils.ByteArraytoHexInt( array ) ;
        mHexCurrentD = CustomParser.byteArraytoHex(array);
        mCurrentD = CustomParser.bytesToInteger(array);

        //list for add time and current of D
        ArrayList<Integer> timeAndCurrentOfD = new ArrayList<>();

        timeAndCurrentOfD.add(mTimeInSeconds) ;
        timeAndCurrentOfD.add(mCurrentD)    ;
        Log.i(TAG , "Current time :" + mTimeInSeconds ) ;
        Log.i(TAG , "Current of D : " + mCurrentD) ;


        switch (mCurrentChannelVoltageOn){
            case 1 :
                if (mCurrentCycleG1 !=null)
                    mCurrentCycleG1.getTimeAndCurrentDList().add(timeAndCurrentOfD ) ;
                break;
            case 2 :
                if (mCurrentCycleG2 !=null)
                    mCurrentCycleG2.getTimeAndCurrentDList().add(timeAndCurrentOfD ) ;
                break;
            case 3 :
                if (mCurrentCycleG3 !=null)
                    mCurrentCycleG3.getTimeAndCurrentDList().add(timeAndCurrentOfD ) ;
                break;

        }


        prepareBroadcastDataRead(mCharacteristicBusD);


    }

    // ____________________________ Handle bus of D ______________________________
    private void handleBusD(byte[] array) {

        mHexBusD = CustomParser.byteArraytoHex(array);
        mBusD = CustomParser.bytesToInteger(array);


        String g1 = "\n" + DateTime.getCompleteTime() +",G1 Bus:"+mBusG1 +" ,D Bus:"+mBusD +", G1 Current:"+mCurrentG1+" ,D current:"+
                mCurrentD+ ", G1 Shunt:"+mVoltageG1;



        String g2 = "\n" + DateTime.getCompleteTime() +",G2 Bus:"+mBusG2 +" ,D Bus:"+mBusD +", G2 Current:"+mCurrentG2+" ,D current:"+
                mCurrentD+ ", G2 Shunt:"+mVoltageG2;


        String g3 = "\n" + DateTime.getCompleteTime() +",G3 Bus:"+mBusG3 +" ,D Bus:"+mBusD +", G3 Current:"+mCurrentG3+" ,D current:"+
                mCurrentD+ ", G3 Shunt:"+mVoltageG3 ;

        switch (mCurrentChannelVoltageOn ){
            case 1 :
               fileBody += g1 ;
                break;

            case 2 :
               fileBody += g2 ;
                break;

            case 3 :
               fileBody += g3 ;
                break;
        }





        FilesManager.saveChannelsData(this , fileBody);
        this.fileBody = "\n" ;

       // mHandler = new Handler( );
      //  mHandler.post(readVoltageRunnable) ;

        readVoltageRunnable.run();




    }


    public class CustomBinder extends Binder {

        public CustomService getService (){
            return CustomService.this ;
        }
    }


    private void checkma1Andmb1(float ma, float mb){

        if ( sessionG1.getMaxMavalue() !=  0 && sessionG1.getMaxMbValue() != 0 ){

            if (ma > sessionG1.getMaxMavalue() && mb > sessionG1.getMaxMbValue() ){

                showNotification(1 , getString(R.string.msg_high_dehydration));
            }


            if (ma > sessionG1.getMaxMavalue() && mb <sessionG1.getMaxMbValue() ){


                showNotification(1 ,getString(R.string.msg_high_saline));
            }
        }


    }


    private void checkMa2AndMb2(float ma, float mb){

        Log.i(TAG , "session 2 max ma: "+ sessionG2.getMaxMavalue());
        Log.i(TAG , "session 2 max mb :"+ sessionG2.getMaxMbValue()) ;

        if ( sessionG2.getMaxMavalue() !=  0 && sessionG2.getMaxMbValue() != 0 ){

            if (ma > sessionG2.getMaxMavalue() && mb > sessionG2.getMaxMbValue() ){

                showNotification(2 , getString(R.string.msg_high_dehydration));
            }


            if (ma > sessionG2.getMaxMavalue() && mb <sessionG2.getMaxMbValue() ){


                showNotification(2 ,getString(R.string.msg_high_saline));
            }
        }

    }

    private void checkMa3andMb3(float ma, float mb){

        if ( sessionG3.getMaxMavalue() !=  0 && sessionG3.getMaxMbValue() != 0 ){

            if (ma > sessionG3.getMaxMavalue() && mb > sessionG3.getMaxMbValue() ){

                showNotification(3 , getString(R.string.msg_high_dehydration));
            }


            if (ma > sessionG3.getMaxMavalue() && mb <sessionG3.getMaxMbValue() ){


                showNotification(3 ,getString(R.string.msg_high_saline));
            }
        }
    }
    public CycleChannelG1 getmLastCycleG1() {
        return mLastCycleG1;
    }

    public CycleChannelG2 getmLastCycleG2() {
        return mLastCycleG2;
    }

    public CycleChannelG3 getmLastCycleG3() {
        return mLastCycleG3;
    }

    public SessionG1 getSessionG1() {
        return sessionG1;
    }

    public SessionG2 getSessionG2() {
        return sessionG2;
    }

    public SessionG3 getSessionG3() {
        return sessionG3;
    }

    public int getmCurrentChannelVoltageOn (){
        return mCurrentChannelVoltageOn ;
    }

    private void showNotification (int channelNumber , String message  ){

        mMananger = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE );
        Intent intent = new Intent(this,ChannelsDataActivity.class) ;
        PendingIntent pendingIntent =  PendingIntent.getActivity(this ,
                0  ,intent ,PendingIntent.FLAG_UPDATE_CURRENT) ;


        mBuilder  = new NotificationCompat.Builder(this ,createChannelId() );
        mBuilder.setSmallIcon(R.drawable.ic_app_icon)
                .setContentTitle( getString(R.string.title_alert ) + channelNumber )
                .setContentText(message )
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentIntent(pendingIntent ) ;

        mMananger.notify(100 , mBuilder.build()  );

    }


    private String createChannelId(){

        // if android sdk > 26 we must create NotificationChannel  and return the id of this channel
        // else we must return nul

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            String channelId = "my_custom_service" ;
            String channelName = "custom_service" ;
            NotificationChannel channel  =  new NotificationChannel(channelId ,
                    channelName ,
                    NotificationManager.IMPORTANCE_NONE  ) ;

            channel.setLightColor(Color.RED);
            channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

            mMananger.createNotificationChannel(channel  );

            return  channelId ;
        }else {
            // the sdk < 26 return null for channel
            return  null ;
        }

    }
}
