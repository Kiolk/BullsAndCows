package com.example.notepad.bullsandcows.data.models;

public class UserMoveModel {

    private String mMove;

    private String mInputNumber;

    private String mBulls;

    private String mCows;

    public String getMove() {
        return mMove;
    }

    public void setMove(final String pMove) {
        mMove = pMove;
    }

    public String getInputNumber() {
        return mInputNumber;
    }

    public void setInputNumber(final String pInputNumber) {
        mInputNumber = pInputNumber;
    }

    public String getBulls() {
        return mBulls;
    }

    public void setBulls(final String pBulls) {
        mBulls = pBulls;
    }

    public String getCows() {
        return mCows;
    }

    public void setCows(final String pCows) {
        mCows = pCows;
    }
}
