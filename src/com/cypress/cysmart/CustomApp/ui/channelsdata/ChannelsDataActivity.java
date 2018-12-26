package com.cypress.cysmart.CustomApp.ui.channelsdata;

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
import android.view.View;

import com.cypress.cysmart.CustomApp.services.CustomService;
import com.cypress.cysmart.CustomApp.ui.channelsdata.adapter.ChannelsPagerAdapter;
import com.cypress.cysmart.R;

public class ChannelsDataActivity extends AppCompatActivity {



    private ViewPager mViewPager ;
    private Toolbar mToolbar ;
    private ChannelsPagerAdapter  mPagerAdapter ;
    private TabLayout mTablayout ;

    private CustomService mCustomService ;


    ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {

            CustomService.CustomBinder binder = (CustomService.CustomBinder) iBinder;
            mCustomService = binder.getService() ;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

            mCustomService = null ;
        }
    } ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channles_data);

        mTablayout = findViewById(R.id.tablayout) ;
        mToolbar   = findViewById (R.id.toolbar ) ;
        mViewPager = findViewById (R.id.viewpager );


        setSupportActionBar(mToolbar );
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        bindService(new Intent(this,CustomService.class) , connection ,Context.BIND_AUTO_CREATE) ;
        setViewPager();
    }


    private void setViewPager() {

        mPagerAdapter = new ChannelsPagerAdapter(getSupportFragmentManager() ) ;
        mViewPager.setAdapter(mPagerAdapter );
        mTablayout.setupWithViewPager(mViewPager ) ;
    }


    public CustomService getCustomService() {
        return mCustomService;
    }
}
