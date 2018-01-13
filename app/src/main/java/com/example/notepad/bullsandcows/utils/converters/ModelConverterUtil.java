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
import java.util.List;

//TODO split to small ones. Some of them related to DB some of them to JSON
public final class ModelConverterUtil {

    //TODO move to classes related to SQL or DB
    public static final int TRUE = 1;
    public static final int FALSE = 0;

    //TODO create separate class and move bussines
    public static ContentValues fromRecordToNetToCv(final RecordsToNet pRecord) {
        final ContentValues cv = new ContentValues();

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

    public static BestUserRecords fromRecordToNetToBestUserRecords(final RecordsToNet pRecord) {

        final BestUserRecords recordForCheck = new BestUserRecords();

        recordForCheck.setCodes(pRecord.getCodes());
        recordForCheck.setDate(pRecord.getDate());
        recordForCheck.setMoves(pRecord.getMoves());
        recordForCheck.setNikName(pRecord.getNikName());
        recordForCheck.setTime(pRecord.getTime());

        return recordForCheck;
    }

    //TODO method name is not clear
    public static ContentValues[] fromArrayRecordToNetToCv(final List<RecordsToNet> pArrayRecords) {
        final ContentValues[] arrayContentValues = new ContentValues[pArrayRecords.size()];
        int i = 0;

        for (final RecordsToNet note : pArrayRecords) {

            arrayContentValues[i] = fromRecordToNetToCv(note);

            ++i;
        }
        return arrayContentValues;
    }

    public static ContentValues fromUserDataBaseToCv(final UserDataBase pUserInfo) {
        final ContentValues cv = new ContentValues();
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
        if(pUserInfo.getIsOnline()) {
            cv.put(UserInfoDB.IS_ONLINE, TRUE);
        }
        else {
            cv.put(UserInfoDB.IS_ONLINE, FALSE);
        }
        cv.put(UserInfoDB.USER_FRIENDS, String.valueOf(pUserInfo.getUserFriends()));
        Log.d(Constants.TAG, "fromUserDataBaseToCv:  " + pUserInfo.getUserFriends());
        cv.put(UserInfoDB.USER_MESSAGES, String.valueOf(pUserInfo.getUserMessages()));
        cv.put(UserInfoDB.USERS_BEST_RECORDS, String.valueOf(pUserInfo.getBestUserRecords()));
        cv.put(UserInfoDB.USER_LAST_RECORDS, String.valueOf(pUserInfo.getLastFiveUserRecords()));
        return cv;
    }
}
