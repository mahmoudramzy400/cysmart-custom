package com.cypress.cysmart.CustomApp.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateTime {




    // if time is 13.15 will return 13.025
    public static float getFloatHour(Long timeInMillis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeInMillis);

        int hourPerDay = calendar.get(Calendar.HOUR_OF_DAY);

        int minutes = calendar.get(Calendar.MINUTE);

        float mintesRate = (float) minutes/60 ;
        float floatHour = hourPerDay +mintesRate  ;

        return floatHour;

    }


    public static String getTime (Long timeInMillis ){
        Calendar calendar = Calendar.getInstance() ;
        calendar.setTimeInMillis(timeInMillis );

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm") ;

        return simpleDateFormat.format(calendar.getTime() );
    }


    public static String getDateAndTime (){
        Calendar calendar = Calendar.getInstance() ;

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("DD/MM/YYYY-HH:mm") ;
        return  simpleDateFormat.format(calendar);
    }


    public static String getDate(){
        Calendar calendar = Calendar.getInstance() ;

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("DD-MM-YYYY") ;
        return  simpleDateFormat.format(calendar ) ;
    }


}
