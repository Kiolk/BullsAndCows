package com.example.notepad.bullsandcows.utils;

import android.util.Log;

import com.example.notepad.bullsandcows.BuildConfig;

public final class LogUtil {

    private LogUtil() {
    }

    public static void msg(String message) {

        if (BuildConfig.DEBUG) {
            Log.d(Constants.TAG, message);
        }
    }
}
