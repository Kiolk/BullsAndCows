package com.example.notepad.bullsandcows.data.holders;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Handler;
import android.util.Log;

import com.example.notepad.bullsandcows.data.databases.DBOperationsSingleTone;
import com.example.notepad.bullsandcows.data.databases.models.CurrentUserDB;
import com.example.notepad.bullsandcows.data.managers.UserBaseManager;
import com.example.notepad.bullsandcows.data.managers.UserLoginCallback;
import com.example.notepad.myapplication.backend.userDataBaseApi.model.UserDataBase;

import static com.example.notepad.bullsandcows.utils.Constants.INT_TRUE_VALUE;
import static com.example.notepad.bullsandcows.utils.Constants.TAG;

public class UserLoginHolder {

    public interface checkTokenCallback {
        void isValidToken(boolean isValid);
    }

    public interface LastVisitCallback {
        void getLastVisit(Long pLastVisit);
    }

    private static UserLoginHolder mUser;

    private String mUserName;

    private String mPassword;

    private String mUserImageUrl;

    private Bitmap mUserBitmap;

    private UserDataBase mUserInfo;

    private boolean mKeepOnline;

    private boolean isLogged;

    private String mToken;

    private UserLoginHolder() {
        mKeepOnline = false;
        isLogged = false;
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

    public String getToken() {
        return mToken;
    }

    public void setToken(String mToken) {
        this.mToken = mToken;
    }

    public void setOffline() {
        if (!mKeepOnline && isLogged()) {
            UserDataBase userInfo = UserLoginHolder.getInstance().getUserInfo();
            userInfo.setMLastUserVisit(System.currentTimeMillis());
            userInfo.setIsOnline(false);
            new UserBaseManager().updateLastUserVisit(userInfo, false);
        }
        mKeepOnline = false;
    }

    public void keepUserOnline() {
        mKeepOnline = true;
    }

    public void setUserOnline() {
        try {
            if (mUserInfo != null && isLogged()) {
                new UserBaseManager().updateLastUserVisit(mUserInfo, true);
            }
        } catch (Exception pE) {
            pE.getStackTrace();
        }
    }

    public boolean isLogged() {
        return isLogged;
    }

    public void setLogged(boolean logged) {
        isLogged = logged;
    }

    public void initHolder(UserDataBase pUserInfo) {
        mUserInfo = pUserInfo;
        mUserName = pUserInfo.getUserName();
        mPassword = pUserInfo.getPassword();
        mUserImageUrl = pUserInfo.getMPhotoUrl();
        isLogged = true;
    }

    public void keepUserData(final String pName, final String pToken, final int pKeepLogin) {
        Log.d(TAG, "keepUserData: start");
        new Thread(new Runnable() {
            @Override
            public void run() {
                ContentValues cv = new ContentValues();
                cv.put(CurrentUserDB.ID, 1);
                cv.put(CurrentUserDB.USER_NAME, pName);
                cv.put(CurrentUserDB.TOKEN, pToken);
                cv.put(CurrentUserDB.IS_KIPPING_LOGIN, pKeepLogin);
                cv.put(CurrentUserDB.LAST_USER_VISIT, System.currentTimeMillis());

                int res = DBOperationsSingleTone.getInstance().update(CurrentUserDB.TABLE, cv);

                if (res <= 0) {
                    DBOperationsSingleTone.getInstance().insert(CurrentUserDB.TABLE, cv);
                }
                Log.d(TAG, "keepUserData: end");
            }
        }).start();
    }

    public void getSavedUserData(final Context pContext, final UserLoginHolder.checkTokenCallback pCallback) {

        final Handler cursorHandler = new Handler();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                final Cursor cursor = DBOperationsSingleTone.getInstance().query(CurrentUserDB.TABLE, null, null, null, null, null, null);
                cursorHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (cursor != null && cursor.moveToFirst() && cursor.getInt(cursor.getColumnIndex(CurrentUserDB.IS_KIPPING_LOGIN)) == INT_TRUE_VALUE) {
                            final String savedToken = cursor.getString(cursor.getColumnIndex(CurrentUserDB.TOKEN));

                            String userName = cursor.getString(cursor.getColumnIndex(CurrentUserDB.USER_NAME));
                            String userToken = cursor.getString(cursor.getColumnIndex(CurrentUserDB.TOKEN));

                            keepUserData(userName, userToken, INT_TRUE_VALUE);
                            cursor.close();

                            UserBaseManager userBaseManager = new UserBaseManager();
                            userBaseManager.getUserInfo(pContext, userName, new UserLoginCallback() {
                                @Override
                                public void getUserInfoCallback(UserDataBase pUserInfo) {
                                    if (pUserInfo != null && pUserInfo.getMSex().equals(savedToken)) {
                                        initHolder(pUserInfo);
                                        pCallback.isValidToken(true);
                                    } else {
                                        pCallback.isValidToken(false);
                                    }
                                }
                            });

                        } else {
                            pCallback.isValidToken(false);
                        }
                    }
                });
            }
        });
        thread.start();
    }

    public void getLastUserVisit(final UserLoginHolder.LastVisitCallback pCallback) {
        final Handler handler = new Handler();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Cursor cursor = DBOperationsSingleTone.getInstance().query(CurrentUserDB.TABLE, null, null, null, null, null, null);
                final Long lastVisit;

                if (cursor != null && cursor.moveToFirst()) {
                    lastVisit = cursor.getLong(cursor.getColumnIndex(CurrentUserDB.LAST_USER_VISIT));
                } else {
                    lastVisit = null;
                }

                if (cursor != null) {
                    cursor.close();
                }

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        pCallback.getLastVisit(lastVisit);
                    }
                });
            }
        });
        thread.start();
    }
}
