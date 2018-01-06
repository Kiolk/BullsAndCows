package com.example.notepad.bullsandcows.data.models;

import com.example.notepad.myapplication.backend.VersionOfApp;
import com.example.notepad.bullsandcows.services.UploadNewVersionAppCallback;

public class RequestUpdateModel {

    //TODO move callback to another place
    private UploadNewVersionAppCallback mCallback;

    private VersionOfApp mVersionApp;

    private Exception mException;

    //TODO move method to another place
    public UploadNewVersionAppCallback getCallback() {
        return mCallback;
    }

    public void setCallback(UploadNewVersionAppCallback mCallback) {
        this.mCallback = mCallback;
    }

    public VersionOfApp getVersionApp() {
        return mVersionApp;
    }

    public void setVersionApp(VersionOfApp mVersionApp) {
        this.mVersionApp = mVersionApp;
    }

    public Exception getException() {
        return mException;
    }

    public void setException(Exception mException) {
        this.mException = mException;
    }
}
