package com.haybankz.medmanager.util;

import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by LENOVO on 3/25/2018.
 */

public class DateTimeUtils {



    public static long getDateTimeInMilliseconds(int year, int month, int day, int hourOfDay, int minute){
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day, hourOfDay, minute, 0);
        return calendar.getTimeInMillis();
    }

    public static String getDateTimeString(long dateInTimeMillis) {
        Date date = new Date(dateInTimeMillis);

        String format = "dd MMM, yyyy hh:mm a";
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.US);

        return String.format("%s%s", "", dateFormat.format(date));
    }

    public static long getFrequencyInMilliseconds(int frequency){
        long frequencyInMillis = 0L;

        switch(frequency){
            case Constant.INT_ONCE_A_DAY:
                frequencyInMillis = Constant.DAY_IN_MILLIS / Constant.INT_ONCE_A_DAY;
                break;

            case Constant.INT_TWICE_A_DAY:
                frequencyInMillis = Constant.DAY_IN_MILLIS / Constant.INT_TWICE_A_DAY;
                break;

            case Constant.INT_THRICE_A_DAY:
                frequencyInMillis = Constant.DAY_IN_MILLIS / Constant.INT_THRICE_A_DAY;
                break;

            case Constant.INT_FOUR_TIMES_A_DAY:
                frequencyInMillis = Constant.DAY_IN_MILLIS / Constant.INT_FOUR_TIMES_A_DAY;
                break;

            default:
//                throw new IllegalArgumentException("Cannot convert frequency");
                frequencyInMillis = Constant.DAY_IN_MILLIS / 96;

        }

        return frequencyInMillis;
    }

    public static long DateTimeStringToMillis(String dateTime){
        DateFormat format = new SimpleDateFormat("dd MMM, yyyy hh:mm a", Locale.US);
        long timeInMillis = 0;
        try{
            Date date = format.parse(dateTime);
            timeInMillis = date.getTime();

        }catch (ParseException e){
            Log.e("DateTimeUtils", "DateTimeStringToMillis: ", e);
        }

        return timeInMillis;
    }
}
