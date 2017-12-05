package com.example.NotePad.myapplication.backend;

public class BestUserRecords {

    private Long mDate;

    private String mNikName;

    private String mCodes;

    private String mMoves;

    private String mTime;

    public BestUserRecords (Long pDate, String pNikName, String pCodes, String pMoves, String pTime) {
        mDate = pDate;
        mNikName = pNikName;
        mCodes = pCodes;
        mMoves = pMoves;
        mTime = pTime;
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
}
