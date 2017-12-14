package com.example.notepad.bullsandcows.data.managers;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.util.Log;


import com.example.notepad.bullsandcows.BuildConfig;
import com.example.notepad.myapplication.backend.userDataBaseApi.UserDataBaseApi;
import com.example.notepad.myapplication.backend.userDataBaseApi.model.BestUserRecords;
import com.example.notepad.myapplication.backend.userDataBaseApi.model.UserDataBase;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class UserBaseManager implements UserInfoCallback {

    private static final String FREE_USER_NAME_ON_BACKEND = "Free user name";
    private static final String USER_BACKEND_URL = BuildConfig.BACKEND_USER_INFO;
    private static final String SPLITTER_FOR_TIMER = ":";
    private static final int MINUTE_TIME = 0;
    private static final int SECOND_TIME = 1;
    private static final int ADD_ONE_NEW_GAME = 1;
    private static final int MAX_USER_LAST_RECORD = 5;
    private static final int MAX_BEST_RECORDS_NOTES = 10;

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

                if (mUserModelFromBackend.getUserName().equals(FREE_USER_NAME_ON_BACKEND)) {
                    nikCorrectPasswordWrongCallback();
                    nikFreeCallback();
                } else if (mUserModelFromBackend.getUserName().equals(mUserModel.getUserName()) &&
                        mUserModelFromBackend.getPassword().equals(mUserModel.getPassword())) {
                    nikPasswordCorrectCallback(mUserModelFromBackend);
                    updateLastUserVisit(mUserModelFromBackend, true);
                } else {
                    nikCorrectPasswordWrongCallback();
                    getFullUserInfoCallback(mUserModelFromBackend);
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

    public void checkNewBestRecord(final BestUserRecords pRecord) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                if (myApiService == null) {
                    UserDataBaseApi.Builder builder = new UserDataBaseApi.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
                            .setRootUrl(USER_BACKEND_URL);
                    myApiService = builder.build();
                }

                try {
                    UserDataBase userInfo = myApiService.get(pRecord.getNikName()).execute();
                    List<BestUserRecords> listRecords = userInfo.getBestUserRecords();


                    if (listRecords != null) {
                        listRecords = insertPossibleBestRecord(listRecords, pRecord);
                        listRecords = sortListRecords(listRecords);
                    } else {
                        listRecords = new ArrayList<>();
                        listRecords.add(pRecord);
                    }

                    userInfo.setBestUserRecords(listRecords);
                    userInfo.setLastFiveUserRecords(updateLastRecords(userInfo.getLastFiveUserRecords(), pRecord));
                    userInfo.setMNumberPlayedGames(userInfo.getMNumberPlayedGames() + ADD_ONE_NEW_GAME);
                    myApiService.update(userInfo.getUserName(), userInfo).execute();
                } catch (IOException pE) {
                    pE.getStackTrace();
                }
            }
        });
        thread.start();
    }

    private List<BestUserRecords> updateLastRecords(List<BestUserRecords> lastFiveUserRecords, BestUserRecords pRecord) {
        List<BestUserRecords> lastFive = lastFiveUserRecords;

        if (lastFive == null) {
            lastFive = new ArrayList<>();
        }
            lastFive.add(pRecord);




        while (lastFive.size() > MAX_USER_LAST_RECORD) {
            lastFive.remove(0);
        }

        Comparator<BestUserRecords> comparator = new Comparator<BestUserRecords>() {
            @Override
            public int compare(BestUserRecords o1, BestUserRecords o2) {
                return o2.getDate().compareTo(o1.getDate());
            }
        };
        //TODO implement revers sort
        Collections.sort(lastFive, comparator);

        return lastFive;
    }


    private List<BestUserRecords> insertPossibleBestRecord(List<BestUserRecords> listRecords, BestUserRecords pRecord) {
        boolean isCodPresent = false;

        for (int i = 0; i < listRecords.size(); ++i) {
            if (listRecords.get(i).getCodes().equals(pRecord.getCodes())) {
                isCodPresent = true;
                int playedGames = listRecords.get(i).getMNumberGames() + ADD_ONE_NEW_GAME;

                if (listRecords.get(i).getMoves().equals(pRecord.getMoves())) {
                    String[] time1 = listRecords.get(i).getTime().split(SPLITTER_FOR_TIMER);
                    String[] time2 = pRecord.getTime().split(SPLITTER_FOR_TIMER);
                    if (Integer.parseInt(time2[MINUTE_TIME]) < Integer.parseInt(time1[MINUTE_TIME])) {
                        listRecords.remove(i);
                        listRecords.add(pRecord);
                    } else if (Integer.parseInt(time2[MINUTE_TIME]) == Integer.parseInt(time1[MINUTE_TIME])
                            && Integer.parseInt(time2[SECOND_TIME]) < Integer.parseInt(time1[SECOND_TIME])) {
                        listRecords.remove(i);
                        listRecords.add(pRecord);
                    }
                } else if (Integer.parseInt(listRecords.get(i).getMoves()) > Integer.parseInt(pRecord.getMoves())) {
                    listRecords.remove(i);
                    listRecords.add(pRecord);
                }

                listRecords = sortListRecords(listRecords);
                listRecords.get(i).setMNumberGames(playedGames);
                break;
            }
        }

        if (!isCodPresent && listRecords.size() <= MAX_BEST_RECORDS_NOTES) {
            pRecord.setMNumberGames(ADD_ONE_NEW_GAME);
            listRecords.add(pRecord);
        }

        return listRecords;
    }

    private List<BestUserRecords> sortListRecords(List<BestUserRecords> listRecords) {

        Comparator<BestUserRecords> listComparator = new Comparator<BestUserRecords>() {
            @Override
            public int compare(BestUserRecords pRecord1, BestUserRecords pRecord2) {
                return pRecord1.getCodes().compareTo(pRecord2.getCodes());
            }
        };
        Collections.sort(listRecords, listComparator);

        return listRecords;
    }

    public void updateLastUserVisit(final UserDataBase pUserInfo, final boolean pIsOnline) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                if (myApiService == null) {
                    UserDataBaseApi.Builder builder = new UserDataBaseApi.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
                            .setRootUrl(USER_BACKEND_URL);
                    myApiService = builder.build();
                }

                try {

                    String userName = pUserInfo.getUserName();
                    UserDataBase userInfo = myApiService.get(userName).execute();
                    userInfo.setMLastUserVisit(System.currentTimeMillis());
                    userInfo.setIsOnline(pIsOnline);

                    userInfo = myApiService.patch(userName, userInfo).execute();
                    userInfo.clear();

                } catch (IOException pE) {
                    pE.getStackTrace();
                }
            }
        });
        thread.start();
    }

    public void patchNewUserInformation(final UserDataBase pUserNewInfo){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    UserDataBaseApi.Builder builder = new UserDataBaseApi.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
                            .setRootUrl(USER_BACKEND_URL);
                    myApiService = builder.build();

                    myApiService.patch(pUserNewInfo.getUserName(), pUserNewInfo).execute();
                    patchNewUserInfoCallback(true);
                } catch (IOException e) {
                    e.printStackTrace();
                    patchNewUserInfoCallback(false);
                }
            }
        });
        thread.start();
    }

    @Override
    public void nikFreeCallback() {
    }

    @Override
    public UserDataBase nikPasswordCorrectCallback(UserDataBase pUserInfo) {
        return pUserInfo;
    }

    @Override
    public void nikCorrectPasswordWrongCallback() {
    }

    @Override
    public UserDataBase getFullUserInfoCallback(UserDataBase pUserData) {
        return pUserData;
    }

    @Override
    public boolean patchNewUserInfoCallback(Boolean isSuccessFull) {
        return isSuccessFull;
    }
}
