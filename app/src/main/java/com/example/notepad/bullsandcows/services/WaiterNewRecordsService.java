package com.example.notepad.bullsandcows.services;

import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.notepad.bullsandcows.data.managers.RecordsCallbacks;
import com.example.notepad.bullsandcows.data.managers.RecordsManager;
import com.example.notepad.bullsandcows.data.models.ResponseRecordModel;
import com.example.notepad.bullsandcows.data.providers.RecordsContentProvider;
import com.example.notepad.bullsandcows.utils.converters.ModelConverterUtil;

import static com.example.notepad.bullsandcows.utils.Constants.IntentKeys.RECORDS_FROM_BACKEND_ON_DAY;
import static com.example.notepad.bullsandcows.utils.Constants.TAG;

public class WaiterNewRecordsService extends Service {

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand: ");
        Long onDay = null;

        if (intent != null) {
            onDay = intent.getLongExtra(RECORDS_FROM_BACKEND_ON_DAY, System.currentTimeMillis());
        }

        RecordsManager recordsManager = new RecordsManager();
        recordsManager.getRecordsFromBackend(onDay, new RecordsCallbacks() {

            @Override
            public void getRecordsBackendCallback(final ResponseRecordModel pResponse) {

                if (pResponse.getmRecordsArray() != null) {
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            ContentValues[] arrayValues = ModelConverterUtil
                                    .fromArrayRecordToNetToCv(pResponse.getmRecordsArray());
                            getContentResolver().bulkInsert(RecordsContentProvider.CONTENT_URI, arrayValues);

                        }
                    });
                    thread.start();
                }
                stopSelf();
            }
        });

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate service start");
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy: ");
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind: ");
        return null;
    }
}
