package com.example.notepad.myapplication.backend;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity
public class RecordsToNet {

    @Id
    Long mDate;
    @Index
    String mNikName;
    @Index
    String mCodes;

    String mMoves;

    String mTime;

    String mUserUrlPhoto;

//    public RecordsToNet(Long pDate, String pNikName, String pCodes, String pMoves, String pTime) {
//        mDate = pDate;
//        mNikName = pNikName;
//        mCodes = pCodes;
//        mMoves = pMoves;
//        mTime = pTime;
//    }

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

    public String getUserUrlPhoto() {
        return mUserUrlPhoto;
    }

    public void setUserUrlPhoto(String pUserUrlPhoto) {
        mUserUrlPhoto = pUserUrlPhoto;
    }
}
