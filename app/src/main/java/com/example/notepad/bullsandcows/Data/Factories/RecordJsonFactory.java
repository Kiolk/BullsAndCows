package com.example.notepad.bullsandcows.Data.Factories;

import com.example.notepad.bullsandcows.Data.Models.RecordModel;
import com.example.notepad.bullsandcows.Utils.Converters;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by yauhen on 22.11.17.
 */

public class RecordJsonFactory {

    public static ArrayList<RecordModel> getArrayRecordsFromJson (String jsonString){
        ArrayList<RecordModel> listRecords = new ArrayList<RecordModel>();
        RecordModel recordModel;

        try {
            JSONObject listOfRecords = new JSONObject(jsonString);
            JSONArray detailsOneRecord = listOfRecords.getJSONArray("items");
            int arryIndex = detailsOneRecord.length();
            for (int i = 0; i < arryIndex; ++i) {
                recordModel = new RecordModel();
                JSONObject record = detailsOneRecord.getJSONObject(i);
                recordModel.setmCod(record.getString("codes"));
                Long dateOfRecord = record.getLong("date");
                String date = Converters.convertTimeToString(dateOfRecord);
                recordModel.setmDate(date);
                recordModel.setmNikName(record.getString("nikName"));
                recordModel.setmMoves(record.getString("moves"));
                recordModel.setmTime(record.getString("time"));
                listRecords.add(i, recordModel);
            }
            return listRecords;
        } catch (JSONException pE) {
            pE.printStackTrace();
        }
        return null;
    }

}
