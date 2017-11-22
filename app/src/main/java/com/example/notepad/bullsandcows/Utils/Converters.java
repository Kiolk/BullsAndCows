package com.example.notepad.bullsandcows.Utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by yauhen on 22.11.17.
 */

public class Converters {
    public static String convertTimeToString(Long pLong) {
        Date date = new Date(pLong + (3600000 * 3));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC+3"));
        DateFormat dateFormat = simpleDateFormat;
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC+3"));
        return dateFormat.format(date);
    }
}
