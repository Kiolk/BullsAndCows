package com.example.notepad.bullsandcows.utils.converters;

import android.content.ContentValues;
import android.util.Log;

import com.example.notepad.bullsandcows.data.databases.models.UserInfoDB;
import com.example.notepad.bullsandcows.data.databases.models.UserRecordsDB;
import com.example.notepad.bullsandcows.utils.Constants;
import com.example.notepad.myapplication.backend.recordsToNetApi.model.RecordsToNet;
import com.example.notepad.myapplication.backend.userDataBaseApi.model.BestUserRecords;
import com.example.notepad.myapplication.backend.userDataBaseApi.model.UserDataBase;

import java.util.ArrayList;

public class ModelConverterUtil {

    public static final int TRUE = 1;
    public static final int FALSE = 0;

    public static ContentValues fromRecordToNetToCv(RecordsToNet pRecord) {

        ContentValues cv = new ContentValues();

        cv.put(UserRecordsDB.ID, pRecord.getDate());
        cv.put(UserRecordsDB.NIK_NAME, pRecord.getNikName());
        cv.put(UserRecordsDB.MOVES, Integer.parseInt(pRecord.getMoves()));
        cv.put(UserRecordsDB.CODES, Integer.parseInt(pRecord.getCodes()));
        cv.put(UserRecordsDB.TIME, pRecord.getTime());
        if (pRecord.getUserUrlPhoto() != null) {
            cv.put(UserRecordsDB.USER_PHOTO_URL, pRecord.getUserUrlPhoto());
        }
        return cv;
    }

    public static BestUserRecords fromRecordToNetToBestUserRecords(RecordsToNet pRecord) {

        BestUserRecords recordForCheck = new BestUserRecords();

        recordForCheck.setCodes(pRecord.getCodes());
        recordForCheck.setDate(pRecord.getDate());
        recordForCheck.setMoves(pRecord.getMoves());
        recordForCheck.setNikName(pRecord.getNikName());
        recordForCheck.setTime(pRecord.getTime());

        return recordForCheck;
    }

    public static ContentValues[] fromArrayRecordToNetToCv(ArrayList<RecordsToNet> pArrayRecords) {
        ContentValues[] arrayContentValues = new ContentValues[pArrayRecords.size()];
        int i = 0;

        for (RecordsToNet note : pArrayRecords) {

            arrayContentValues[i] = fromRecordToNetToCv(note);

            ++i;
        }
        return arrayContentValues;
    }

    public static ContentValues fromUserDataBaseToCv(UserDataBase pUserInfo) {
        ContentValues cv = new ContentValues();
        cv.put(UserInfoDB.ID, pUserInfo.getUserName());
        cv.put(UserInfoDB.COUNTRY, pUserInfo.getCountry());
        cv.put(UserInfoDB.SEX, pUserInfo.getMSex());
        cv.put(UserInfoDB.AGE, pUserInfo.getMAge());
        cv.put(UserInfoDB.AVATAR_URL, pUserInfo.getMPhotoUrl());
        cv.put(UserInfoDB.NUMBER_OF_PLAYED_GAMES, pUserInfo.getMNumberPlayedGames());
        cv.put(UserInfoDB.COUNTRY_FLAG, pUserInfo.getMCountryFlag());
        cv.put(UserInfoDB.EMAIL, pUserInfo.getEmail());
        cv.put(UserInfoDB.DESCRIPTION, pUserInfo.getMShortDescription());
        cv.put(UserInfoDB.LAST_VISIT, pUserInfo.getMLastUserVisit());
        cv.put(UserInfoDB.REGISTRATION_TIME, pUserInfo.getMUserRegistrationTime());
        //tine place, possible something
        if(pUserInfo.getIsOnline()) {
            cv.put(UserInfoDB.IS_ONLINE, TRUE);
        }
        else {
            cv.put(UserInfoDB.IS_ONLINE, FALSE);
        }
        cv.put(UserInfoDB.USER_FRIENDS, String.valueOf(pUserInfo.getUserFriends()));
        Log.d(Constants.TAG, "fromUserDataBaseToCv:  " + String.valueOf(pUserInfo.getUserFriends()));
        cv.put(UserInfoDB.USER_MESSAGES, String.valueOf(pUserInfo.getUserMessages()));
        cv.put(UserInfoDB.USERS_BEST_RECORDS, String.valueOf(pUserInfo.getBestUserRecords()));
        cv.put(UserInfoDB.USER_LAST_RECORDS, String.valueOf(pUserInfo.getLastFiveUserRecords()));
        return cv;
    }
}
