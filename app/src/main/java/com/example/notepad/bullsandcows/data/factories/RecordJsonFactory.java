package com.example.notepad.bullsandcows.data.factories;

import com.example.notepad.bullsandcows.data.models.ResponseRecordModel;
import com.example.notepad.myapplication.backend.recordsToNetApi.model.RecordsToNet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RecordJsonFactory {

    public ResponseRecordModel getRecordsFromBackend(ResponseRecordModel pResponse) {

        ArrayList<RecordsToNet> listRecords;
        RecordsToNet recordModel;
        String jsonString = pResponse.getmJsonFromBackend();

        if (pResponse.getmRecordsArray() == null) {
            listRecords = new ArrayList<>();
        } else {
            listRecords = pResponse.getmRecordsArray();
        }

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

                try {
                    recordModel.setUserUrlPhoto(record.getString("userUrlPhoto"));
                } catch (Exception pE) {
                    pE.getStackTrace();
                    recordModel.setUserUrlPhoto(null);
                }

                listRecords.add(recordModel);
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
