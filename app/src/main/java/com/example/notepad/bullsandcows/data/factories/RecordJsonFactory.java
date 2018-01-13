package com.example.notepad.bullsandcows.data.factories;

import com.example.notepad.bullsandcows.data.models.ResponseRecordModel;
import com.example.notepad.myapplication.backend.recordsToNetApi.model.RecordsToNet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RecordJsonFactory {

    public ResponseRecordModel getRecordsFromBackend(final ResponseRecordModel pResponse) {

        final List<RecordsToNet> listRecords;
        RecordsToNet recordModel;
        final String jsonString = pResponse.getJsonFromBackend();

        if (pResponse.getRecordsArray() == null) {
            listRecords = new ArrayList<>();
        } else {
            listRecords = pResponse.getRecordsArray();
        }

        try {
            final JSONObject listOfRecords = new JSONObject(jsonString);
            final JSONArray detailsOneRecord = listOfRecords.getJSONArray("items");
            final String cursor = listOfRecords.getString("nextPageToken");
            final int arrayIndex = detailsOneRecord.length();

            for (int i = 0; i < arrayIndex; ++i) {
                recordModel = new RecordsToNet();
                final JSONObject record = detailsOneRecord.getJSONObject(i);
                recordModel.setCodes(record.getString("codes"));
                final Long dateOfRecord = record.getLong("date");
//                String date = Converters.convertTimeToString(dateOfRecord);
                recordModel.setDate(dateOfRecord);
                recordModel.setNikName(record.getString("nikName"));
                recordModel.setMoves(record.getString("moves"));
                recordModel.setTime(record.getString("time"));

                try {
                    recordModel.setUserUrlPhoto(record.getString("userUrlPhoto"));
                } catch (final Exception pE) {
                    pE.getStackTrace();
                    recordModel.setUserUrlPhoto(null);
                }

                listRecords.add(recordModel);
            }

            pResponse.setRecordsArray(listRecords);
            pResponse.setCursor(cursor);

            return pResponse;
        } catch (final JSONException pE) {
            pE.printStackTrace();
            pResponse.setException(pE);

            return pResponse;
        }
    }
}
