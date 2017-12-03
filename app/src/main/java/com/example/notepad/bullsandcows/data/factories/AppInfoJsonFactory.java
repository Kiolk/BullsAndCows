package com.example.notepad.bullsandcows.data.factories;

import com.example.NotePad.myapplication.backend.VersionOfApp;
import com.example.notepad.bullsandcows.data.models.ResponseAppInfoModel;

import org.json.JSONException;
import org.json.JSONObject;

public class AppInfoJsonFactory {

    public ResponseAppInfoModel readAppInfoFromJson(ResponseAppInfoModel pResponse){

        String json = pResponse.getmJsonResponse();
        VersionOfApp result = new VersionOfApp();

        try {
            JSONObject object = new JSONObject(json);

            result.setVersionOfApp( object.getString("mVersionOfApp"));
            result.setNameOfApp(object.getString("mNameOfApp"));
            result.setDateOfRelease(object.getLong("mDateOfRelease"));
            result.setFeatures(object.getString("mFeatures"));
            result.setPowered(object.getString("mPowered"));
            result.setUrlNewVersionOfApp(object.getString("mUrlNewVersionOfApp"));

            pResponse.setmResponseInfoApp(result);

            return pResponse;
        } catch (JSONException pE) {
            pE.printStackTrace();
            pResponse.setmException(pE);
        }
        return pResponse;
    }

}
