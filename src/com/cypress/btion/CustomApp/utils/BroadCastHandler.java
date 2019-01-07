package com.cypress.btion.CustomApp.utils;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import com.cypress.btion.CustomApp.data.models.CycleChannelG1;
import com.cypress.btion.CustomApp.data.models.CycleChannelG2;
import com.cypress.btion.CustomApp.data.models.CycleChannelG3;
import com.cypress.btion.CustomApp.data.models.SessionG1;
import com.cypress.btion.CustomApp.data.models.SessionG2;
import com.cypress.btion.CustomApp.data.models.SessionG3;

public class BroadCastHandler {

    private static final String TAG  = "BroadCastHandler" ;
    public static final String ACTION_ACTIVE_CHANNEL_CHANGED = "com.example.bluetooth.le.ACTION_ACTIVE_CHANNEL_CHANGED";
    // for every complete cycle of G1 G2 G3
    public static final String ACTION_CYCLECHANG1 = "com.example.bluetooth.le.ACTION_CYCLECHANG1";
    public static final String ACTION_CYCLECHANG2 = "com.example.bluetooth.le.ACTION_CYCLECHANG2";
    public static final String ACTION_CYCLECHANG3 = "com.example.bluetooth.le.ACTION_CYCLECHANG3";

    // for ma mb values Changed for every session of G1 G3 G3
    public static final String ACTION_SESSION1_CHANGED = "com.example.bluetooth.le.ACTION_SESSION1_CHANGED";
    public static final String ACTION_SESSION2_CHANGED = "com.example.bluetooth.le.ACTION_SESSION2_CHANGED";
    public static final String ACTION_SESSION3_CHANGED = "com.example.bluetooth.le.ACTION_SESSION3_CHANGED";


    //Extra data
    public static final String EXTRA_ACTIVATE_CHANNEL = "EXTRA_ACTIVATE_CHANNEL";
    public static final String EXTRA_CYCLE_CHANG1 = "EXTRA_CYCLE_CHANG1";
    public static final String EXTRA_CYCLE_CHANG2 = "EXTRA_CYCLE_CHANG2";
    public static final String EXTRA_CYCLE_CHANG3 = "EXTRA_CYCLE_CHANG3";
    public static final String EXTRA_SESSIONG1 = "EXTRA_SESSIONG1";
    public static final String EXTRA_SESSIONG2 = "EXTRA_SESSIONG2";
    public static final String EXTRA_SESSIONG3 = "EXTRA_SESSIONG3";

    private BroadCastHandler() {

    }

    public static void broadCastActiviteChannel(Context context, int activeChannel) {

        Intent intent = new Intent(ACTION_ACTIVE_CHANNEL_CHANGED);
        intent.putExtra(EXTRA_ACTIVATE_CHANNEL, activeChannel);
        context.sendBroadcast(intent);


        Log.i(TAG  ,"broadCastActiviteChannel") ;
    }


    public static void broadcastCyclG1(Context context, CycleChannelG1 cycleChannelG1) {

        Intent intent = new Intent(ACTION_CYCLECHANG1);
        intent.putExtra(EXTRA_CYCLE_CHANG1, cycleChannelG1);
        context.sendBroadcast(intent);
        Log.i(TAG , "broadcastCyclG1") ;
    }

    public static void broadcastCyclG2(Context context, CycleChannelG2 cycleChannelG2) {

        Intent intent = new Intent(ACTION_CYCLECHANG2);
        intent.putExtra(EXTRA_CYCLE_CHANG2, cycleChannelG2);
        context.sendBroadcast(intent);
        Log.i(TAG ,"braodcastCycleG2") ;
    }

    public static void broadcastCyclG3(Context context, CycleChannelG3 cycleChannelG3) {

        Intent intent = new Intent(ACTION_CYCLECHANG3);
        intent.putExtra(EXTRA_CYCLE_CHANG3, cycleChannelG3);
        context.sendBroadcast(intent);

        Log.i(TAG ,"broadcastcycle3") ;
    }


    public static void broadcastSessionG1(Context context, SessionG1 sessionG1) {
        Intent intent = new Intent(ACTION_SESSION1_CHANGED);
        intent.putExtra(EXTRA_SESSIONG1, sessionG1);
        context.sendBroadcast(intent);
        Log.i(TAG , "braodcasetSessionG1") ;
    }

    public static void broadcastSessionG2(Context context, SessionG2 sessionG2) {
        Intent intent = new Intent(ACTION_SESSION2_CHANGED);
        intent.putExtra(EXTRA_SESSIONG2, sessionG2);
        context.sendBroadcast(intent);
        Log.i(TAG , "broadcastSetssionG2") ;
    }

    public static void broadcastSessionG3(Context context, SessionG3 sessionG3) {
        Intent intent = new Intent(ACTION_SESSION3_CHANGED);
        intent.putExtra(EXTRA_SESSIONG3, sessionG3);
        context.sendBroadcast(intent);
        Log.i(TAG , " broadcastSessionG3 ");
    }

    public static IntentFilter makeCustomIntentFilter() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_ACTIVE_CHANNEL_CHANGED);
        intentFilter.addAction(ACTION_CYCLECHANG1);
        intentFilter.addAction(ACTION_CYCLECHANG2);
        intentFilter.addAction(ACTION_CYCLECHANG3);

        intentFilter.addAction(ACTION_SESSION1_CHANGED);
        intentFilter.addAction(ACTION_SESSION2_CHANGED);
        intentFilter.addAction(ACTION_SESSION3_CHANGED);


        return  intentFilter ;

    }
}
