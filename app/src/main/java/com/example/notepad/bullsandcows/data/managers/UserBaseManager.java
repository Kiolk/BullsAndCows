package com.example.notepad.bullsandcows.data.managers;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;

import com.example.notepad.bullsandcows.data.databases.models.UserInfoDB;
import com.example.notepad.bullsandcows.data.httpclient.BackendEndpointClient;
import com.example.notepad.bullsandcows.data.providers.RecordsContentProvider;
import com.example.notepad.bullsandcows.utils.converters.ModelConverterUtil;
import com.example.notepad.myapplication.backend.userDataBaseApi.model.BestUserRecords;
import com.example.notepad.myapplication.backend.userDataBaseApi.model.UserDataBase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.example.notepad.bullsandcows.utils.Constants.TAG;

public class UserBaseManager {

    private static final String SPLITTER_FOR_TIMER = ":";
    private static final int MINUTE_TIME = 0;
    private static final int SECOND_TIME = 1;
    private static final int ADD_ONE_NEW_GAME = 1;
    private static final int MAX_USER_LAST_RECORD = 5;
    private static final int MAX_BEST_RECORDS_NOTES = 10;

    public void getUserInfo(final Context pContext, final String pUserNik, final OnResultCallback<UserDataBase> pCallback) {
        final Handler handler = new Handler();
        final Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                UserDataBase userInfo = null;
                try {
                    userInfo = BackendEndpointClient.getUserDataBaseApi().get(pUserNik).execute();
                } catch (final IOException e) {
                    e.printStackTrace();
                }

                if (userInfo != null && pContext != null) {
                    final Uri result = pContext.getContentResolver().insert(RecordsContentProvider.CONTENT_USERS_URI, ModelConverterUtil.fromUserDataBaseToCv(userInfo));

                    if (result != null) {
                        if (Integer.parseInt(result.getLastPathSegment()) <= 0) {
                            pContext.getContentResolver().update(RecordsContentProvider.CONTENT_USERS_URI, ModelConverterUtil.fromUserDataBaseToCv(userInfo), UserInfoDB.ID + " = ?", new String[]{pUserNik});
                            Log.d(TAG, "update user data: ");
                        } else {
                            Log.d(TAG, "insert new user data");
                        }
                    }
                }

                final UserDataBase userGettingInfo = userInfo;

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (pCallback != null) {
                            if (userGettingInfo != null) {
                                pCallback.onSuccess(userGettingInfo);
                            } else {
                                pCallback.onError(null);
                            }
                        }
                    }
                });
            }
        });
        thread.start();
    }

    public void createNewUser(final UserDataBase pNewUser) {
        final Thread addUserThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    BackendEndpointClient.getUserDataBaseApi().insert(pNewUser).execute();
                } catch (final IOException e) {
                    e.printStackTrace();
                }
            }
        });
        addUserThread.start();
    }

    public void checkNewBestRecord(final BestUserRecords pRecord) {
        final Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final UserDataBase userInfo = BackendEndpointClient.getUserDataBaseApi().get(pRecord.getNikName()).execute();
                    List<BestUserRecords> listRecords = userInfo.getBestUserRecords();

                    if (listRecords != null) {
                        listRecords = insertPossibleBestRecord(listRecords, pRecord);
                        listRecords = sortListRecords(listRecords);
                    } else {
                        listRecords = new ArrayList<>();
                        listRecords.add(pRecord);
                        listRecords.get(listRecords.size() - 1).setMNumberGames(ADD_ONE_NEW_GAME);
                    }

                    userInfo.setBestUserRecords(listRecords);
                    userInfo.setLastFiveUserRecords(updateLastRecords(userInfo.getLastFiveUserRecords(), pRecord));
                    userInfo.setMNumberPlayedGames(userInfo.getMNumberPlayedGames() + ADD_ONE_NEW_GAME);
                    BackendEndpointClient.getUserDataBaseApi().update(userInfo.getUserName(), userInfo).execute();
                } catch (final IOException pE) {
                    pE.getStackTrace();
                }
            }
        });
        thread.start();
    }

    private List<BestUserRecords> updateLastRecords(final List<BestUserRecords> lastFiveUserRecords, final BestUserRecords pRecord) {
        List<BestUserRecords> lastFive = lastFiveUserRecords;

        if (lastFive == null) {
            lastFive = new ArrayList<>();
        }
        lastFive.add(pRecord);

        while (lastFive.size() > MAX_USER_LAST_RECORD) {
            lastFive.remove(0);
        }

        final Comparator<BestUserRecords> comparator = new Comparator<BestUserRecords>() {
            @Override
            public int compare(final BestUserRecords o1, final BestUserRecords o2) {
                return o2.getDate().compareTo(o1.getDate());
            }
        };
        Collections.sort(lastFive, comparator);

        return lastFive;
    }


    private List<BestUserRecords> insertPossibleBestRecord(List<BestUserRecords> listRecords, final BestUserRecords pRecord) {
        boolean isCodPresent = false;

        for (int i = 0; i < listRecords.size(); ++i) {
            if (listRecords.get(i).getCodes().equals(pRecord.getCodes())) {
                isCodPresent = true;
                final int playedGames = listRecords.get(i).getMNumberGames() + ADD_ONE_NEW_GAME;

                if (listRecords.get(i).getMoves().equals(pRecord.getMoves())) {
                    final String[] time1 = listRecords.get(i).getTime().split(SPLITTER_FOR_TIMER);
                    final String[] time2 = pRecord.getTime().split(SPLITTER_FOR_TIMER);
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
            listRecords.add(pRecord);
            listRecords.get(listRecords.size() - 1).setMNumberGames(ADD_ONE_NEW_GAME);
        }

        return listRecords;
    }

    private List<BestUserRecords> sortListRecords(final List<BestUserRecords> listRecords) {

        final Comparator<BestUserRecords> listComparator = new Comparator<BestUserRecords>() {
            @Override
            public int compare(final BestUserRecords pRecord1, final BestUserRecords pRecord2) {
                return pRecord1.getCodes().compareTo(pRecord2.getCodes());
            }
        };
        Collections.sort(listRecords, listComparator);

        return listRecords;
    }

    public void updateLastUserVisit(final UserDataBase pUserInfo, final boolean pIsOnline) {
        final Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    final String userName = pUserInfo.getUserName();
                    final UserDataBase userInfo = BackendEndpointClient.getUserDataBaseApi().get(userName).execute();
                    userInfo.setMLastUserVisit(System.currentTimeMillis());
                    userInfo.setIsOnline(pIsOnline);

                    BackendEndpointClient.getUserDataBaseApi().patch(userName, userInfo).execute();

                } catch (final IOException pE) {
                    pE.getStackTrace();
                }
            }
        });
        thread.start();
    }

    public void patchNewUserInformation(final UserDataBase pUserNewInfo, final OnResultCallback<UserDataBase> pCallback) {
        final Handler handler = new Handler();
        final Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                final UserDataBase userGettingInfo;
                UserDataBase tmp;
                try {
                    tmp = BackendEndpointClient.getUserDataBaseApi().patch(pUserNewInfo.getUserName(), pUserNewInfo).execute();
                } catch (final IOException e) {
                    e.printStackTrace();
                    tmp = null;
                }
                userGettingInfo = tmp;
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (pCallback != null) {
                            pCallback.onSuccess(userGettingInfo);
                        }
                    }
                });
            }
        });
        thread.start();
    }
}
