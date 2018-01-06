package com.example.notepad.bullsandcows.data.models;

import com.example.notepad.myapplication.backend.VersionOfApp;

@Deprecated
public class ResponseAppInfoModel {

    private String mTargetOfRequest;
    private VersionOfApp mResponseInfoApp;
    private String mJsonResponse;
    private Exception mException;

    public ResponseAppInfoModel() {
        this.mTargetOfRequest = null;
        this.mResponseInfoApp = null;
        this.mJsonResponse = null;
        this.mException = null;
    }

    public String getmTargetOfRequest() {
        return mTargetOfRequest;
    }

    public void setmTargetOfRequest(String mTargetOfRequest) {
        this.mTargetOfRequest = mTargetOfRequest;
    }

    public VersionOfApp getmResponseInfoApp() {
        return mResponseInfoApp;
    }

    public void setmResponseInfoApp(VersionOfApp mResponseInfoApp) {
        this.mResponseInfoApp = mResponseInfoApp;
    }

    public String getmJsonResponse() {
        return mJsonResponse;
    }

    public void setmJsonResponse(String mJsonResponse) {
        this.mJsonResponse = mJsonResponse;
    }

    public Exception getmException() {
        return mException;
    }

    public void setmException(Exception mException) {
        this.mException = mException;
    }
}
