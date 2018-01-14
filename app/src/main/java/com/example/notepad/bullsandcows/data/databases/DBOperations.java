package com.example.notepad.bullsandcows.data.databases;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.notepad.bullsandcows.data.databases.models.UserRecordsDB;

import static android.content.ContentValues.TAG;

public final class DBOperations {

    private final SQLiteOpenHelper mHelper;

    private static DBOperations mInstance;

    private DBOperations() {
        mHelper = DBConnector.getInstance();
    }

    public static DBOperations getInstance() {
        if (mInstance == null) {
            mInstance = new DBOperations();
        }
        return mInstance;
    }

    public synchronized long insert(final String pTableName, final ContentValues pValues) {

        final SQLiteDatabase database = mHelper.getReadableDatabase();
        database.beginTransaction();
        Log.d(TAG, "insert: start");
        long id = 0;

        try {
            id = database.insert(pTableName, null, pValues);
            database.setTransactionSuccessful();
            Log.d("MyLogs", "Add one new record in " + pTableName + pValues);
        } catch (final Exception pE) {
            pE.getStackTrace();
            Log.d("MyLogs", this.getClass().getSimpleName() + pE.getLocalizedMessage());
        } finally {
            database.endTransaction();
            Log.d(TAG, "insert: end");
            database.close();
        }
        return id;
    }

    public synchronized int update(final String pTableName, final ContentValues pValues) {
        final SQLiteDatabase database = mHelper.getReadableDatabase();
        database.beginTransaction();
        int result = 0;
        try {
            result = database.update(pTableName, pValues, "_id = ?", new String[]{pValues.getAsString(UserRecordsDB.ID)});
            database.setTransactionSuccessful();
            Log.d("MyLogs", "Add one new record in " + pTableName);
        } catch (final Exception pE) {
            pE.getStackTrace();
            Log.d("MyLogs", this.getClass().getSimpleName() + pE.getLocalizedMessage());
        } finally {
            database.endTransaction();
            database.close();
        }
        return result;
    }

    public synchronized Cursor query(final String table, final String[] columns, final String selection, final String[] selectionArgs, final String groupBy,
                                     final String having, final String sortOrder) {
        Log.d(TAG, "query: start");
        final SQLiteDatabase database = mHelper.getReadableDatabase();

        return database.query(table, columns, selection, selectionArgs, groupBy, having, sortOrder);
    }

    public synchronized int bulkInsert(final String pTableName, final ContentValues[] pArrayValues) {
        final SQLiteDatabase database = mHelper.getReadableDatabase();
        Log.d(TAG, "bulkInsert in DBOperation start: ");

        int successAdd = 0;

        for (final ContentValues contentValues : pArrayValues) {
            database.beginTransaction();
            Log.d(TAG, "bulkInsert: start inser one record");

            try {
                final long success = database.insert(pTableName, null, contentValues);
                database.setTransactionSuccessful();
                Log.d(TAG, "bulkInsert: success" + success);

                if (success > 0) {
                    ++successAdd;
                }
            } catch (final Exception pE) {
                pE.getStackTrace();
            } finally {
                database.endTransaction();
                Log.d(TAG, "bulkInsert: end insert one record");
            }
        }
        database.close();
        Log.d(TAG, "bulkInsert end: total adds: " + successAdd);
        return successAdd;
    }
}
