package com.example.notepad.bullsandcows.utils.converters;

import android.content.Context;
import android.text.format.DateUtils;

import com.example.notepad.bullsandcows.utils.Constants;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public final class TimeConvertersUtil {

    private static final String SPLITTING_SYMBOL = ":";
    private static final int SHIFT_CONSTANT_FOR_BACKEND = 51888000;
    private static final String TIME_STAMP_DD_MM_YYYY_HH_MM_SS = "dd/MM/yyyy hh:mm:ss";
    private static final String UTC_3 = "UTC+3";

    public static long getActualDay(final long pActualTime) {
        long actualTime = Constants.BACK_EPOCH_TIME_NOTATION - pActualTime;
        final long oneDay = DateUtils.DAY_IN_MILLIS;
        final long shiftConstant = SHIFT_CONSTANT_FOR_BACKEND;
        actualTime -= shiftConstant;
        final long days = actualTime / oneDay;
        return days * oneDay + shiftConstant + oneDay;
    }

    public static long getActualWeek(final long pActualTime) {
        final long actualTime = Constants.BACK_EPOCH_TIME_NOTATION - pActualTime;
        final long days = actualTime / DateUtils.WEEK_IN_MILLIS;
        return days * DateUtils.WEEK_IN_MILLIS + SHIFT_CONSTANT_FOR_BACKEND + DateUtils.WEEK_IN_MILLIS - DateUtils.DAY_IN_MILLIS;
    }

    public static String convertTimeToString(final Long pLong, final Context pContext) {
        final Date date = new Date((Constants.BACK_EPOCH_TIME_NOTATION - pLong) + (DateUtils.HOUR_IN_MILLIS * 3));
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(TIME_STAMP_DD_MM_YYYY_HH_MM_SS, getCurrentLocale(pContext));
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone(UTC_3));
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone(UTC_3));
        return simpleDateFormat.format(date);
    }

    public static String convertDirectTimeToString(final Long pLong, final Context pContext) {
        final Date date = new Date((pLong) + (DateUtils.HOUR_IN_MILLIS * 3));
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(TIME_STAMP_DD_MM_YYYY_HH_MM_SS, getCurrentLocale(pContext));
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone(UTC_3));
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone(UTC_3));
        return simpleDateFormat.format(date);
    }

    public static long convertToBackendTime(final long pEpochTime) {
        return Constants.BACK_EPOCH_TIME_NOTATION - pEpochTime;
    }

    public static int gameTimeToSeconds(final String pTime) {
        final String[] time = pTime.split(SPLITTING_SYMBOL);
        return Integer.parseInt(time[0]) * (int) DateUtils.MINUTE_IN_MILLIS + Integer.parseInt(time[1]);
    }

    private static Locale getCurrentLocale(final Context pContext) {
        return new Locale(pContext.getResources().getConfiguration().locale.getCountry());
    }
}
