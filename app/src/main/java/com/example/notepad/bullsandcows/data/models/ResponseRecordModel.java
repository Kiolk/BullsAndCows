package com.example.notepad.bullsandcows.data.models;

import com.example.notepad.myapplication.backend.recordsToNetApi.model.RecordsToNet;

import java.util.ArrayList;

public class ResponseRecordModel {

    private RequestRecordModel mTarget;
    private String mJsonFromBackend;
    private Exception mException;
    private ArrayList<RecordsToNet> mRecordsArray;
    private String mCursor;

    public ResponseRecordModel() {
        this.mTarget = null;
        this.mJsonFromBackend = null;
        this.mException = null;
        this.mRecordsArray = null;
        this.mCursor = null;
    }

    public RequestRecordModel getmTarget() {
        return mTarget;
    }

    public void setmTarget(RequestRecordModel mTarget) {
        this.mTarget = mTarget;
    }

    public String getmJsonFromBackend() {
        return mJsonFromBackend;
    }

    public void setmJsonFromBackend(String mJsonFromBackend) {
        this.mJsonFromBackend = mJsonFromBackend;
    }

    public Exception getmException() {
        return mException;
    }

    public void setmException(Exception mException) {
        this.mException = mException;
    }

    public ArrayList<RecordsToNet> getmRecordsArray() {
        return mRecordsArray;
    }

    public void setmRecordsArray(ArrayList<RecordsToNet> mRecordsArray) {
        this.mRecordsArray = mRecordsArray;
    }

    public String getmCursor() {
        return mCursor;
    }

    public void setmCursor(String mCursor) {
        this.mCursor = mCursor;
    }
}
