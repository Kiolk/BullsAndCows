package com.example.notepad.myapplication.backend;

public class BestUserRecords {

    private Long mDate;

    private String mNikName;

    private String mCodes;

    private String mMoves;

    private String mTime;

    private int mNumberGames;

    private long mDuringGameTime;

    public long getDuringGameTime() {
        return mDuringGameTime;
    }

    public void setDuringGameTime(long mDuringGameTime) {
        this.mDuringGameTime = mDuringGameTime;
    }

    public Long getDate() {
        return mDate;
    }

    public void setDate(Long pDate) {
        mDate = pDate;
    }

    public String getNikName() {
        return mNikName;
    }

    public void setNikName(String pNikName) {
        mNikName = pNikName;
    }

    public String getCodes() {
        return mCodes;
    }

    public void setCodes(String pCodes) {
        mCodes = pCodes;
    }

    public String getMoves() {
        return mMoves;
    }

    public void setMoves(String pMoves) {
        mMoves = pMoves;
    }

    public String getTime() {
        return mTime;
    }

    public void setTime(String pTime) {
        mTime = pTime;
    }

    public int getmNumberGames() {
        return mNumberGames;
    }

    public void setmNumberGames(int mNumberGames) {
        this.mNumberGames = mNumberGames;
    }
}
