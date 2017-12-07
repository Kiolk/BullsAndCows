package com.example.NotePad.myapplication.backend;


import com.google.appengine.repackaged.com.google.gson.annotations.SerializedName;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

import java.util.ArrayList;

@Entity
public class UserDataBase {

    @Id
    @SerializedName("name")
    private String mUserName;

    private String mPassword;

    private String mCountry;

    private String mEmail;

    private String mPhotoUrl;

    private String mShortDescription;

    private String mSex;

    private int mAge;

    private int mCountryFlag;

    private int mNumberPlayedGames;

    private long mUserRegistrationTime;

    private long mLastUserVisit;

    private ArrayList<BestUserRecords> mBestUserRecords;

    public ArrayList<BestUserRecords> getBestUserRecords() {
        return mBestUserRecords;
    }

    public void setBestUserRecords(ArrayList<BestUserRecords> pBestUserRecords) {
        mBestUserRecords = pBestUserRecords;
    }

    public String getUserName() {
        return mUserName;
    }

    public void setUserName(String pUserName) {
        mUserName = pUserName;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String pPassword) {
        mPassword = pPassword;
    }

    public String getCountry() {
        return mCountry;
    }

    public void setCountry(String pCountry) {
        mCountry = pCountry;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String pEmail) {
        mEmail = pEmail;
    }

    public String getmPhotoUrl() {
        return mPhotoUrl;
    }

    public void setmPhotoUrl(String mPhotoUrl) {
        this.mPhotoUrl = mPhotoUrl;
    }

    public String getmShortDescription() {
        return mShortDescription;
    }

    public void setmShortDescription(String mShortDescription) {
        this.mShortDescription = mShortDescription;
    }

    public String getmSex() {
        return mSex;
    }

    public void setmSex(String mSex) {
        this.mSex = mSex;
    }

    public int getmAge() {
        return mAge;
    }

    public void setmAge(int mAge) {
        this.mAge = mAge;
    }

    public int getmCountryFlag() {
        return mCountryFlag;
    }

    public void setmCountryFlag(int mCountryFlag) {
        this.mCountryFlag = mCountryFlag;
    }

    public int getmNumberPlayedGames() {
        return mNumberPlayedGames;
    }

    public void setmNumberPlayedGames(int mNumberPlayedGames) {
        this.mNumberPlayedGames = mNumberPlayedGames;
    }

    public long getmUserRegistrationTime() {
        return mUserRegistrationTime;
    }

    public void setmUserRegistrationTime(long mUserRegistrationTime) {
        this.mUserRegistrationTime = mUserRegistrationTime;
    }

    public long getmLastUserVisit() {
        return mLastUserVisit;
    }

    public void setmLastUserVisit(long mLastUserVisit) {
        this.mLastUserVisit = mLastUserVisit;
    }
}
