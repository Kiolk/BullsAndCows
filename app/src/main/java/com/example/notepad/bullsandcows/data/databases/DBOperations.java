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
import com.example.notepad.bullsandcows.utils.Converters;

public class DBOperations {

    private SQLiteOpenHelper mHelper;

    public DBOperations() {
        mHelper = DBConnector.getInstance();
    }

    public void insert(final String pTableName, final String pColumnHack, final ContentValues pValues) {

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
            database.close();
        }
    }

    public Cursor query() {
        SQLiteDatabase database = mHelper.getReadableDatabase();

        return database.query(UserRecordsDB.TABLE, null, null,
                null, null, null, UserRecordsDB.ID + Tables.ASC, null);
    }

    public Cursor queryForSortRecords(String[] pArrayString) {
        SQLiteDatabase database = mHelper.getReadableDatabase();

        StringBuilder builderSelection = new StringBuilder();
        builderSelection.append(UserRecordsDB.NIK_NAME);

        if (!pArrayString[0].equals("")) {
            builderSelection.append(" = ?");
        } else {
            builderSelection.append(" is not ?");
        }

        builderSelection.append(" and ");
        builderSelection.append(UserRecordsDB.CODES);

        if (!pArrayString[1].equals("Eny")) {
            builderSelection.append(" = ?");
        } else {
            builderSelection.append(" is not ?");
        }

        builderSelection.append(" and ");
        builderSelection.append(UserRecordsDB.ID);

        if (pArrayString[2].equals("Last day")) {
            builderSelection.append(" < ? ");
            pArrayString[2] = String.valueOf(Converters.getActualDay(System.currentTimeMillis()));
        } else if (pArrayString[2].equals("Last week")) {
            builderSelection.append(" < ? ");
            pArrayString[2] = String.valueOf(Converters.getActualWeek(System.currentTimeMillis()));
        } else if (pArrayString[2].equals("Eny")) {
            builderSelection.append(" is not ? ");
        } else {
            builderSelection.append(" is not ? ");
        }

        return database.query(UserRecordsDB.TABLE, null, builderSelection.toString(),
                pArrayString, null, null, UserRecordsDB.ID + Tables.ASC, null);
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
}
