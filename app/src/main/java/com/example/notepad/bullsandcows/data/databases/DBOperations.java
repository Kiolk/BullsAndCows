package com.example.notepad.bullsandcows.data.databases;

import android.content.ContentValues;

public class DBOperations {

    public long insert(String pTableName, ContentValues pValues){
       return DBConnector.getInstance().getReadableDatabase().insert(pTableName, null, pValues);
    }
}
