package com.example.notepad.bullsandcows.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Converters {

    public static long getCurrentDayOfRecord(long pLongDate) {
        return (Constants.BACK_EPOCH_TIME_NOTATION - pLongDate);
    }
    //todo find correct path conversion for time
    public static long getActualDay(long pActualTime) {
        pActualTime = Constants.BACK_EPOCH_TIME_NOTATION - pActualTime;
        long oneDay = 1000 * 60 * 60 * 24;
        return (pActualTime - (pActualTime % oneDay)) + oneDay  - (3600000 * 3)*2 + 360000;
    }

    public static long getActualWeek(long pActualTime) {
        pActualTime = Constants.BACK_EPOCH_TIME_NOTATION - pActualTime;
        long oneWeek = 1000 * 60 * 60 * 24 * 7;
        return (pActualTime - (pActualTime % oneWeek)) + oneWeek  - (3600000 * 3)*2 +360000;
    }

    public static String convertTimeToString(Long pLong) {
        Date date = new Date((Constants.BACK_EPOCH_TIME_NOTATION - pLong) + (3600000 * 3));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC+3"));
        DateFormat dateFormat = simpleDateFormat;
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC+3"));
        return dateFormat.format(date);
    }

    public static String convertDirectTimeToString(Long pLong) {
        Date date = new Date((pLong) + (3600000 * 3));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC+3"));
        DateFormat dateFormat = simpleDateFormat;
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC+3"));
        return dateFormat.format(date);
    }
}
