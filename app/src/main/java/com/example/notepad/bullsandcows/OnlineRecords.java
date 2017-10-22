package com.example.notepad.bullsandcows;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.example.notepad.myapplication.backend.recordsToNetApi.RecordsToNetApi;
import com.example.notepad.myapplication.backend.recordsToNetApi.model.RecordsToNet;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

public class OnlineRecords extends AppCompatActivity {

    ArrayList<String> mCods = new ArrayList<>();
    ArrayList<String> mDate= new ArrayList<>();
    ArrayList<String> mNikName= new ArrayList<>();
    ArrayList<String> mMoves= new ArrayList<>();
    ArrayList<String> mTime= new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_records);
        new GetOnlineRecordsAsyncTask().execute("ss");
    }

    class GetOnlineRecordsAsyncTask extends AsyncTask <String, Void, String> {
        private  RecordsToNetApi myApiService = null;
ProgressBar mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String pS) {
            super.onPostExecute(pS);
            mCods.clear();
            mTime.clear();
            mMoves.clear();
            mNikName.clear();
            mDate.clear();
            try{
                JSONObject listOfRecords = new JSONObject(pS);
                JSONArray detailsOneRecord = listOfRecords.getJSONArray("items");
                int arryIndex = detailsOneRecord.length();
                for(int i =  0; i < arryIndex; ++i){
                    JSONObject record = detailsOneRecord.getJSONObject(i);
                    mCods.add(i, record.getString("codes"));
                    Long dateOfRecord = record.getLong("date");
                    String date = convertTimeToString(dateOfRecord);
                    mDate.add(i, date);
                    mNikName.add(i, record.getString("nikName"));
                    mMoves.add(i, record.getString("moves"));
                    mTime.add(i, record.getString("time"));
                }
            }catch (JSONException pE){
                pE.printStackTrace();
            }
            setItemsOfRecors();
            mProgressBar.setVisibility(View.INVISIBLE);
        }

        @Override
        protected String doInBackground(String... pStrings) {
            if(myApiService == null){
                RecordsToNetApi.Builder builder = new RecordsToNetApi.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
                        .setRootUrl("https://onlinerecordbulsandcows.appspot.com/_ah/api/");

                myApiService = builder.build();
            }

            try{
                String cod = myApiService.list().execute().toString();  //(recordsToNet).execute().toString();
                return cod;//(recordsToNet).execute().toString();
            }catch (IOException pE){
                pE.printStackTrace();
            }

            return null;
        }
    }

    public String convertTimeToString (Long pLong){
        Date date = new Date(pLong + (3600000 *3));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC+3"));
        DateFormat dateFormat = simpleDateFormat;
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC+3"));
        String wonTimeString = dateFormat.format(date);
        return wonTimeString;
    }

    public void setItemsOfRecors(){
        ListView onlineRecods = (ListView) findViewById(R.id.list_of_record_online_list_view);
        CustomAdapterForRecords customAdapterForRecords = new CustomAdapterForRecords(mCods, mDate, mNikName, mMoves, mTime, this);
        onlineRecods.setAdapter(customAdapterForRecords);
    }
}
