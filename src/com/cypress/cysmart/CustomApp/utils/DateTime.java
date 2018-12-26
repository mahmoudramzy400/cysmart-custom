package com.cypress.cysmart.CustomApp.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateTime {


    public static String getTimeHourMinutes() {
        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        String hourAndMinutes = simpleDateFormat.format(calendar.getTimeInMillis());
        return hourAndMinutes;
    }

    // get Hour in 24 Format
    public static int getHourPerday(Long timeInMillis) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeInMillis);
        int hourPerDay = calendar.get(Calendar.HOUR_OF_DAY);

        return hourPerDay;

    }

    // if time is 13.15 will return 13.025
    public static float getFloatHour(Long timeInMillis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeInMillis);

        int hourPerDay = calendar.get(Calendar.HOUR_OF_DAY);

        int minutes = calendar.get(Calendar.MINUTE);

        float mintesRate =  minutes/60 ;
        float floatHour = hourPerDay +mintesRate  ;

        return floatHour;

    }


    public static String getTime (Long timeInMillis ){
        Calendar calendar = Calendar.getInstance() ;
        calendar.setTimeInMillis(timeInMillis );

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm") ;

        return simpleDateFormat.format(calendar.getTime() );
    }
}
