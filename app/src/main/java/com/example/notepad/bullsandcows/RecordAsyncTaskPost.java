package com.example.notepad.bullsandcows;

import android.os.AsyncTask;

import com.example.notepad.bullsandcows.ui.activity.listeners.PostRecordSuccessListener;
import com.example.notepad.myapplication.backend.recordsToNetApi.RecordsToNetApi;
import com.example.notepad.myapplication.backend.recordsToNetApi.model.RecordsToNet;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;

import java.io.IOException;

public class RecordAsyncTaskPost extends AsyncTask<RecordsToNet, Void, RecordsToNet> {

    private static final String USER_RECORD_BACKEND = BuildConfig.BACKEND_USER_INFO;
    private static RecordsToNetApi myApiService = null;

    private PostRecordSuccessListener mSuccessListener;

    public RecordAsyncTaskPost(){
        mSuccessListener = null;
    }

    public RecordAsyncTaskPost(PostRecordSuccessListener pListener){
       mSuccessListener = pListener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(RecordsToNet pRecord) {
        if(mSuccessListener != null){
            if(pRecord != null){
                mSuccessListener.setResult(pRecord);
            }else {
                mSuccessListener.setResult(null);
            }
        }

        super.onPostExecute(pRecord);
    }

    @Override
    protected RecordsToNet doInBackground(RecordsToNet... pRecordsToNets) {
        if(myApiService == null){
            RecordsToNetApi.Builder builder = new RecordsToNetApi.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
                    .setRootUrl(USER_RECORD_BACKEND);

            myApiService = builder.build();
        }
        RecordsToNet recordsToNet = pRecordsToNets[0];


        try{
            String cod = myApiService.insert(recordsToNet).execute().toString();
            return recordsToNet;//(recordsToNet).execute().toString();
        }catch (IOException pE){
            pE.printStackTrace();
        }

        return null;
    }
}
