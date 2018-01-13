package com.example.notepad.bullsandcows.utils;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import com.example.notepad.bullsandcows.data.holders.UserLoginHolder;

import static android.content.ContentValues.TAG;

public class KeepUserOnlineUtil  {

    private int mCountStartedActivities;

    public Application.ActivityLifecycleCallbacks getActivityLifeCycleCallback() {

        return new Application.ActivityLifecycleCallbacks() {

            @Override
            public void onActivityCreated(final Activity pActivity, final Bundle pBundle) {

            }

            @Override
            public void onActivityStarted(final Activity pActivity) {

            }

            @Override
            public void onActivityResumed(final Activity pActivity) {
                mCountStartedActivities++;
                Log.d(TAG, "onActivityResumed: ");
            }

            @Override
            public void onActivityPaused(final Activity pActivity) {
                mCountStartedActivities--;
                Log.d(TAG, "onActivityPaused: ");
                Log.d(TAG, "onActivityStopped: ");

                if (mCountStartedActivities == 0) {
                    UserLoginHolder.getInstance().setUserOffline();
                    Log.d(TAG, "onActivityStopped: ");
                }
            }

            @Override
            public void onActivityStopped(final Activity pActivity) {

            }

            @Override
            public void onActivitySaveInstanceState(final Activity pActivity, final Bundle pBundle) {

            }

            @Override
            public void onActivityDestroyed(final Activity pActivity) {
            }
        };
    }
}
