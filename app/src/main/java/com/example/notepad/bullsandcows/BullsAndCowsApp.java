package com.example.notepad.bullsandcows;

import android.app.Application;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.example.notepad.bullsandcows.data.databases.DBConnector;
import com.example.notepad.bullsandcows.utils.Constants;
import com.example.notepad.bullsandcows.utils.KeepUserOnlineUtil;

import io.fabric.sdk.android.Fabric;
import kiolk.com.github.pen.Pen;

public class BullsAndCowsApp extends Application {

    private int mCountStartedActivities = 0;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(Constants.TAG, "onCreate: BullsAndCowsApp");
        initImageLoaderConfiguration();
        DBConnector.initInstance(this);

//        new KeepUserOnlineUtil().setupOnlineStatusObserver();

        Fabric.with(this, new Crashlytics());
        registerActivityLifecycleCallbacks(new KeepUserOnlineUtil().getActivityLifeCycleCallback());
//        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
//
//            @Override
//            public void onActivityCreated(final Activity pActivity, final Bundle pBundle) {
//                mCountStartedActivities++;
//                Log.d(TAG, "onActivityResumed: ");
//            }
//
//            @Override
//            public void onActivityStarted(final Activity pActivity) {
//
//            }
//
//            @Override
//            public void onActivityResumed(final Activity pActivity) {
//
//            }
//
//            @Override
//            public void onActivityPaused(final Activity pActivity) {
//
//            }
//
//            @Override
//            public void onActivityStopped(final Activity pActivity) {
//
//            }
//
//            @Override
//            public void onActivitySaveInstanceState(final Activity pActivity, final Bundle pBundle) {
//
//            }
//
//            @Override
//            public void onActivityDestroyed(final Activity pActivity) {
//                mCountStartedActivities--;
//                Log.d(TAG, "onActivityPaused: ");
//                Log.d(TAG, "onActivityStopped: ");
//
//                if (mCountStartedActivities == 0) {
//                    UserLoginHolder.getInstance().setUserOffline();
//                    Log.d(TAG, "onActivityStopped: ");
//                } else {
//                    UserLoginHolder.getInstance().setUserOnline();
//                    Log.d(TAG, "onActivityStopped: ");
//                }
//            }
//        });
    }


    //TODO rename to init or similar
    private void initImageLoaderConfiguration() {
        Pen.getInstance().setLoaderSettings()
                .setContext(this)
                .setDefaultDrawable(getResources().getDrawable(R.drawable.ic_bull_big_size))
//                .setErrorDrawable(getResources().getDrawable(R.drawable.ic_image_no_load))
                .setSavingStrategy(Pen.SAVE_SCALING_IMAGE_STRATEGY)
                .setTypeOfCache(Pen.INNER_FILE_CACHE)
                .setSizeInnerFileCache(Constants.INNER_FILE_CACHE_SIZE_MB)
                .setQualityImageCompression(Constants.QUALITY_IMAGE_COMPRESSION)
                .setUp();
    }
}
