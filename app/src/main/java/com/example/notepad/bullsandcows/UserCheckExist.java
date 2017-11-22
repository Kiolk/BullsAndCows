package com.example.notepad.bullsandcows;

import android.os.AsyncTask;

import com.example.notepad.myapplication.backend.userDataBaseApi.UserDataBaseApi;
import com.example.notepad.myapplication.backend.userDataBaseApi.model.UserDataBase;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;

import java.io.IOException;

public class UserCheckExist extends AsyncTask<String, Void, Boolean> {

    UserDataBaseApi myApiService = null;
    Boolean result = false;

    @Override
    protected Boolean doInBackground(String... pStrings) {
        if (myApiService == null) {  // Only do this once
            UserDataBaseApi.Builder builder = new UserDataBaseApi.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
                    .setRootUrl("https://onlinerecordbulsandcows.appspot.com/_ah/api/");

            myApiService = builder.build();
        }

        try {
            UserDataBase userInfo = myApiService.get(pStrings[0]).execute();
            String name = userInfo.getUserName();
            String password = userInfo.getPassword();
            if (pStrings.length > 1) {
                if (pStrings[0].equals(name) && pStrings[1].equals(password)) {
                    return true;
                }
                return false;
            } else if (pStrings.length == 1) {
                if (pStrings[0].equals(name)) {

                    result = true;
                }
            }
            return result;
        } catch (IOException pE) {
            pE.printStackTrace();
        }
        return false;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Boolean pBoolean) {
        super.onPostExecute(pBoolean);
    }
}
