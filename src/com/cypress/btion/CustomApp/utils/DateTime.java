package com.cypress.btion.CustomApp.utils;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DateTime {

    private static final String TAG = "DateTime" ;



    // if time is 13.15 will return 13.025
    public static float getFloatHour(Long timeInMillis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeInMillis);

        int hourPerDay = calendar.get(Calendar.HOUR_OF_DAY);

        int minutes = calendar.get(Calendar.MINUTE);


        float mintesRate = (float) minutes/60 ;
        Log.i(TAG ,"Minutes :"+ minutes) ;
        Log.i(TAG ,"Minutes Rate :"+mintesRate);
        float floatHour = hourPerDay +mintesRate  ;

        return floatHour;

    }


    public static String getTime (Long timeInMillis ){
        Calendar calendar = Calendar.getInstance() ;
        calendar.setTimeInMillis(timeInMillis );

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm") ;

        return simpleDateFormat.format(calendar.getTime() );
    }

    public static String getCompleteTime (){
        Calendar calendar =Calendar.getInstance() ;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss") ;
        return simpleDateFormat.format(calendar.getTime()) ;
    }

    public static String getDateAndTime (){
        Calendar calendar = Calendar.getInstance() ;

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy-HH:mm") ;
        return  simpleDateFormat.format(calendar.getTime());
    }


    public static String getDate(){
        Calendar calendar = Calendar.getInstance() ;

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy") ;
        return  simpleDateFormat.format(calendar.getTime() ) ;
    }


}
