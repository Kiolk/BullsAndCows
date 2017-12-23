package com.example.notepad.bullsandcows.data.databases;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.notepad.bullsandcows.data.databases.models.UserRecordsDB;

import static android.content.ContentValues.TAG;

public class DBOperations {

    private SQLiteOpenHelper mHelper;

    public DBOperations() {
        mHelper = DBConnector.getInstance();
    }

    public long insert(final String pTableName, final ContentValues pValues) {

        SQLiteDatabase database = mHelper.getReadableDatabase();
        database.beginTransaction();
        long id = 0;

        try {
            id = database.insert(pTableName, null, pValues);
            database.setTransactionSuccessful();
            Log.d("MyLogs", "Add one new record in UserRecordsDB");
        } catch (Exception pE) {
            pE.getStackTrace();
            Log.d("MyLogs", this.getClass().getSimpleName() + pE.getLocalizedMessage());
        } finally {
            database.endTransaction();
            database.close();
        }
        return id;
    }

    public void update(final String pTableName, final ContentValues pValues) {
        SQLiteDatabase database = mHelper.getReadableDatabase();
        database.beginTransaction();
        try {
            database.update(pTableName, pValues, UserRecordsDB.ID + " = " + pValues.getAsString(UserRecordsDB.ID), null);
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

    public Cursor query(String table, String[] columns, String selection, String[] selectionArgs, String groupBy,
                        String having, String sortOrder) {

        SQLiteDatabase database = mHelper.getReadableDatabase();

        return database.query(table, columns, selection, selectionArgs, groupBy, having, sortOrder);
    }

    public int bulkInsert(String pTableName, ContentValues[] pArrayValues) {
        SQLiteDatabase database = mHelper.getReadableDatabase();
        Log.d(TAG, "bulkInsert in DBOperation start: ");

        int successAdd = 0;

        for (ContentValues contentValues : pArrayValues) {
            database.beginTransaction();

            try {
                long success = database.insert(pTableName, null, contentValues);
                database.setTransactionSuccessful();
                Log.d(TAG, "bulkInsert: success" + success);

                if (success > 0) {
                    ++successAdd;
                }
            } catch (Exception pE) {
                pE.getStackTrace();
            } finally {
                database.endTransaction();
            }
        }
        database.close();
        Log.d(TAG, "bulkInsert: total adds: " + successAdd);
        return successAdd;
    }
}
