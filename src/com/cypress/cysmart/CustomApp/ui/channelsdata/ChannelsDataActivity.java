package com.cypress.cysmart.CustomApp.ui.channelsdata;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.cypress.cysmart.CustomApp.ui.channelsdata.adapter.ChannelsPagerAdapter;
import com.cypress.cysmart.R;

public class ChannelsDataActivity extends AppCompatActivity {



    private ViewPager mViewPager ;
    private Toolbar mToolbar ;
    private ChannelsPagerAdapter  mPagerAdapter ;
    private TabLayout mTablayout ;




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
        setViewPager();
    }


    private void setViewPager() {

        mPagerAdapter = new ChannelsPagerAdapter(getSupportFragmentManager() ) ;
        mViewPager.setAdapter(mPagerAdapter );
        mTablayout.setupWithViewPager(mViewPager ) ;
    }
}
