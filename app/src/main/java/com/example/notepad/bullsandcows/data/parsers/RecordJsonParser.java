package com.example.notepad.bullsandcows.data.parsers;

import com.example.notepad.bullsandcows.data.models.ResponseRecordModel;
import com.example.notepad.myapplication.backend.recordsToNetApi.model.RecordsToNet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RecordJsonParser {

    private static final String ITEMS = "items";
    private static final String NEXT_PAGE_TOKEN = "nextPageToken";
    private static final String CODED_DIGITS = "codes";
    private static final String DATE = "date";
    private static final String NIK_NAME = "nikName";
    private static final String MOVES = "moves";
    private static final String TIME = "time";
    private static final String USER_URL_PHOTO = "userUrlPhoto";

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
            final JSONArray detailsOneRecord = listOfRecords.getJSONArray(ITEMS);
            final String cursor = listOfRecords.getString(NEXT_PAGE_TOKEN);
            final int arrayIndex = detailsOneRecord.length();

            for (int i = 0; i < arrayIndex; ++i) {
                recordModel = new RecordsToNet();
                final JSONObject record = detailsOneRecord.getJSONObject(i);
                recordModel.setCodes(record.getString(CODED_DIGITS));
                final Long dateOfRecord = record.getLong(DATE);
                recordModel.setDate(dateOfRecord);
                recordModel.setNikName(record.getString(NIK_NAME));
                recordModel.setMoves(record.getString(MOVES));
                recordModel.setTime(record.getString(TIME));

                try {
                    recordModel.setUserUrlPhoto(record.getString(USER_URL_PHOTO));
                } catch (final Exception pE) {
                    recordModel.setUserUrlPhoto(null);
                }

                listRecords.add(recordModel);
            }

            pResponse.setRecordsArray(listRecords);
            pResponse.setCursor(cursor);

            return pResponse;
        } catch (final JSONException pE) {
            pResponse.setException(pE);

            return pResponse;
        }
    }
}
