package com.example.notepad.bullsandcows.Data.Models;

/**
 * Created by yauhen on 22.11.17.
 */

public class RecordModel {
    
    private String mCod;
    private String mDate;
    private String mNikName;
    private String mMoves;
    private String mTime;

    public RecordModel() {
    }

    public RecordModel(String mCod, String mDate, String mNikName, String mMoves, String mTime) {
        this.mCod = mCod;
        this.mDate = mDate;
        this.mNikName = mNikName;
        this.mMoves = mMoves;
        this.mTime = mTime;
    }

    public String getmCod() {
        return mCod;
    }

    public void setmCod(String mCod) {
        this.mCod = mCod;
    }

    public String getmDate() {
        return mDate;
    }

    public void setmDate(String mDate) {
        this.mDate = mDate;
    }

    public String getmNikName() {
        return mNikName;
    }

    public void setmNikName(String mNikName) {
        this.mNikName = mNikName;
    }

    public String getmMoves() {
        return mMoves;
    }

    public void setmMoves(String mMoves) {
        this.mMoves = mMoves;
    }

    public String getmTime() {
        return mTime;
    }

    public void setmTime(String mTime) {
        this.mTime = mTime;
    }
}
