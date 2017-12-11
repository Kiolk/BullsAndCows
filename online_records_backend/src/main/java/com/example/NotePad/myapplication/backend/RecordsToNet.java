package com.example.NotePad.myapplication.backend;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity
public class RecordsToNet {

    @Id
    private Long mDate;
    @Index
    private String mNikName;
    @Index
    private String mCodes;

    private String mMoves;

    private String mTime;

    private String mUserPhotoUrl;

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

    public String getmUserPhotoUrl() {
        return mUserPhotoUrl;
    }

    public void setmUserPhotoUrl(String mUserPhotoUrl) {
        this.mUserPhotoUrl = mUserPhotoUrl;
    }
}
