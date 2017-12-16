package com.example.notepad.bullsandcows.data.databases;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.notepad.bullsandcows.data.databases.dblisteners.CursorListener;
import com.example.notepad.bullsandcows.data.databases.models.UserRecordsDB;


import java.util.logging.LogRecord;

public class DBOperations {

    private SQLiteOpenHelper mHelper;

    public DBOperations() {
        mHelper = DBConnector.getInstance();
    }

    public void insert(final String pTableName, final String pColumnHack, final ContentValues pValues) {
        final Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                Log.d("MyLogs", "Start thread: "
                        + Thread.currentThread().getName()
                        + " for add new record to DB");

                SQLiteDatabase database = mHelper.getReadableDatabase();

                database.beginTransaction();

                try {
                    database.insert(pTableName, pColumnHack, pValues);
                    database.setTransactionSuccessful();
                    Log.d("MyLogs", "Add one new record in UserRecordsDB");
                } catch (Exception pE) {
                    pE.getStackTrace();
                    Log.d("MyLogs", this.getClass().getSimpleName() + pE.getLocalizedMessage());
                } finally {
                    database.endTransaction();
                }

            }
        });

        thread.start();
    }

    public Cursor query() {
        SQLiteDatabase database = mHelper.getReadableDatabase();


        return database.query(UserRecordsDB.TABLE, null, null,
                null, null, null, UserRecordsDB.ID + Tables.ASC, null);
//        return database.query(UserRecordsDB.TABLE, null, UserRecordsDB.NIK_NAME + " = ?",
//                new String[]{"Gor"}, null, null, UserRecordsDB.ID + Tables.ASC, null);
    }

    public void query(final CursorListener pListener) {
        @SuppressLint("HandlerLeak") final Handler queryHandler = new Handler() {
            ;

            @Override
            public void handleMessage(Message msg) {
                Cursor cursor = (Cursor) msg.obj;
                pListener.getCursorListener(cursor);

            }
        };

        Thread queryThread = new Thread(new Runnable() {
            @Override
            public void run() {
                SQLiteDatabase database = mHelper.getReadableDatabase();

                Log.d("MyLogs", "Start thread: "
                        + Thread.currentThread().getName()
                        + " for add get Cursor");

                Cursor cursor = database.query(UserRecordsDB.TABLE, null, null,
                        null, null, null, UserRecordsDB.ID + Tables.ASC, null);
                Message msg = new Message();
                msg.obj = cursor;
                queryHandler.sendMessage(msg);
            }
        });

        queryThread.start();
    }

    public int bulkInsert(String pTableName, ContentValues[] pArrayValues) {
        SQLiteDatabase database = mHelper.getReadableDatabase();

        int successAdd = 0;
        for (ContentValues contentValues : pArrayValues) {
            database.beginTransaction();

            try {
                database.insert(pTableName, null, contentValues);
                database.setTransactionSuccessful();
                ++successAdd;
            } catch (Exception pE) {
                pE.getStackTrace();
            } finally {
                database.endTransaction();
            }
        }

        return successAdd;
    }
//
//    @Override
//    public Cursor getCursorListener(Cursor pCursor) {
//        return null;
//    }
}
