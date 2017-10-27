package com.example.notepad.bullsandcows.services;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.notepad.bullsandcows.OnlineRecords;

import java.util.concurrent.TimeUnit;

public class RefreshOnlineRecordService extends IntentService {

    public static final String TAG = "MyLogs";
    public static final String ACTION_REFRESH_ONLINE_RECORD_SERVICE = "com.example.notepad.bullsandcows.OnlineRecords.Response";
    public static final String SERVICE_RESPONSE_KEY = "Service_response_key";
    private String mString;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate RefreshOnlineRecordService");
    }

    public RefreshOnlineRecordService() {
        super("RefreshOnlineRecordService");
    }

    @Override
    protected void onHandleIntent(Intent pIntent) {
        String text = pIntent.getStringExtra(OnlineRecords.TEXT_FOR_INTENT);
        Log.d(TAG, "onHandleIntent" + text);
        try {
            TimeUnit.SECONDS.sleep(5);
            //My service hard work or do something
        } catch (InterruptedException pE) {
            pE.printStackTrace();
        }
        Intent response = new Intent();
        response.setAction(ACTION_REFRESH_ONLINE_RECORD_SERVICE);
        response.addCategory(Intent.CATEGORY_DEFAULT);
        mString = "Service end work. Don't have new records";
        response.putExtra(SERVICE_RESPONSE_KEY, mString);
        sendBroadcast(response);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy RefreshOnlineRecordService");
    }
}
