package com.example.notepad.bullsandcows.data.databases;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.notepad.bullsandcows.data.databases.models.UserRecordsDB;

public class DBOperations {

    private SQLiteOpenHelper mHelper;

    public DBOperations() {
        mHelper = DBConnector.getInstance();
    }

    public long insert(String pTableName, ContentValues pValues) {

        SQLiteDatabase database = mHelper.getReadableDatabase();
        long idInsert = 0L;

        database.beginTransaction();

        try {
            idInsert = database.insert(pTableName, null, pValues);
            database.setTransactionSuccessful();
            Log.d("MyLogs", "Add one new record in UserRecordsDB");
        } catch (Exception pE) {
            pE.getStackTrace();
            Log.d("MyLogs", this.getClass().getSimpleName() + pE.getLocalizedMessage());
        } finally {
            database.endTransaction();
        }

        return idInsert;
    }

    public Cursor query(){
        SQLiteDatabase database = mHelper.getReadableDatabase();

        return database.query(UserRecordsDB.TABLE, null, null,
                null, null, null, UserRecordsDB.ID + Tables.ASC, null);
    }

    public int bulkInsert(String pTableName, ContentValues [] pArrayValues){
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
