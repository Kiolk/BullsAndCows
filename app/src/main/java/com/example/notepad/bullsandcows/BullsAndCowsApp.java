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

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(Constants.TAG, "onCreate: BullsAndCowsApp");
        initImageLoaderConfiguration();
        DBConnector.initInstance(this);

        Fabric.with(this, new Crashlytics());

        registerActivityLifecycleCallbacks(new KeepUserOnlineUtil().getActivityLifeCycleCallback());
    }

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
