/*
 * Copyright Cypress Semiconductor Corporation, 2014-2015 All rights reserved.
 * 
 * This software, associated documentation and materials ("Software") is
 * owned by Cypress Semiconductor Corporation ("Cypress") and is
 * protected by and subject to worldwide patent protection (UnitedStates and foreign), United States copyright laws and international
 * treaty provisions. Therefore, unless otherwise specified in a separate license agreement between you and Cypress, this Software
 * must be treated like any other copyrighted material. Reproduction,
 * modification, translation, compilation, or representation of this
 * Software in any other form (e.g., paper, magnetic, optical, silicon)
 * is prohibited without Cypress's express written permission.
 * 
 * Disclaimer: THIS SOFTWARE IS PROVIDED AS-IS, WITH NO WARRANTY OF ANY
 * KIND, EXPRESS OR IMPLIED, INCLUDING, BUT NOT LIMITED TO,
 * NONINFRINGEMENT, IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS
 * FOR A PARTICULAR PURPOSE. Cypress reserves the right to make changes
 * to the Software without notice. Cypress does not assume any liability
 * arising out of the application or use of Software or any product or
 * circuit described in the Software. Cypress does not authorize its
 * products for use as critical components in any products where a
 * malfunction or failure may reasonably be expected to result in
 * significant injury or death ("High Risk Product"). By including
 * Cypress's product in a High Risk Product, the manufacturer of such
 * system or application assumes all risk of such use and in doing so
 * indemnifies Cypress against all liability.
 * 
 * Use of this Software may be limited by and subject to the applicable
 * Cypress software license agreement.
 * 
 * 
 */

package com.cypress.btion;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;

import com.cypress.btion.CommonUtils.Logger;

import androidx.annotation.NonNull;

/**
 * Activity to display the initial splash screen
 */

public class SplashPageActivity extends Activity {
    /**
     * Page display time
     */
    private final int SPLASH_DISPLAY_LENGTH = 3000;
    /**
     * Flag to handle the handler
     */
    private boolean mHandlerFlag = true;

    private Handler mHandler = new Handler();
    private Runnable mRun = new Runnable() {

        @Override
        public void run() {

            if (mHandlerFlag) {
                // Finish the current Activity and start HomePage Activity
                Intent home = new Intent(SplashPageActivity.this, HomePageActivity.class);
                startActivity(home);
                SplashPageActivity.this.finish();
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (isTablet(this)) {
            Logger.d("Tablet");
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        } else {
            Logger.d("Phone");
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        setContentView(R.layout.activity_splash);

        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            // Activity was brought to front and not created,
            // Thus finishing this will get us to the last viewed activity
            finish();
            return;
        }
        /**
         * Run the code inside the runnable after the display time is finished
         * using a handler
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){

            boolean isGetLocation = ContextCompat.checkSelfPermission(this ,Manifest.permission.ACCESS_FINE_LOCATION)    == PackageManager.PERMISSION_GRANTED ;
            boolean isGetStorage  =  ContextCompat.checkSelfPermission(this ,Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
            if ( !isGetLocation ){
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION} ,100);
            }
            if (!isGetStorage ){
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE} ,101);
            }


            if (isGetLocation && isGetStorage){
                mHandler.postDelayed(mRun ,SPLASH_DISPLAY_LENGTH) ;
            }

        }else {
            mHandler.postDelayed(mRun, SPLASH_DISPLAY_LENGTH);
        }

    }

    @Override
    public void onBackPressed() {
        /**
         * Disable the handler to execute when user presses back when in
         * SplashPage Activity
         */
        mHandlerFlag = false;
        super.onBackPressed();
    }

    /**
     * Method to detect whether the device is phone or tablet
     */
    private static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        boolean isGetLocation = ContextCompat.checkSelfPermission(this ,Manifest.permission.ACCESS_FINE_LOCATION)    == PackageManager.PERMISSION_GRANTED ;
        boolean isGetStorage  = ContextCompat.checkSelfPermission(this ,Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        switch (requestCode){
            case 100 :
               isGetLocation= grantResults[0 ]== PackageManager.PERMISSION_GRANTED ;
                break;

            case 101 :
                isGetStorage = grantResults[0]==PackageManager.PERMISSION_GRANTED ;
                break ;

        }

        if (isGetLocation && isGetStorage){
            mHandler.postDelayed(mRun,SPLASH_DISPLAY_LENGTH) ;
        }
    }
}
