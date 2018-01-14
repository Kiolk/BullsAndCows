package com.example.notepad.bullsandcows.data.models;
@Deprecated
public class RecordModel {
    
    private String mCod;
    private String mDate;
    private String mNikName;
    private String mMoves;
    private String mTime;

    public RecordModel() {
    }

    public RecordModel(final String mCod, final String mDate, final String mNikName, final String mMoves, final String mTime) {
        this.mCod = mCod;
        this.mDate = mDate;
        this.mNikName = mNikName;
        this.mMoves = mMoves;
        this.mTime = mTime;
    }

    public String getmCod() {
        return mCod;
    }

    public void setmCod(final String mCod) {
        this.mCod = mCod;
    }

    public String getmDate() {
        return mDate;
    }

    public void setmDate(final String mDate) {
        this.mDate = mDate;
    }

    public String getmNikName() {
        return mNikName;
    }

    public void setmNikName(final String mNikName) {
        this.mNikName = mNikName;
    }

    public String getmMoves() {
        return mMoves;
    }

    public void setmMoves(final String mMoves) {
        this.mMoves = mMoves;
    }

    public String getmTime() {
        return mTime;
    }

    public void setmTime(final String mTime) {
        this.mTime = mTime;
    }
}
