package com.example.notepad.bullsandcows.data.managers;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
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

public class AppInfoManager implements AppInfoCallbacks{

    VersionOfApp mAppInfo;
    Thread mInfoAppThread;
    Handler mInfoHandler;
    String mRequestTarget;

    public AppInfoManager(String pRequestType) {

        mRequestTarget = pRequestType;
        mInfoAppThread = new Thread(mRunnable);
        initHandler();

    }

    public AppInfoManager(){
        mInfoAppThread = new Thread(mRunnable);
        initHandler();
    }

    @SuppressLint("HandlerLeak")
    private void initHandler() {
        mInfoHandler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                ResponseAppInfoModel responseAppModel = (ResponseAppInfoModel) msg.obj;

                if(responseAppModel.getmJsonResponse() != null){
                    responseAppModel =  new AppInfoJsonFactory().readAppInfoFromJson(responseAppModel);
                    Log.d("MyLogs", "Json:" + responseAppModel.getmJsonResponse()
                            + " Name of app:" + responseAppModel.getmResponseInfoApp().getNameOfApp()
                            + " Version of App: " + responseAppModel.getmResponseInfoApp().getVersionOfApp());
                    getInfoAppCallback(responseAppModel.getmResponseInfoApp());

                }else {

                }
            }
        };
    }

    final Runnable mRunnable = new Runnable() {

        @Override
        public void run() {
            String name = mRequestTarget;
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

                if (respondCod == HttpURLConnection.HTTP_OK)   {
                    responseAppModel.setmJsonResponse(response.toString());
                }
            } catch (IOException pE) {
                pE.printStackTrace();
                responseAppModel.setmException(pE);
            }finally {
                Message msg = new Message();
                msg.obj = responseAppModel;
                mInfoHandler.sendMessage(msg);
            }
        }
    };

    public void getCurrentAppInfo() {
        mInfoAppThread.start();
    }


    public String buildPostDataString(Map<String, String> params) throws UnsupportedEncodingException {
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

    @Override
    public VersionOfApp getInfoAppCallback(VersionOfApp versionOfApp) {
        return versionOfApp;
    }
}
