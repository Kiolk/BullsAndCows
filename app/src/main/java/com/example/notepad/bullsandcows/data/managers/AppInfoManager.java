package com.example.notepad.bullsandcows.data.managers;

import android.os.Handler;
import android.util.Log;

import com.example.NotePad.myapplication.backend.VersionOfApp;
import com.example.notepad.bullsandcows.data.factories.AppInfoJsonFactory;
import com.example.notepad.bullsandcows.data.httpclient.HttpClient;
import com.example.notepad.bullsandcows.data.httpclient.models.HttpRequest;
import com.example.notepad.bullsandcows.data.httpclient.models.HttpResponse;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class AppInfoManager {

    public AppInfoManager() {
    }

    public void getCurrentAppInfo(final HttpRequest pRequest, final AppInfoCallbacks appInfoCallbacks) {
        final Handler handler = new Handler();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                HttpResponse response = new HttpClient().post(pRequest);

                final VersionOfApp responseAboutApp = new AppInfoJsonFactory().getAppInfo(response.getResponse());

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (responseAboutApp != null) {
                            appInfoCallbacks.getInfoAppCallback(responseAboutApp);
                            Log.d(TAG, "response: " + responseAboutApp.toString());
                        }
                    }
                });
            }
        });
        thread.start();
    }

    @Deprecated
    private String buildPostDataString(Map<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (first) {
                first = false;
            } else {
                result.append("&");
            }
            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }
        return result.toString();
    }
}
