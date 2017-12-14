package com.example.notepad.bullsandcows.data.databases;

import com.example.notepad.bullsandcows.data.databases.models.UserRecordsDB;

public class Tables {

    public static final String DESC = " DESC";

    public static final String ASC = " ASC";

    public static final String CRATE_USERS_RECORDS_TABLE =
            "CREATE TABLE IF NOT EXISTS " +
                    UserRecordsDB.TABLE + "(" +
                    UserRecordsDB.ID + " BIGINT PRIMARY KEY, " +
                    UserRecordsDB.CODES + " INTEGER, " +
                    UserRecordsDB.NIK_NAME + " TEXT, " +
                    UserRecordsDB.MOVES + " INTEGER, " +
                    UserRecordsDB.TIME + " TEXT, " +
                    UserRecordsDB.USER_PHOTO_URL + " TEXT, " +
                    UserRecordsDB.IS_UPDATE_ONLINE + " TEXT)";
}
