package com.example.notepad.bullsandcows.utils.logic;

import android.os.Handler;
import android.text.format.DateUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

class GameTimer {

    private static final int TIMER_INTERVAL = 1000;

    interface Callback {

        void refreshTimer(String pNewTime);
    }

    private static final String TIMESTAMP_MM_SS = "mm:ss";

    private int mTimerCount;

    private Timer mTimer;

    void startTimer(final Callback pCallback) {
        mTimerCount = 0;
        mTimer = new Timer();
        final Handler handler = new Handler();
        mTimer.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
                final Date pDate = new Date(mTimerCount * DateUtils.SECOND_IN_MILLIS);
                final DateFormat format = new SimpleDateFormat(TIMESTAMP_MM_SS, Locale.ENGLISH);
                final String gameTime = format.format(pDate);
                ++mTimerCount;
                handler.post(new Runnable() {

                    @Override
                    public void run() {
                        pCallback.refreshTimer(gameTime);
                    }
                });
            }
        }, 0, TIMER_INTERVAL);
    }

    void cancelTimer() {
        mTimerCount = 0;
        mTimer.cancel();
    }

    String getWinTime() {
        final Date pDate = new Date(mTimerCount * DateUtils.SECOND_IN_MILLIS);
        final DateFormat format = new SimpleDateFormat(TIMESTAMP_MM_SS, Locale.ENGLISH);
        return format.format(pDate);
    }
}
