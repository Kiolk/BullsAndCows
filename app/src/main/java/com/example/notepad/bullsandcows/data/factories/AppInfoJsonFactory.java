package com.example.notepad.bullsandcows.data.factories;

import com.example.notepad.myapplication.backend.VersionOfApp;
import com.google.gson.Gson;

public class AppInfoJsonFactory {

    public VersionOfApp getAppInfo(String pJson) {
        VersionOfApp result;
        result = new Gson().fromJson(pJson, VersionOfApp.class);

        return result;
    }
}
