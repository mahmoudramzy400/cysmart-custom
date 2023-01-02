package com.cypress.btion.CustomApp.ui.channelsdata;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.cypress.btion.CustomApp.services.CustomService2Channels;
import com.cypress.btion.CustomApp.services.CustomService3Channels;
import com.cypress.btion.CustomApp.ui.channelsdata.adapter.ChannelsPagerAdapter;
import com.cypress.btion.R;

public class ChannelsDataActivity extends AppCompatActivity {

    private static final String TAG = "ChannelsDataActivity" ;

    private ViewPager mViewPager;
    private Toolbar mToolbar;
    private ChannelsPagerAdapter mPagerAdapter;
    private TabLayout mTablayout;

    private CustomService3Channels mCustomService3Channels;
    private CustomService2Channels mCustomService2Channels;
    private int channelNumber = 0 ;


    ServiceConnection connection3 = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {

            CustomService3Channels.CustomBinder binder = (CustomService3Channels.CustomBinder) iBinder;
            mCustomService3Channels = binder.getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

            mCustomService3Channels = null;
        }
    };

    ServiceConnection connection2 = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {

            CustomService2Channels.CustomBinder binder = (CustomService2Channels.CustomBinder) iBinder;
            mCustomService2Channels = binder.getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

            mCustomService2Channels = null;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channles_data);

        mTablayout = findViewById(R.id.tablayout);
        mToolbar = findViewById(R.id.toolbar);
        mViewPager = findViewById(R.id.viewpager);


        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        if (CustomService3Channels.mIsRunning) {
            Log.d(TAG , "3 Channels") ;
            bindService(new Intent(this, CustomService3Channels.class), connection3, Context.BIND_AUTO_CREATE);
            channelNumber =3 ;
        } else if (CustomService2Channels.mIsRunning){
            Log.d(TAG , "2 Channels") ;
            bindService(new Intent(this, CustomService2Channels.class), connection2, Context.BIND_AUTO_CREATE);
            channelNumber = 2 ;
        }



        setViewPager();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(connection3);
    }

    private void setViewPager() {

        if (channelNumber == 2 ){ // Two channel mode
            mPagerAdapter = new ChannelsPagerAdapter(getSupportFragmentManager() , 2);
        }else if (channelNumber == 3 )
            mPagerAdapter = new ChannelsPagerAdapter(getSupportFragmentManager());

        mViewPager.setAdapter(mPagerAdapter);
        mTablayout.setupWithViewPager(mViewPager);
    }


    public CustomService3Channels get3ChannelsCustomService() {
        return mCustomService3Channels;
    }

    public CustomService2Channels get2ChannelsCustomService (){
        return mCustomService2Channels ;
    }

    public int getChannelNumber() {
        return channelNumber;
    }
}
