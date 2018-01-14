package com.example.notepad.bullsandcows.data.parsers;

import com.example.notepad.myapplication.backend.userDataBaseApi.model.BestUserRecords;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public final class JsonParser {

    private static final String CODES_KEY = "codes";
    private static final String DATE = "date";
    private static final String NUMBER_OF_GAME_KEY = "mNumberGames";
    private static final String TIME_KEY = "time";
    private static final String MOVES_KEY = "moves";
    private static final String NIK_NAME_KEY = "nikName";

    public static BestUserRecords[] getBestUserRecordsFromJson(final String pJson) {
        BestUserRecords[] records = null;
        BestUserRecords record;

        try {
            final JSONArray listOfRecords = new JSONArray(pJson);
            records = new BestUserRecords[listOfRecords.length()];
            final int arrayIndex = listOfRecords.length();

            for (int i = 0; i < arrayIndex; ++i) {
                final JSONObject obj = listOfRecords.getJSONObject(i);
                record = new BestUserRecords();

                record.setCodes(obj.getString(CODES_KEY));
                record.setDate(obj.getLong(DATE));
                record.setMNumberGames(obj.getInt(NUMBER_OF_GAME_KEY));
                record.setTime(obj.getString(TIME_KEY));
                record.setMoves(obj.getString(MOVES_KEY));
                record.setNikName(obj.getString(NIK_NAME_KEY));

                records[i] = record;
            }
        } catch (final JSONException pE) {

            return records;
        }

        return records;
    }
}
