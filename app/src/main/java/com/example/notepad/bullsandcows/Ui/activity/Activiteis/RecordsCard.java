package com.example.notepad.bullsandcows.Ui.activity.Activiteis;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.notepad.bullsandcows.Data.Factories.RecordJsonFactory;
import com.example.notepad.bullsandcows.Data.Models.RecordModel;
import com.example.notepad.bullsandcows.R;
import com.example.notepad.bullsandcows.Ui.activity.Adapters.RecordRecyclerViewAdapter;
import com.example.notepad.bullsandcows.Utils.CheckConnection;
import com.example.notepad.myapplication.backend.recordsToNetApi.RecordsToNetApi;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;

import java.io.IOException;
import java.util.ArrayList;

public class RecordsCard extends AppCompatActivity {

    RecyclerView mRecordRecyclerView;
    ArrayList<RecordModel> recordModelArrayList;
    RecordRecyclerViewAdapter adapter;
    Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_records_card);
        initializationHandler();
//        firstTimeShowRecycler();
        if(CheckConnection.checkConnection(this)){
            getDataFromBackend();
        }
//        firstTimeShowRecycler();
    }

    private void firstTimeShowRecycler() {
        mRecordRecyclerView = findViewById(R.id.records_recycler_view);
        mRecordRecyclerView.setHasFixedSize(false);
        mRecordRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecordRecyclerViewAdapter(this, recordModelArrayList);
        mRecordRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void getDataFromBackend (){
        Thread myThread = new Thread(new Runnable() {
            @Override
            public void run() {
                RecordsToNetApi myApiService = null;

                if (myApiService == null) {
                    RecordsToNetApi.Builder builder = new RecordsToNetApi.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
                            .setRootUrl("https://onlinerecordbulsandcows.appspot.com/_ah/api/");

                    myApiService = builder.build();
                }

                try {
                    String cursor = "CjoSNGoZZ35vbmxpbmVyZWNvcmRidWxzYW5kY293c3IXCxIMUmVjb3Jkc1RvTmV0GIGk8JX5KwwYACAA";
                    String json = myApiService.list().setCursor(cursor).execute().toString();
                    Message msg = new Message();
                    msg.obj = RecordJsonFactory.getArrayRecordsFromJson(json);
                    mHandler.sendMessage(msg);
                } catch (IOException pE) {
                    pE.printStackTrace();
                }
            }
        });
        myThread.start();
    }

    private void initializationHandler(){
        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                recordModelArrayList = (ArrayList<RecordModel>) msg.obj;
                firstTimeShowRecycler();
//                adapter.notifyDataSetChanged();
            }
        };
    }
}
