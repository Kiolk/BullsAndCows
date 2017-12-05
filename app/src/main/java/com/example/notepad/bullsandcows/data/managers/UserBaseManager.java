package com.example.notepad.bullsandcows.data.managers;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.notepad.myapplication.backend.userDataBaseApi.UserDataBaseApi;
import com.example.notepad.myapplication.backend.userDataBaseApi.model.UserDataBase;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;

import java.io.IOException;

public class UserBaseManager implements UserInfoCallback {

    private static final String FREE_USER_NAME_ON_BACKEND = "Free user name";
    private static final String USER_BACKEND_URL = "https://myjson-182914.appspot.com/_ah/api/";

    private UserDataBase mUserModel;
    private UserDataBase mUserModelFromBackend;
    private Handler mUserInfoHandler;
    private Thread mUserInfoThread;
    private Runnable mRunnableThread;
    private UserDataBaseApi myApiService;

    public UserBaseManager() {
        initializationUserInfoHandler();
        initRunnable();

        mUserInfoThread = new Thread(mRunnableThread);
        mUserModel = new UserDataBase();
        mUserModelFromBackend = null;
    }

    @SuppressLint("HandlerLeak")
    private void initializationUserInfoHandler() {
        mUserInfoHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                mUserModelFromBackend = (UserDataBase) msg.obj;
                Log.d("MyLogs", "get message");

                getFullUserInfoCallback(mUserModelFromBackend);

                if (mUserModelFromBackend.getUserName().equals(FREE_USER_NAME_ON_BACKEND)) {
                    nikCorrectPasswordWrongCallback();
                    nikFreeCallback();
                } else if (mUserModelFromBackend.getUserName().equals(mUserModel.getUserName()) &&
                        mUserModelFromBackend.getPassword().equals(mUserModel.getPassword())) {
                    nikPasswordCorrectCallback();
                } else {
                    nikCorrectPasswordWrongCallback();
                }
            }
        };
    }

    public void checkInfoAboutUser(String pNikName, String pPassword) {
        mUserModel.setUserName(pNikName);
        mUserModel.setPassword(pPassword);
        mUserInfoThread.start();
    }

    private void initRunnable() {
        mRunnableThread = new Runnable() {
            @Override
            public void run() {

                if (myApiService == null) {
                    UserDataBaseApi.Builder builder = new UserDataBaseApi.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
                            .setRootUrl(USER_BACKEND_URL);

                    myApiService = builder.build();
                }

                try {
                    UserDataBase userInfo = myApiService.get(mUserModel.getUserName()).execute();
                    Message msg = new Message();

                    msg.obj = userInfo;
                    mUserInfoHandler.sendMessage(msg);
                } catch (IOException pE) {
                    Message msg = new Message();

                    pE.printStackTrace();
                    msg.obj = new UserDataBase().setUserName(FREE_USER_NAME_ON_BACKEND);
                    mUserInfoHandler.sendMessage(msg);
                }
            }
        };
    }

    public void createNewUser(final UserDataBase pNewUser) {

        Thread addUserThread = new Thread(new Runnable() {
            @Override
            public void run() {

                if (myApiService == null) {
                    UserDataBaseApi.Builder builder = new UserDataBaseApi.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
                            .setRootUrl(USER_BACKEND_URL);

                    myApiService = builder.build();
                }
                try {
                    myApiService.insert(pNewUser).execute();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        addUserThread.start();
    }

    @Override
    public void nikFreeCallback() {

    }

    @Override
    public void nikPasswordCorrectCallback() {

    }

    @Override
    public void nikCorrectPasswordWrongCallback() {

    }

    @Override
    public UserDataBase getFullUserInfoCallback(UserDataBase pUserData) {
        return pUserData;
    }
}
