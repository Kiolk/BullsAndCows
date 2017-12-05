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
}
