package com.example.notepad.bullsandcows.Data.Managers;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;

import com.example.notepad.bullsandcows.UserCheckExist;
import com.example.notepad.myapplication.backend.userDataBaseApi.UserDataBaseApi;
import com.example.notepad.myapplication.backend.userDataBaseApi.model.UserDataBase;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;

import java.io.IOException;

public class UserBaseManager {

    private UserDataBase mUserModel;
    private UserDataBase mUserModelFromBackend;
    private Handler mUserInfoHandler;
    private Thread mUserInfoThread;


    public UserBaseManager() {
        initializationUserInfoHandler();
        mUserInfoThread = new Thread(runnableThread);
        mUserModel = new UserDataBase();
    }

    @SuppressLint("HandlerLeak")
    private void initializationUserInfoHandler() {
        mUserInfoHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                mUserModelFromBackend = (UserDataBase) msg.obj;
            }
        };
    }

    public boolean isUserExist(String pUserName) {
        mUserModel.setUserName(pUserName);
        mUserInfoThread.start();

        return (mUserModelFromBackend.getUserName().equals(pUserName));
    }

    public boolean isNikPasswordCorrect(String pNikName, String pPassword) {
        mUserModel.setUserName(pNikName);
        mUserModel.setPassword(pPassword);
        mUserInfoThread.start();

        return (mUserModelFromBackend.getUserName().equals(pNikName)
                && mUserModelFromBackend.getPassword().equals(pPassword));
    }

    Runnable runnableThread = new Runnable() {
        @Override
        public void run() {
            UserDataBaseApi myApiService = null;

            if (myApiService == null) {
                UserDataBaseApi.Builder builder = new UserDataBaseApi.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
                        .setRootUrl("https://onlinerecordbulsandcows.appspot.com/_ah/api/");

                myApiService = builder.build();
            }

            try {
                UserDataBase userInfo = myApiService.get(mUserModel.getUserName()).execute();
                Message msg = new Message();
                msg.obj = userInfo;
                mUserInfoHandler.sendMessage(msg);
            } catch (IOException pE) {
                pE.printStackTrace();
            }
        }
    };

}
