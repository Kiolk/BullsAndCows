package com.example.notepad.bullsandcows.data.models;

import com.example.notepad.myapplication.backend.recordsToNetApi.model.RecordsToNet;

import java.util.List;

public class ResponseRecordModel {

    private RequestRecordModel mTarget;
    private String mJsonFromBackend;
    private Exception mException;
    private List<RecordsToNet> mRecordsArray;
    private String mCursor;

    public ResponseRecordModel() {
        this.mTarget = null;
        this.mJsonFromBackend = null;
        this.mException = null;
        this.mRecordsArray = null;
        this.mCursor = null;
    }

    public RequestRecordModel getTarget() {
        return mTarget;
    }

    public void setTarget(final RequestRecordModel mTarget) {
        this.mTarget = mTarget;
    }

    public String getJsonFromBackend() {
        return mJsonFromBackend;
    }

    public void setJsonFromBackend(final String mJsonFromBackend) {
        this.mJsonFromBackend = mJsonFromBackend;
    }

    public Exception getException() {
        return mException;
    }

    public void setException(final Exception mException) {
        this.mException = mException;
    }

    public List<RecordsToNet> getRecordsArray() {
        return mRecordsArray;
    }

    public void setRecordsArray(final List<RecordsToNet> pRecordsArray) {
        mRecordsArray = pRecordsArray;
    }

    public String getCursor() {
        return mCursor;
    }

    public void setCursor(final String mCursor) {
        this.mCursor = mCursor;
    }
}
