package com.example.notepad.bullsandcows.data.databases.models;

//TODO add method convertToContentValues() maybe move to separate interface
public class UserRecordsDB {

    public static final String TABLE = "Records";

    public static final String ID = "_id";

    public static final String NIK_NAME = "nik";

    public static final String CODES = "codes";

    public static final String TIME = "time";

    public static final String MOVES = "moves";

    public static final String USER_PHOTO_URL = "url";

    public static final String IS_UPDATE_ONLINE = "updateRecords";

    public static final String UPDATE_ONLINE_HACK = "1";

    public static final String NOT_UPDATE_ONLINE_HACK = "0";

    public static final String[] AVAILABLE_COLUMNS = {ID, NIK_NAME,
            CODES, TIME, MOVES, USER_PHOTO_URL, IS_UPDATE_ONLINE};
}
