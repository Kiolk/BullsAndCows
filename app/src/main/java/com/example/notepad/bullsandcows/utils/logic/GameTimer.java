package com.example.notepad.bullsandcows.utils.logic;

import android.os.Handler;
import android.text.format.DateUtils;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class GameTimer {

    interface Callback {

        void refreshTimer(String pNewTime);
    }

    private static final String TIMESTAMP_MM_SS = "mm:ss";

    private int mTimerCount;
    private long mStartGameTime;
    private TextView mTimerView;

    private Timer mTimer;

    GameTimer() {
    }

    void startTimer(final Callback pCallback) {
        //TODO move logic with timer to separate class and use callbacks for notifications
        mTimerCount = 0;
        mStartGameTime = System.currentTimeMillis();
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
        }, 0, 1000);
    }

    void cancelTimer() {
        mTimerCount = 0;
        mTimer.cancel();
    }

    public String getWinTime(){
        final Date pDate = new Date(mTimerCount * DateUtils.SECOND_IN_MILLIS);
        final DateFormat format = new SimpleDateFormat(TIMESTAMP_MM_SS, Locale.ENGLISH);
        return format.format(pDate);
    }
}
