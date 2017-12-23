package com.example.notepad.bullsandcows.utils.converters;

import com.example.notepad.bullsandcows.utils.Constants;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Converters {

    //todo find correct path conversion for time
    public static long getActualDay(long pActualTime) {
        pActualTime = Constants.BACK_EPOCH_TIME_NOTATION - pActualTime;
        long oneDay = 1000 * 60 * 60 * 24;
        long shiftConstant = 51888000;
        pActualTime = pActualTime - shiftConstant;
        long days = pActualTime / oneDay;
        return days * oneDay + shiftConstant + oneDay;
    }

    public static long getActualWeek(long pActualTime) {
        pActualTime = Constants.BACK_EPOCH_TIME_NOTATION - pActualTime;
        long oneWeek = 1000 * 60 * 60 * 24 * 7;
        long shiftConstant = 51888000;
        pActualTime = pActualTime - shiftConstant;
        long days = pActualTime / oneWeek;
        return days * oneWeek + shiftConstant + oneWeek;
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

    public static long convertToBackendTime(long pEpochTime){
        return Constants.BACK_EPOCH_TIME_NOTATION - pEpochTime;
    }
}
