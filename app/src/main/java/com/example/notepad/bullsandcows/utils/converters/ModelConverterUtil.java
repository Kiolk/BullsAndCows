package com.example.notepad.bullsandcows.utils.converters;

import android.content.ContentValues;

import com.example.notepad.bullsandcows.data.databases.models.UserRecordsDB;
import com.example.notepad.myapplication.backend.recordsToNetApi.model.RecordsToNet;
import com.example.notepad.myapplication.backend.userDataBaseApi.model.BestUserRecords;

import java.util.ArrayList;

public class ModelConverterUtil {

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
}
