package com.example.notepad.bullsandcows.data.holders;

import com.example.notepad.myapplication.backend.VersionOfApp;

public class AppInfoHolder {

    private static AppInfoHolder mInstance;

    private VersionOfApp mVersionApp;

    private AppInfoHolder (){
        mVersionApp = new VersionOfApp();
    }

    public static AppInfoHolder getInstance(){
        if(mInstance == null){
            mInstance = new AppInfoHolder();
        }

        return mInstance;
    }

    public VersionOfApp getVersionApp() {
        return mVersionApp;
    }

    public void setVersionApp(VersionOfApp mVersionApp) {
        this.mVersionApp = mVersionApp;
    }
}
