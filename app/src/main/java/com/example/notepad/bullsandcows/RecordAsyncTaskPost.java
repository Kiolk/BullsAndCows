package com.example.notepad.bullsandcows;

import android.os.AsyncTask;

import com.example.notepad.myapplication.backend.recordsToNetApi.RecordsToNetApi;
import com.example.notepad.myapplication.backend.recordsToNetApi.model.RecordsToNet;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;

import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;

import java.io.IOException;

public class RecordAsyncTaskPost extends AsyncTask<RecordsToNet, Void, String> {

 private static RecordsToNetApi myApiService = null;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(String pS) {
        super.onPostExecute(pS);
    }

    @Override
    protected String doInBackground(RecordsToNet... pRecordsToNets) {
        if(myApiService == null){
            RecordsToNetApi.Builder builder = new RecordsToNetApi.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
                    .setRootUrl("https://onlinerecordbulsandcows.appspot.com/_ah/api/");

            myApiService = builder.build();
        }
        RecordsToNet recordsToNet = pRecordsToNets[0];


        try{
//            inline variable
            String cod = myApiService.insert(recordsToNet).execute().toString();
            return cod;//(recordsToNet).execute().toString();
        }catch (IOException pE){
            pE.printStackTrace();
        }

        return null;
    }
}
