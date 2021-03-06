package com.example.notepad.bullsandcows.data.databases;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class DBConnector extends SQLiteOpenHelper {

    private static final String APP_DB_NAME = "bullsandcows.db";

    private static final int DB_VERSION = 1;

    private static DBConnector mInstance;

    private DBConnector(Context context) {
        super(context, APP_DB_NAME, null, DB_VERSION);
    }

    public static void initInstance(Context pContext){
        if(mInstance == null){
            mInstance = new DBConnector(pContext);
            Log.d("MyLogs", "Create instance of DBConnector");
        }
    }

    public static DBConnector getInstance(){
        return mInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //TODO foreach by tables
        //istin2007@gmail.com
        db.execSQL(Tables.CRATE_USERS_RECORDS_TABLE);
        db.execSQL(Tables.CRATE_USER_INFO_TABLE);
        db.execSQL(Tables.CRATE_CURRENT_USER_TABLE);
        Log.d("MyLogs", "Tables " + APP_DB_NAME + "Created;");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d("MyLogs", "Tables " + APP_DB_NAME + "Upgrade;");
    }
}
