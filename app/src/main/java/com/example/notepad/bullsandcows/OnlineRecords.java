package com.example.notepad.bullsandcows;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.notepad.bullsandcows.services.RefreshOnlineRecordService;
import com.example.notepad.bullsandcows.utils.CheckConnection;
import com.example.notepad.bullsandcows.utils.Constants;
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

    public static final String TEXT_FOR_INTENT = "text for intent";
    ArrayList<String> mCods = new ArrayList<>();
    ArrayList<String> mDate = new ArrayList<>();
    ArrayList<String> mNikName = new ArrayList<>();
    ArrayList<String> mMoves = new ArrayList<>();
    ArrayList<String> mTime = new ArrayList<>();
    Button mRefreshButton;
    MyBroadcastReceiver mReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_records);
        if (new CheckConnection().checkConnection(this)) {
            new GetOnlineRecordsAsyncTask().execute("ss");
        } else {
            Toast.makeText(this, Constants.DISCONNECT_SERVER, Toast.LENGTH_LONG).show();
        }
        mReceiver = new MyBroadcastReceiver();
        IntentFilter mIntentFilter = new IntentFilter(RefreshOnlineRecordService.ACTION_REFRESH_ONLINE_RECORD_SERVICE);
        mIntentFilter.addCategory(Intent.CATEGORY_DEFAULT);
        registerReceiver(mReceiver, mIntentFilter);
        mRefreshButton = (Button) findViewById(R.id.refresh_records_button);
        mRefreshButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View pView) {
                Intent intent = new Intent(OnlineRecords.this, RefreshOnlineRecordService.class);
                intent.putExtra(TEXT_FOR_INTENT, "Refresh");
                startService(intent);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }

    private class GetOnlineRecordsAsyncTask extends AsyncTask<String, Void, String> {

        private RecordsToNetApi myApiService = null;
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
            try {
                JSONObject listOfRecords = new JSONObject(pS);
                JSONArray detailsOneRecord = listOfRecords.getJSONArray("items");
                int arryIndex = detailsOneRecord.length();
                for (int i = 0; i < arryIndex; ++i) {
                    JSONObject record = detailsOneRecord.getJSONObject(i);
                    mCods.add(i, record.getString("codes"));
                    Long dateOfRecord = record.getLong("date");
                    String date = convertTimeToString(dateOfRecord);
                    mDate.add(i, date);
                    mNikName.add(i, record.getString("nikName"));
                    mMoves.add(i, record.getString("moves"));
                    mTime.add(i, record.getString("time"));
                }
            } catch (JSONException pE) {
                pE.printStackTrace();
            }
            setItemsOfRecors();
            mProgressBar.setVisibility(View.INVISIBLE);
        }

        @Override
        protected String doInBackground(String... pStrings) {
            if (myApiService == null) {
                RecordsToNetApi.Builder builder = new RecordsToNetApi.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
                        .setRootUrl("https://onlinerecordbulsandcows.appspot.com/_ah/api/");

                myApiService = builder.build();
            }

            try {
                return myApiService.list().execute().toString();
            } catch (IOException pE) {
                pE.printStackTrace();
            }

            return null;
        }
    }

    public String convertTimeToString(Long pLong) {
        Date date = new Date(pLong + (3600000 * 3));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC+3"));
        DateFormat dateFormat = simpleDateFormat;
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC+3"));
        return dateFormat.format(date);
    }

    public void setItemsOfRecors() {
        ListView onlineRecods = (ListView) findViewById(R.id.list_of_record_online_list_view);
        CustomAdapterForRecords customAdapterForRecords = new CustomAdapterForRecords(mCods, mDate, mNikName, mMoves, mTime, this);
        onlineRecods.setAdapter(customAdapterForRecords);
    }

    class MyBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context pContext, Intent pIntent) {
            String result = pIntent.getStringExtra(RefreshOnlineRecordService.SERVICE_RESPONSE_KEY);
            Toast.makeText(OnlineRecords.this, result, Toast.LENGTH_LONG).show();
        }
    }
}
