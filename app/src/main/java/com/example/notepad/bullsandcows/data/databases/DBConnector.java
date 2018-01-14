package com.example.notepad.bullsandcows.data.databases;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.notepad.bullsandcows.data.databases.models.CurrentUserDB;
import com.example.notepad.bullsandcows.data.databases.models.UserInfoDB;
import com.example.notepad.bullsandcows.data.databases.models.UserRecordsDB;

public final class DBConnector extends SQLiteOpenHelper {

    private static final String APP_DB_NAME = "bullsandcows.db";

    private static final int DB_VERSION = 1;

    private static DBConnector mInstance;

    private DBConnector(final Context context) {
        super(context, APP_DB_NAME, null, DB_VERSION);
    }

    public static void initInstance(final Context pContext){
        if(mInstance == null){
            mInstance = new DBConnector(pContext);
            Log.d("MyLogs", "Create instance of DBConnector");
        }
    }

    public static DBConnector getInstance(){
        return mInstance;
    }

    @Override
    public void onCreate(final SQLiteDatabase db) {
        //TODO foreach by tables
        //istin2007@gmail.com
        db.execSQL(UserRecordsDB.CRATE_USERS_RECORDS_TABLE);
        db.execSQL(UserInfoDB.CRATE_USER_INFO_TABLE);
        db.execSQL(CurrentUserDB.CRATE_CURRENT_USER_TABLE);
        Log.d("MyLogs", "Tables " + APP_DB_NAME + "Created;");
    }

    @Override
    public void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
        Log.d("MyLogs", "Tables " + APP_DB_NAME + "Upgrade;");
    }
}
