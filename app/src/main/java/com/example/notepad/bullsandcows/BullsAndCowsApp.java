package com.example.notepad.bullsandcows;

import android.app.Application;
import android.util.Log;

import com.example.notepad.bullsandcows.data.databases.DBConnector;
import com.example.notepad.bullsandcows.utils.Constants;

import kiolk.com.github.pen.Pen;


public class BullsAndCowsApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(Constants.TAG, "onCreate: BullsAndCowsApp");
        setImageLoaderConfiguration();
        DBConnector.initInstance(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        Log.d(Constants.TAG, "onTerminate: BullsAndCowsApp");
    }

    private void setImageLoaderConfiguration() {
        Pen.getInstance().setLoaderSettings()
                .setContext(this)
                .setDefaultDrawable(getResources().getDrawable(R.drawable.ic_bull_big_size))
                .setErrorDrawable(getResources().getDrawable(R.drawable.ic_image_no_load))
                .setSavingStrategy(Pen.SAVE_SCALING_IMAGE_STRATEGY)
                .setTypeOfCache(Pen.INNER_FILE_CACHE)
                .setSizeInnerFileCache(Constants.INNER_FILE_CACHE_SIZE_MB)
                .setQualityImageCompression(Constants.QUALITY_IMAGE_COMPRESSION)
                .setUp();
        Log.d(Constants.TAG, "setImageLoaderConfiguration: ");
    }
}
