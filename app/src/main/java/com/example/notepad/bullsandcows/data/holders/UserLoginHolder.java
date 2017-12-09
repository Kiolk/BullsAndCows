package com.example.notepad.bullsandcows.data.holders;

import android.graphics.Bitmap;

import com.example.notepad.bullsandcows.data.managers.UserBaseManager;
import com.example.notepad.myapplication.backend.userDataBaseApi.model.UserDataBase;

public class UserLoginHolder {

    private static UserLoginHolder mUser;

    private String mUserName;

    private String mPassword;

    private String mUserImageUrl;

    private Bitmap mUserBitmap;

    private UserDataBase mUserInfo;

    private boolean mKeepOnline;

    private UserLoginHolder (){
        mKeepOnline = false;
    }

    public static UserLoginHolder getInstance() {

        if (mUser == null) {
            mUser = new UserLoginHolder();
        }

        return mUser;
    }

    public String getUserName() {
        return mUserName;
    }

    public void setUserName(String mUserName) {
        this.mUserName = mUserName;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String mPassword) {
        this.mPassword = mPassword;
    }

    public String getUserImageUrl() {
        return mUserImageUrl;
    }

    public void setUserImageUrl(String mUserImageUrl) {
        this.mUserImageUrl = mUserImageUrl;
    }

    public Bitmap getmUserBitmap() {
        return mUserBitmap;
    }

    public void setmUserBitmap(Bitmap mUserBitmap) {
        this.mUserBitmap = mUserBitmap;
    }

    public UserDataBase getUserInfo() {
        return mUserInfo;
    }

    public void setUserInfo(UserDataBase mUserInfo) {
        this.mUserInfo = mUserInfo;
    }

    public void setOffline(){
        if(!mKeepOnline) {
            UserDataBase userInfo = UserLoginHolder.getInstance().getUserInfo();
            userInfo.setMLastUserVisit(System.currentTimeMillis());
            userInfo.setIsOnline(false);
            new UserBaseManager().updateLastUserVisit(userInfo, false);
        }
        mKeepOnline = false;
    }

    public void keepUserOnline(){
        mKeepOnline = true;
    }

    public void setUserOnline() {
        try {
            if (mUserInfo != null) {
                new UserBaseManager().updateLastUserVisit(mUserInfo, true);
            }
        }catch (Exception pE){
            pE.getStackTrace();
        }
    }
}
