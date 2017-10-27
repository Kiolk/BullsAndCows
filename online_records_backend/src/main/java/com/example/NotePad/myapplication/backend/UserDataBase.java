package com.example.NotePad.myapplication.backend;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

@Entity
public class UserDataBase {

    @Id
    private String mUserName;

    private String mPassword;

    private String mCountry;

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

    private String mEmail;
}
