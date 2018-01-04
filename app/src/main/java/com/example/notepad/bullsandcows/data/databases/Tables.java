package com.example.notepad.bullsandcows.data.databases;

import com.example.notepad.bullsandcows.data.databases.models.CurrentUserDB;
import com.example.notepad.bullsandcows.data.databases.models.UserInfoDB;
import com.example.notepad.bullsandcows.data.databases.models.UserRecordsDB;

public class Tables {

    public static final String DESC = " DESC";

    public static final String ASC = " ASC";

    public static final String LAST_DAY = "Last day";

    public static final String LAST_WEEK = "Last week";

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

    public static final String CRATE_USER_INFO_TABLE = "CREATE TABLE IF NOT EXISTS " +
            UserInfoDB.TABLE + "(" +
            UserInfoDB.ID + " TEXT PRIMARY KEY, " +
            UserInfoDB.COUNTRY_FLAG + " INTEGER, " +
            UserInfoDB.COUNTRY + " TEXT, " +
            UserInfoDB.AGE + " INTEGER, " +
            UserInfoDB.EMAIL + " TEXT, " +
            UserInfoDB.DESCRIPTION + " BLOB, " +
            UserInfoDB.AVATAR_URL + " TEXT, " +
            UserInfoDB.SEX + " TEXT, " +
            UserInfoDB.NUMBER_OF_PLAYED_GAMES + " INTEGER, " +
            UserInfoDB.REGISTRATION_TIME + " BIGINT, " +
            UserInfoDB.LAST_VISIT + " BIGINT, " +
            UserInfoDB.USER_FRIENDS + " BLOB, " +
            UserInfoDB.USER_MESSAGES + " BLOB, " +
            UserInfoDB.USERS_BEST_RECORDS + " BLOB, " +
            UserInfoDB.USER_LAST_RECORDS + " BLOB, " +
            UserInfoDB.IS_ONLINE + " INT)";

    public static final String CRATE_CURRENT_USER_TABLE =
            "CREATE TABLE IF NOT EXISTS " +
                    CurrentUserDB.TABLE + "(" +
                    CurrentUserDB.ID + " BIGINT PRIMARY KEY, " +
                    CurrentUserDB.USER_NAME + " TEXT, " +
                    CurrentUserDB.IS_KIPPING_LOGIN + " INTEGER, " +
                    CurrentUserDB.LAST_USER_VISIT + " BIGINT, " +
                    CurrentUserDB.TOKEN + " TEXT)";
}
