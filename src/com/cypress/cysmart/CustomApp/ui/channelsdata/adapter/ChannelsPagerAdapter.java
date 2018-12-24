package com.cypress.cysmart.CustomApp.ui.channelsdata.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.cypress.cysmart.CustomApp.ui.channelsdata.Channel1Fragment;
import com.cypress.cysmart.CustomApp.ui.channelsdata.Channel2Fragment;
import com.cypress.cysmart.CustomApp.ui.channelsdata.Channel3Fragment;

public class ChannelsPagerAdapter extends FragmentStatePagerAdapter {



    private static final int TAB_COUNT = 3 ;



    public ChannelsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch ( position ){

            case 0:
                return   Channel1Fragment.newInstance()  ;

            case 1 :
                return   Channel2Fragment.newInstance() ;

            case 2 :
                return Channel3Fragment.newInstance() ;
        }
        return null;
    }

    @Override
    public int getCount() {
        return TAB_COUNT ;
    }


    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){

            case 0 :
                return  Channel1Fragment.TITLE ;

            case 1 :
                return  Channel2Fragment.TITLE ;

            case 2 :
                return Channel3Fragment.TITLE ;
        }
        return super.getPageTitle(position );
    }
}
