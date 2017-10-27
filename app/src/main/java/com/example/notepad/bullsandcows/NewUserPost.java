package com.example.notepad.bullsandcows;

import android.os.AsyncTask;

import com.example.notepad.myapplication.backend.recordsToNetApi.RecordsToNetApi;
import com.example.notepad.myapplication.backend.recordsToNetApi.model.RecordsToNet;
import com.example.notepad.myapplication.backend.userDataBaseApi.UserDataBaseApi;
import com.example.notepad.myapplication.backend.userDataBaseApi.model.UserDataBase;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;

import java.io.IOException;

public class NewUserPost extends AsyncTask<UserDataBase, Void, String> {

    UserDataBaseApi myApiService = null;

    @Override
    protected String doInBackground(UserDataBase... pUserDataBases) {

        if (myApiService == null) {
            UserDataBaseApi.Builder builder = new UserDataBaseApi.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
                    .setRootUrl("https://onlinerecordbulsandcows.appspot.com/_ah/api/");

            myApiService = builder.build();
        }
        UserDataBase user = pUserDataBases[0];

        try {

            String cod = myApiService.insert(user).execute().toString();
//            if (check == user) {
//                cod = "t";//.toString();
//            }
            return cod;//(recordsToNet).execute().toString();
        } catch (IOException pE) {
            pE.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(String pS) {
        super.onPostExecute(pS);
    }
}
