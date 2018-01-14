package com.example.notepad.bullsandcows.data.managers;

import android.os.Handler;
import android.util.Log;

import com.example.notepad.bullsandcows.data.httpclient.HttpClient;
import com.example.notepad.bullsandcows.data.httpclient.models.HttpRequest;
import com.example.notepad.bullsandcows.data.httpclient.models.HttpResponse;
import com.example.notepad.bullsandcows.data.parsers.AppInfoJsonParser;
import com.example.notepad.myapplication.backend.VersionOfApp;

import static android.content.ContentValues.TAG;

public class AppInfoManager {

    public AppInfoManager() {
    }

    public void getCurrentAppInfo(final HttpRequest pRequest, final OnResultCallback<VersionOfApp> appInfoCallbacks) {
        final Handler handler = new Handler();
        final Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                final HttpResponse response = new HttpClient().post(pRequest);

                final VersionOfApp responseAboutApp = new AppInfoJsonParser().getAppInfo(response.getResponse());

                handler.post(new Runnable() {

                    @Override
                    public void run() {
                        if (responseAboutApp != null) {
                            appInfoCallbacks.onSuccess(responseAboutApp);
                            Log.d(TAG, "response: " + responseAboutApp);
                        } else {
                            appInfoCallbacks.onError(null);
                        }
                    }
                });
            }
        });
        thread.start();
    }
}
