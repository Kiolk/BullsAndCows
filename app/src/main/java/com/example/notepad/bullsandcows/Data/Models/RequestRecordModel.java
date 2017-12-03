package com.example.notepad.bullsandcows.Data.Models;

public class RequestRecordModel {

    private String mCursor;

    public RequestRecordModel() {
    }

    public RequestRecordModel(String pRequest){
        mCursor = pRequest;
    }

    public String getCursor() {
        return mCursor;
    }

    public void setCursor(String mCursor) {
        this.mCursor = mCursor;
    }
}
