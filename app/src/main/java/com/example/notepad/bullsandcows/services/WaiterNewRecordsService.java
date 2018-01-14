package com.example.notepad.bullsandcows.services;

import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.notepad.bullsandcows.data.managers.OnResultCallback;
import com.example.notepad.bullsandcows.data.managers.RecordsManager;
import com.example.notepad.bullsandcows.data.models.ResponseRecordModel;
import com.example.notepad.bullsandcows.data.providers.RecordsContentProvider;
import com.example.notepad.bullsandcows.utils.converters.ModelConverterUtil;

import static com.example.notepad.bullsandcows.utils.Constants.IntentKeys.RECORDS_FROM_BACKEND_ON_DAY;
import static com.example.notepad.bullsandcows.utils.Constants.TAG;

public class WaiterNewRecordsService extends Service {

    @Override
    public int onStartCommand(@Nullable final Intent intent, final int flags, final int startId) {
        Log.d(TAG, "onStartCommand: ");
        Long onDay = null;

        if (intent != null) {
            onDay = intent.getLongExtra(RECORDS_FROM_BACKEND_ON_DAY, System.currentTimeMillis());
        }

        final RecordsManager recordsManager = new RecordsManager();
        recordsManager.getRecordsFromBackend(onDay, new OnResultCallback<ResponseRecordModel>() {

            @Override
            public void onSuccess(final ResponseRecordModel pResult) {
                final Thread thread = new Thread(new Runnable() {

                    @Override
                    public void run() {
                        final ContentValues[] arrayValues = ModelConverterUtil

                                .fromArrayRecordToNetToCv(pResult.getRecordsArray());
                        getContentResolver().bulkInsert(RecordsContentProvider.CONTENT_URI, arrayValues);

                    }
                });
                thread.start();
                stopSelf();
            }

            @Override
            public void onError(final Exception pException) {
                stopSelf();
            }
        });

        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(final Intent intent) {
        Log.d(TAG, "onBind: ");
        return null;
    }
}
