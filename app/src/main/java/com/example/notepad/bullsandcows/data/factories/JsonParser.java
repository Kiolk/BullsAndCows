package com.example.notepad.bullsandcows.data.factories;



import com.example.notepad.myapplication.backend.userDataBaseApi.model.BestUserRecords;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonParser {

    public static BestUserRecords[] getBestUserRecordsFromJson(String pJson){
        BestUserRecords[] records = null;
        BestUserRecords record;

        try {
            JSONArray listOfRecords = new JSONArray(pJson);
            records = new BestUserRecords[listOfRecords.length()];
            int arrayIndex = listOfRecords.length();
            for (int i = 0; i < arrayIndex; ++i) {
                JSONObject obj = listOfRecords.getJSONObject(i);
                record = new BestUserRecords();
                record.setCodes(obj.getString("codes"));
                record.setDate(obj.getLong("date"));
                record.setMNumberGames(obj.getInt("mNumberGames"));
                record.setTime(obj.getString("time"));
                record.setMoves(obj.getString("moves"));
                record.setNikName(obj.getString("nikName"));
                records[i] = record;
            }
        }catch (JSONException pE){

            return records;
        }

        return records;
    }
}
