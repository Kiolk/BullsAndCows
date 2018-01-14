package com.example.notepad.bullsandcows.data.parsers;

import com.example.notepad.myapplication.backend.VersionOfApp;
import com.google.gson.Gson;

public class AppInfoJsonParser {

    public VersionOfApp getAppInfo(final String pJson) {
        final VersionOfApp result;
        result = new Gson().fromJson(pJson, VersionOfApp.class);

        return result;
    }
}
