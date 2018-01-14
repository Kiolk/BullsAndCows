package com.example.notepad.bullsandcows.data.holders;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.notepad.bullsandcows.data.databases.DBOperations;
import com.example.notepad.bullsandcows.data.databases.models.CurrentUserDB;
import com.example.notepad.bullsandcows.data.managers.OnResultCallback;
import com.example.notepad.bullsandcows.data.managers.UserBaseManager;
import com.example.notepad.myapplication.backend.userDataBaseApi.model.UserDataBase;

import static com.example.notepad.bullsandcows.utils.Constants.INT_TRUE_VALUE;
import static com.example.notepad.bullsandcows.utils.Constants.TAG;

//TODO reduce count of singletone google low coupling and higher cohesion
public final class UserLoginHolder {

    private static final int SINGLE_ROW_ABOUT_CURRENT_USER = 1;

    public interface checkTokenCallback {

        void validToken();

        void unValidToken();
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

    public void setUserName(final String mUserName) {
        this.mUserName = mUserName;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(final String mPassword) {
        this.mPassword = mPassword;
    }

    public String getUserImageUrl() {
        return mUserImageUrl;
    }

    public void setUserImageUrl(final String mUserImageUrl) {
        this.mUserImageUrl = mUserImageUrl;
    }

    public Bitmap getUserBitmap() {
        return mUserBitmap;
    }

    public void setUserBitmap(final Bitmap mUserBitmap) {
        this.mUserBitmap = mUserBitmap;
    }

    public UserDataBase getUserInfo() {
        return mUserInfo;
    }

    public void setUserInfo(final UserDataBase mUserInfo) {
        this.mUserInfo = mUserInfo;
    }

    public String getToken() {
        return mToken;
    }

    public void setToken(final String mToken) {
        this.mToken = mToken;
    }

//    @Deprecated
//    public void setOffline() {
//        if (!mKeepOnline && isLogged()) {
//            final UserDataBase userInfo = mUserInfo;
//            userInfo.setMLastUserVisit(System.currentTimeMillis());
//            userInfo.setIsOnline(false);
//            new UserBaseManager().updateLastUserVisit(userInfo, false);
//        }
//        mKeepOnline = false;
//    }

    public void setUserOffline() {

        if (mUserInfo != null && isLogged()) {
            final UserDataBase userInfo = mUserInfo;
            userInfo.setMLastUserVisit(System.currentTimeMillis());
            new UserBaseManager().updateLastUserVisit(userInfo, false);
            Log.d(TAG, "setUserOffline: ");
        }
    }

    public void keepUserOnline() {
        mKeepOnline = true;
    }

    public void setUserOnline() {
        try {
            if (mUserInfo != null && isLogged()) {
                new UserBaseManager().updateLastUserVisit(mUserInfo, true);
                Log.d(TAG, "setUserOnline: ");
            }
        } catch (final Exception pE) {
            pE.getStackTrace();
        }
    }

    public boolean isLogged() {
        return isLogged;
    }

    public void setLogged(final boolean logged) {
        isLogged = logged;
    }

    public void initHolder(final UserDataBase pUserInfo) {
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
                final ContentValues cv = new ContentValues();
                cv.put(CurrentUserDB.ID, SINGLE_ROW_ABOUT_CURRENT_USER);
                cv.put(CurrentUserDB.USER_NAME, pName);
                cv.put(CurrentUserDB.TOKEN, pToken);
                cv.put(CurrentUserDB.IS_KIPPING_LOGIN, pKeepLogin);
                cv.put(CurrentUserDB.LAST_USER_VISIT, System.currentTimeMillis());

                final int res = DBOperations.getInstance().update(CurrentUserDB.TABLE, cv);

                if (res <= 0) {
                    DBOperations.getInstance().insert(CurrentUserDB.TABLE, cv);
                }
                Log.d(TAG, "keepUserData: end");
            }
        }).start();
    }

    public void getSavedUserData(final Context pContext, final UserLoginHolder.checkTokenCallback pCallback) {

        final Handler cursorHandler = new Handler();
        final Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                final Cursor cursor = DBOperations.getInstance().query(CurrentUserDB.TABLE, null, null, null, null, null, null);
                cursorHandler.post(new Runnable() {

                    @Override
                    public void run() {
                        if (cursor != null && cursor.moveToFirst() && cursor.getInt(cursor.getColumnIndex(CurrentUserDB.IS_KIPPING_LOGIN)) == INT_TRUE_VALUE) {
                            final String savedToken = cursor.getString(cursor.getColumnIndex(CurrentUserDB.TOKEN));

                            final String userName = cursor.getString(cursor.getColumnIndex(CurrentUserDB.USER_NAME));
                            final String userToken = cursor.getString(cursor.getColumnIndex(CurrentUserDB.TOKEN));

                            keepUserData(userName, userToken, INT_TRUE_VALUE);
                            cursor.close();

                            final UserBaseManager userBaseManager = new UserBaseManager();
                            userBaseManager.getUserInfo(pContext, userName, new OnResultCallback<UserDataBase>() {

                                @Override
                                public void onSuccess(final UserDataBase pResult) {
                                    if (pResult != null && pResult.getMSex().equals(savedToken)) {
                                        initHolder(pResult);
                                        pCallback.validToken();
                                    } else {
                                        pCallback.unValidToken();
                                    }
                                }

                                @Override
                                public void onError(final Exception pException) {
                                    pCallback.unValidToken();
                                }
                            });

//                            new UserLoginCallback() {
//
//                                @Override
//                                public void getUserInfoCallback(final UserDataBase pUserInfo) {
//
//                                    if (pUserInfo != null && pUserInfo.getMSex().equals(savedToken)) {
//                                        initHolder(pUserInfo);
//                                        pCallback.validToken();
//                                    } else {
//                                        pCallback.unValidToken();
//                                    }
//                                }
//                            });

                        } else {
                            pCallback.unValidToken();
                        }
                    }
                });
            }
        });
        thread.start();
    }

    public void getLastUserVisit(final UserLoginHolder.LastVisitCallback pCallback) {
        final Handler handler = new Handler();
        final Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                final Cursor cursor = DBOperations.getInstance().query(CurrentUserDB.TABLE, null, null, null, null, null, null);
                @Nullable final Long lastVisit;

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
