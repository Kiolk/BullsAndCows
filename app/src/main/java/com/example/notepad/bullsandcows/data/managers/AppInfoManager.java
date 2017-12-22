package com.example.notepad.bullsandcows.data.managers;

import android.os.Handler;
import android.util.Log;

import com.example.NotePad.myapplication.backend.VersionOfApp;
import com.example.notepad.bullsandcows.BuildConfig;
import com.example.notepad.bullsandcows.data.factories.AppInfoJsonFactory;
import com.example.notepad.bullsandcows.data.models.ResponseAppInfoModel;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class AppInfoManager {

    private Thread mInfoAppThread;
    private Handler mInfoHandler;
    private AppInfoCallbacks mCallback;

    public AppInfoManager() {
        Runnable mRunnable = new Runnable() {

            @Override
            public void run() {
                String name = "";
                ResponseAppInfoModel responseAppModel = new ResponseAppInfoModel();
                try {
                    URL url = new URL(BuildConfig.BACKEND_APP_VERSION_URL);

                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoInput(true);
                    httpURLConnection.setDoOutput(true);

                    Map<String, String> nameValuePair = new HashMap<>();
                    nameValuePair.put("name", name);
                    String postParams = buildPostDataString(nameValuePair);

                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    bufferedWriter.write(postParams);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    outputStream.close();
                    httpURLConnection.connect();

                    // Read response
                    int respondCod = httpURLConnection.getResponseCode();
                    StringBuilder response = new StringBuilder();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                    String line;

                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    reader.close();

                    if (respondCod == HttpURLConnection.HTTP_OK) {
                        responseAppModel.setmJsonResponse(response.toString());
                    }
                } catch (IOException pE) {
                    pE.printStackTrace();
                    responseAppModel.setmException(pE);
                } finally {
                    final ResponseAppInfoModel response = responseAppModel;
                    final VersionOfApp responseAboutApp = new AppInfoJsonFactory().readAppInfoFromJson(response).getmResponseInfoApp();
                    mInfoHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (response.getmJsonResponse() != null) {
                                mCallback.getInfoAppCallback(responseAboutApp);
                                Log.d(TAG, "response: " + responseAboutApp.toString());
                            }
                        }
                    });
                }
            }
        };
        mInfoAppThread = new Thread(mRunnable);
    }

    public void getCurrentAppInfo(AppInfoCallbacks appInfoCallbacks) {
        mCallback = appInfoCallbacks;
        mInfoHandler = new Handler();
        mInfoAppThread.start();
    }


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
