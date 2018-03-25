package com.haybankz.medmanager.util;

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
}
