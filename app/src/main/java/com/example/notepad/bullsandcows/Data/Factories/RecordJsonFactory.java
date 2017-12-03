package com.example.notepad.bullsandcows.Data.Factories;

import com.example.notepad.bullsandcows.Data.Models.ResponseRecordModel;
import com.example.notepad.bullsandcows.Utils.Converters;
import com.example.notepad.myapplication.backend.recordsToNetApi.model.RecordsToNet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RecordJsonFactory {

    public static ArrayList<RecordsToNet> getArrayRecordsFromJson(String jsonString) {
        ArrayList<RecordsToNet> listRecords = new ArrayList<>();
        RecordsToNet recordModel;

        try {
            JSONObject listOfRecords = new JSONObject(jsonString);
            JSONArray detailsOneRecord = listOfRecords.getJSONArray("items");
            String cursor = listOfRecords.getString("nextPageToken");
            int arrayIndex = detailsOneRecord.length();
            for (int i = 0; i < arrayIndex; ++i) {
                recordModel = new RecordsToNet();
                JSONObject record = detailsOneRecord.getJSONObject(i);
                recordModel.setCodes(record.getString("codes"));
                Long dateOfRecord = record.getLong("date");
                String date = Converters.convertTimeToString(dateOfRecord);
                recordModel.setDate(dateOfRecord);
                recordModel.setNikName(record.getString("nikName"));
                recordModel.setMoves(record.getString("moves"));
                recordModel.setTime(record.getString("time"));
                listRecords.add(i, recordModel);
            }
            return listRecords;
        } catch (JSONException pE) {
            pE.printStackTrace();
        }
        return null;
    }

    public ResponseRecordModel getRecordsFromBackend(ResponseRecordModel pResponse) {

        ArrayList<RecordsToNet> listRecords = new ArrayList<>();
        RecordsToNet recordModel;
        String jsonString = pResponse.getmJsonFromBackend();

        try {
            JSONObject listOfRecords = new JSONObject(jsonString);
            JSONArray detailsOneRecord = listOfRecords.getJSONArray("items");
            String cursor = listOfRecords.getString("nextPageToken");
            int arrayIndex = detailsOneRecord.length();
            for (int i = 0; i < arrayIndex; ++i) {
                recordModel = new RecordsToNet();
                JSONObject record = detailsOneRecord.getJSONObject(i);
                recordModel.setCodes(record.getString("codes"));
                Long dateOfRecord = record.getLong("date");
//                String date = Converters.convertTimeToString(dateOfRecord);
                recordModel.setDate(dateOfRecord);
                recordModel.setNikName(record.getString("nikName"));
                recordModel.setMoves(record.getString("moves"));
                recordModel.setTime(record.getString("time"));
                listRecords.add(i, recordModel);
            }
            pResponse.setmRecordsArray(listRecords);
            pResponse.setmCursor(cursor);
            return pResponse;
        } catch (JSONException pE) {
            pE.printStackTrace();
            pResponse.setmException(pE);
            return pResponse;
        }
    }
}
