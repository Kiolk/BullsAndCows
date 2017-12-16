package com.example.notepad.bullsandcows.services;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.notepad.bullsandcows.data.databases.DBOperations;
import com.example.notepad.bullsandcows.data.databases.models.UserRecordsDB;
import com.example.notepad.bullsandcows.data.managers.RecordsManager;
import com.example.notepad.bullsandcows.data.models.RequestRecordModel;
import com.example.notepad.bullsandcows.data.models.ResponseRecordModel;
import com.example.notepad.myapplication.backend.recordsToNetApi.model.RecordsToNet;

import java.util.ArrayList;

import static com.example.notepad.bullsandcows.utils.Constants.TAG;

public class WaiterNewRecordsService extends IntentService {

    public static final String WAITER_NEW_RECORDS = "waiterNewRecords";

    private RecordsManager mRecordsManager;

    private String mCursorString;
    private ContentValues[] mArrayContentValues;
    private RequestRecordModel mRequest;

    public WaiterNewRecordsService() {
        super(WAITER_NEW_RECORDS);
        Log.d(TAG, "Start constructor WaiterNewRecordsService");
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        mRecordsManager.getRecordSBackend(mRequest);
        return super.onStartCommand(intent, flags, startId);

    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate service start");
        initRecordManager();
        mRequest = new RequestRecordModel(mCursorString);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.d(TAG, "onHandleIntent: ");
    }

    private void initRecordManager() {
        mRecordsManager = new RecordsManager() {

            @Override
            public ResponseRecordModel getResponseBackendCallback(ResponseRecordModel pResponse) {
                ResponseRecordModel response = super.getResponseBackendCallback(pResponse);
                Log.d(TAG, ", cursor :" + response.getmCursor());
                ArrayList<RecordsToNet> recordModelArrayList = response.getmRecordsArray();

                mArrayContentValues = new ContentValues[recordModelArrayList.size()];
                int i = 0;
                for (RecordsToNet note : recordModelArrayList) {
                    ContentValues cv = new ContentValues();

                    cv.put(UserRecordsDB.ID, note.getDate());
                    cv.put(UserRecordsDB.NIK_NAME, note.getNikName());
                    cv.put(UserRecordsDB.MOVES, Integer.parseInt(note.getMoves()));
                    cv.put(UserRecordsDB.CODES, Integer.parseInt(note.getCodes()));
                    cv.put(UserRecordsDB.TIME, note.getTime());
                    cv.put(UserRecordsDB.USER_PHOTO_URL, note.getUserUrlPhoto());

                    mArrayContentValues[i] = cv;

                    ++i;
                }

                mCursorString = response.getmCursor();

                int insert = new DBOperations().bulkInsert(UserRecordsDB.TABLE, mArrayContentValues);
                Log.d(TAG, "Insert records: " + insert);

                return response;
            }
        };
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy: ");
        super.onDestroy();
    }
}
