package com.example.notepad.bullsandcows.utils;

import com.example.notepad.bullsandcows.data.databases.Tables;
import com.example.notepad.bullsandcows.data.databases.models.UserRecordsDB;

public class Constants {

    public interface IntentKeys {
        String RECORDS_FROM_BACKEND_ON_DAY = "RecordsFromBackendOnDay";
    }

    public interface ContentProvidersConstant{
       String SORT_ITEM_BY_TIME = UserRecordsDB.TIME + Tables.ASC;
       String SORT_ITEM_BY_MOVES = UserRecordsDB.MOVES + Tables.ASC;
       String SORT_ITEM_BY_MOVES_TIME = SORT_ITEM_BY_MOVES + " , " + SORT_ITEM_BY_TIME;
    }

    public final static String TAG = "MyLogs";
    public final static String FOLDER_DESTINATION_IN_SD = "Download";
    public static final String NIK_NAME_OF_USER = "nikNameOfUser";
    public static final String PASSWORD_OF_USER = "passwordOfUser";
    public static final String CODED_DIGITS = "codedNumberOfDigits";
    public static final String MODE_STATE = "modeState";
    public static final String REGISTRATION_NAME_OF_USER = "nameOfUser";
    public static final String REGISTRATION_PASSWORD = "password";
    public static final String EMPTY_STRING = "";
    public static final long BACK_EPOCH_TIME_NOTATION = 3008586288000L;
    public static final long INNER_FILE_CACHE_SIZE_MB = 10L;
    public static final int QUALITY_IMAGE_COMPRESSION = 70;
    public static final int INT_FALSE_VALUE = 0;
    public static final int INT_TRUE_VALUE = 1;

}
