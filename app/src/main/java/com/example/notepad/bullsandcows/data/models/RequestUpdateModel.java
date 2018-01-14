package com.example.notepad.bullsandcows.data.models;

import com.example.notepad.myapplication.backend.VersionOfApp;
import com.example.notepad.bullsandcows.services.UploadNewVersionAppCallback;

public class RequestUpdateModel {

    private UploadNewVersionAppCallback mCallback;

    private VersionOfApp mVersionApp;

    private Exception mException;

    public UploadNewVersionAppCallback getCallback() {
        return mCallback;
    }

    public void setCallback(final UploadNewVersionAppCallback mCallback) {
        this.mCallback = mCallback;
    }

    public VersionOfApp getVersionApp() {
        return mVersionApp;
    }

    public void setVersionApp(final VersionOfApp mVersionApp) {
        this.mVersionApp = mVersionApp;
    }

    public Exception getException() {
        return mException;
    }

    public void setException(final Exception mException) {
        this.mException = mException;
    }
}
