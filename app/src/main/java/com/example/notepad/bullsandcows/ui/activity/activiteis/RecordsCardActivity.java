package com.example.notepad.bullsandcows.ui.activity.activiteis;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.example.notepad.bullsandcows.data.managers.RecordsManager;
import com.example.notepad.bullsandcows.data.models.RequestRecordModel;
import com.example.notepad.bullsandcows.data.models.ResponseRecordModel;
import com.example.notepad.bullsandcows.R;
import com.example.notepad.bullsandcows.ui.activity.adapters.RecordRecyclerViewAdapter;
import com.example.notepad.bullsandcows.utils.CheckConnection;
import com.example.notepad.bullsandcows.utils.Constants;
import com.example.notepad.myapplication.backend.recordsToNetApi.model.RecordsToNet;

import java.util.ArrayList;

public class RecordsCardActivity extends AppCompatActivity {

    private ArrayList<RecordsToNet> recordModelArrayList;
    private RecordRecyclerViewAdapter adapter;
    private RecordsManager mRecordsManager;
    private String mCursor;
    private boolean isLoading;
    private ProgressBar mRecordsProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_records_card);
        mRecordsProgressBar = findViewById(R.id.records_load_progress_bar);
        mCursor = null;

        initRecordManager();

        if (CheckConnection.checkConnection(this)) {
            showProgressBar();
            mRecordsManager.getRecordSBackend(new RequestRecordModel(mCursor));
        }
    }

    private void initRecordManager() {
        mRecordsManager = new RecordsManager() {

            @Override
            public ResponseRecordModel getResponseBackendCallback(ResponseRecordModel pResponse) {
                ResponseRecordModel response = super.getResponseBackendCallback(pResponse);
                Log.d(Constants.TAG, ", cursor :" + response.getmCursor());
                recordModelArrayList = response.getmRecordsArray();

                closeProgressBar();
                firstTimeShowRecycler();

                mCursor = response.getmCursor();

                return response;
            }
        };
    }

    private void firstTimeShowRecycler() {
        RecyclerView mRecordRecyclerView = findViewById(R.id.records_recycler_view);
        adapter = new RecordRecyclerViewAdapter(this, recordModelArrayList);

        mRecordRecyclerView.setHasFixedSize(false);
        mRecordRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecordRecyclerView.addOnScrollListener(mScrollListener);
        mRecordRecyclerView.setAdapter(adapter);

        adapter.notifyDataSetChanged();
    }

    private void updateAdapter() {
        RecordsManager manager = new RecordsManager() {

            @Override
            public ResponseRecordModel getResponseBackendCallback(ResponseRecordModel pResponse) {
                ResponseRecordModel response = super.getResponseBackendCallback(pResponse);

                closeProgressBar();

                if (response.getmRecordsArray() != null) {
                    mCursor = response.getmCursor();
                    isLoading = false;

                    recordModelArrayList.addAll(response.getmRecordsArray());
                    adapter.notifyDataSetChanged();
                }

                return response;
            }
        };
        manager.getRecordSBackend(new RequestRecordModel(mCursor));
    }

    private RecyclerView.OnScrollListener mScrollListener = new RecyclerView.OnScrollListener() {

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
            int visibleItems = manager.getChildCount();
            int totalNumberItems = manager.getItemCount();
            int firstVisible = ((LinearLayoutManager) manager).findFirstVisibleItemPosition();

            if (!isLoading && totalNumberItems <= visibleItems + firstVisible) {
                isLoading = true;

                Log.d("MyLogs", "Stay near last position " + mCursor);
                showProgressBar();
                updateAdapter();
            }
        }
    };

    private void showProgressBar() {
        mRecordsProgressBar.setVisibility(View.VISIBLE);
    }

    private void closeProgressBar() {
        mRecordsProgressBar.setVisibility(View.GONE);
    }
}
