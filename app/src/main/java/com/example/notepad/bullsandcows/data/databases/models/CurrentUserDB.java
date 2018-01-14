package com.example.notepad.bullsandcows.data.databases.models;

public class CurrentUserDB {

    public static final String TABLE = "CurrentUser";

    public static final String ID = "_id";

    public static final String USER_NAME = "UserName";

    public static final String TOKEN = "Token";

    public static final String IS_KIPPING_LOGIN = "KeepLogin";

    public static final String LAST_USER_VISIT = "LastUserVisit";

    public static final String CRATE_CURRENT_USER_TABLE =
            "CREATE TABLE IF NOT EXISTS " +
                    CurrentUserDB.TABLE + "(" +
                    CurrentUserDB.ID + " BIGINT PRIMARY KEY, " +
                    CurrentUserDB.USER_NAME + " TEXT, " +
                    CurrentUserDB.IS_KIPPING_LOGIN + " INTEGER, " +
                    CurrentUserDB.LAST_USER_VISIT + " BIGINT, " +
                    CurrentUserDB.TOKEN + " TEXT)";
}
