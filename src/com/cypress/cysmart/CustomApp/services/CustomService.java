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

import com.cypress.cysmart.CustomApp.utils.CustomParser;
import com.cypress.cysmart.CustomApp.utils.GattAttributes;
import com.cypress.cysmart.CustomApp.utils.UUIDDatabase;

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

    private CycleChannelG1 mCurrentCycleG1 = new CycleChannelG1();
    private CycleChannelG2 mCurrentCycleG2 = new CycleChannelG2();
    private CycleChannelG3 mCurrentCycleG3 = new CycleChannelG3();


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
    private String mHexCurrentTime = "";
    private String mHexBusD = "";

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
    private long mCurrentTime = 0;
    private long mBusD = 0;


    private int mCurrentChannelVoltageOn = -1;
    private Handler mHandler = new Handler();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        super.onCreate();

        Log.i(TAG, "OnCreate Our custom service ");

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

            mHandler.post(readVoltageRunnable);

        } else {

            Log.i(TAG, "Is not our service stop self will be called");
            stopSelf();
        }
    }


    Runnable readVoltageRunnable = new Runnable() {
        @Override
        public void run() {
            Log.i(TAG, "Run starting");
            prepareBroadcastDataRead(mCharacteristicVoltageG1);
            mHandler.postDelayed(this, MINIMUM_DURATION_OF_READING_VOLTAGE);
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
                        if (receivedCharacteristicUUID.equals(mCharacteristicTime.getUuid().toString()) &&
                                receivedCharacteristicInstanceId == mCharacteristicTime.getInstanceId()) {
                            handleTime(array);
                        }
                    }

                }

            }

        }
    };

    /*
     ________________________________ Handle Voltage of Channels ___________________________________
     */
    private void handleVoltageG1(byte[] value) {


        //   int hexValue = Utils.ByteArraytoHexInt( value );
        mHexVoltageG1 = CustomParser.byteArraytoHex(value);
        mVoltageG1 = CustomParser.hexToInteger(mHexVoltageG1);
        prepareBroadcastDataRead(mCharacteristicVoltageG2);
    }

    private void handleVoltageG2(byte[] value) {

        //  int hexaValue = Utils.ByteArraytoHexInt( value );
        mHexVoltageG2 = CustomParser.byteArraytoHex(value);

        mVoltageG2 = CustomParser.hexToInteger(mHexVoltageG2);
        prepareBroadcastDataRead(mCharacteristicVoltageG3);


    }

    private void handleVoltageG3(byte[] value) {

        //  int hexaValue = Utils.ByteArraytoHexInt( value );
        mHexVoltageG3 = CustomParser.byteArraytoHex(value);

        mVoltageG3 = CustomParser.hexToInteger(mHexVoltageG3);
        checkCurrentVoltages();
    }

    private void checkCurrentVoltages() {

        if (CustomParser.isVoltageOn(mCharacteristicVoltageG1)) { // Voltage on in G1

            mCurrentChannelVoltageOn = 1;
            prepareBroadcastDataRead(mCharacteristicBusVG1);

        } else if (CustomParser.isVoltageOn(mCharacteristicVoltageG2)) { // Voltage ON in G2

            mCurrentChannelVoltageOn = 2;
            prepareBroadcastDataRead(mCharacteristicBusVG1);


        } else if (CustomParser.isVoltageOn(mCharacteristicVoltageG3)) { // Voltage ON in G3
            mCurrentChannelVoltageOn = 3;
            prepareBroadcastDataRead(mCharacteristicBusVG1);

        } else {
            // this mean some of channel was on for period and now is off

            switch (mCurrentChannelVoltageOn) {
                case 1:
                    prepareBroadcastDataRead(mCharacteristicBusVG1);
                    break;

                case 2:
                    prepareBroadcastDataRead(mCharacteristicBusVG2);
                    break;

                case 3:
                    prepareBroadcastDataRead(mCharacteristicBusVG3);
                    break;
            }
        }
    }

    private void handleBusG1(byte[] value) {

        mHexBusG1 = CustomParser.byteArraytoHex(value);
        mBusG1 = CustomParser.hexToInteger(mHexBusG1);
        prepareBroadcastDataRead(mCharacteristicCurrentG1);
    }

    private void handleBusG2(byte[] value) {
        // int customeValue = CustomParser.getButVoltage(mCharacteristicBusVG2) ;
        mHexBusG2 = CustomParser.byteArraytoHex(value);
        mBusG2 = CustomParser.hexToInteger(mHexBusG2);
        prepareBroadcastDataRead(mCharacteristicCurrentG2);
    }

    private void handleBusG3(byte[] value) {

        // int customeValue = CustomParser.getButVoltage(mCharacteristicBusVG3) ;
        mHexBusG3 = CustomParser.byteArraytoHex(value);
        mBusG3 = CustomParser.hexToInteger(mHexBusG3);
        prepareBroadcastDataRead(mCharacteristicCurrentG3);
    }


    private void handleCurrentOfG1(byte[] array) {


        // mHexCurrentG1 = Utils.ByteArraytoHexInt( array ) ;
        mHexCurrentG1 = CustomParser.byteArraytoHex(array);
        mCurrentG1 = CustomParser.hexToInteger(mHexCurrentG1);
        prepareBroadcastDataRead(mCharacteristicCurrentD);
    }

    private void handleCurrentOfG2(byte[] array) {


        //  mHexCurrentG2 = Utils.ByteArraytoHexInt( array ) ;
        mHexCurrentG2 = CustomParser.byteArraytoHex(array);
        mCurrentG2 = CustomParser.hexToInteger(mHexCurrentG2);

        prepareBroadcastDataRead(mCharacteristicCurrentD);
    }

    private void handleCurrentOfG3(byte[] array) {


        // mHexCurrentG3 = Utils.ByteArraytoHexInt( array ) ;
        mHexCurrentG3 = CustomParser.byteArraytoHex(array);
        mCurrentG3 = CustomParser.hexToInteger(mHexCurrentG3);

        prepareBroadcastDataRead(mCharacteristicCurrentD);
    }

    /*       ________________________________ Handle Current of Channels _________________________________
     */
    private void handleCurrentOfD(byte[] array) {


        // mHexCurrentD = Utils.ByteArraytoHexInt( array ) ;
        mHexCurrentD = CustomParser.byteArraytoHex(array);
        mCurrentD = CustomParser.hexToInteger(mHexCurrentD);
        prepareBroadcastDataRead(mCharacteristicBusD);


    }

    // ____________________________ Handle bus of D ______________________________
    private void handleBusD(byte[] array) {

        mHexBusD = CustomParser.byteArraytoHex(array);
        mBusD = CustomParser.hexToInteger(mHexBusD);

        prepareBroadcastDataRead(mCharacteristicTime);

    }

    private void handleTime(byte[] array) {

        // get time
        mHexCurrentTime = CustomParser.byteArraytoHex(array);


        // Print hex Values
        switch (mCurrentChannelVoltageOn) {
            case 1:
                Log.i(TAG, "time :" + mHexCurrentTime +
                        " | Current of D :" + mHexCurrentD +
                        " | current of G1 :" + mHexCurrentG1 +
                        " | Voltage of G1: " + mHexVoltageG1 +
                        " | Bus: " + mHexBusG1 +
                        " | Bus of D :" + mHexBusD);
                break;

            case 2:
                Log.i(TAG, " time :" + mHexCurrentTime +
                        " | Current of D :" + mHexCurrentD +
                        " | current of G2 : " + mHexCurrentG2 +
                        " | Voltage of G2: " + mHexVoltageG2 +
                        " | Bus :" + mHexBusG2 +
                        " | Bus of D :" + mHexBusD);
                break;

            case 3:
                Log.i(TAG, "time :" + mHexCurrentTime +
                        " | Current of D :" + mHexCurrentD +
                        " | current of G3 :" + mHexCurrentG3 +
                        " | Voltage of G3: " + mHexVoltageG3 +
                        " | Bus :" + mHexBusG3 +
                        " |Bus of D :" + mHexBusD);
                break;
        }

        // Print Integer values
        switch (mCurrentChannelVoltageOn) {
            case 1:
                Log.v(TAG, "time :" + mHexCurrentTime +
                        " | Current of D :" + mCurrentD +
                        " | current of G1 :" + mCurrentG1 +
                        " | Voltage of G1: " + mVoltageG1 +
                        " | Bus: " + mBusG1 +
                        " | Bus of D :" + mBusD);
                break;

            case 2:
                Log.v(TAG, " time :" + mHexCurrentTime +
                        " | Current of D :" + mCurrentD +
                        " | current of G2 : " + mCurrentG2 +
                        " | Voltage of G2: " + mVoltageG2 +
                        " | Bus :" + mBusG2 +
                        " | Bus of D :" + mBusD);
                break;

            case 3:
                Log.v(TAG, "time :" + mHexCurrentTime +
                        " | Current of D :" + mCurrentD +
                        " | current of G3 :" + mCurrentG3 +
                        " | Voltage of G3: " + mVoltageG3 +
                        " | Bus :" + mBusG3 +
                        " |Bus of D :" + mBusD);
                break;
        }

    }

}
