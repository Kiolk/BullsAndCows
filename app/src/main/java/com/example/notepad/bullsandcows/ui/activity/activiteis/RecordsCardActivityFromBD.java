package com.example.notepad.bullsandcows.ui.activity.activiteis;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.example.notepad.bullsandcows.R;
import com.example.notepad.bullsandcows.data.databases.DBOperations;
import com.example.notepad.bullsandcows.data.databases.models.UserRecordsDB;
import com.example.notepad.bullsandcows.data.holders.UserLoginHolder;
import com.example.notepad.bullsandcows.data.managers.RecordsManager;
import com.example.notepad.bullsandcows.data.managers.UserBaseManager;
import com.example.notepad.bullsandcows.data.models.RequestRecordModel;
import com.example.notepad.bullsandcows.data.models.ResponseRecordModel;
import com.example.notepad.bullsandcows.ui.activity.adapters.RecordRecyclerViewAdapter;
import com.example.notepad.bullsandcows.ui.activity.fragments.UserInfoRecordFragment;
import com.example.notepad.bullsandcows.utils.CheckConnection;
import com.example.notepad.bullsandcows.utils.Constants;
import com.example.notepad.myapplication.backend.recordsToNetApi.model.RecordsToNet;
import com.example.notepad.myapplication.backend.userDataBaseApi.model.UserDataBase;

import java.util.ArrayList;

public class RecordsCardActivityFromBD extends AppCompatActivity implements View.OnClickListener {

    private ArrayList<RecordsToNet> recordModelArrayList;
    private RecordRecyclerViewAdapter adapter;
    private RecordsManager mRecordsManager;
    private String mCursor;
    private boolean isLoading;
    private ProgressBar mRecordsProgressBar;
    private UserInfoRecordFragment mUserInfoFragment;
    private FrameLayout mInfoFrameLayout;
    private FragmentManager mFragmentManager;
    private FragmentTransaction mFragmentTransaction;
    private ContentValues[] mArrayContentValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_records_card);
        UserLoginHolder.getInstance().setUserOnline();
        mRecordsProgressBar = findViewById(R.id.records_load_progress_bar);
        mInfoFrameLayout = findViewById(R.id.user_info_record_frame_layout);
        mInfoFrameLayout.setOnClickListener(this);

        mCursor = null;
        mUserInfoFragment = new UserInfoRecordFragment();

        initRecordManager();

        if (CheckConnection.checkConnection(this)) {
            showProgressBar();
            mRecordsManager.getRecordSBackend(new RequestRecordModel(mCursor));
        } else {
            firstTimeShowRecycler();
        }
    }

    private void initRecordManager() {
        mRecordsManager = new RecordsManager() {

            @Override
            public ResponseRecordModel getResponseBackendCallback(ResponseRecordModel pResponse) {
                ResponseRecordModel response = super.getResponseBackendCallback(pResponse);
                Log.d(Constants.TAG, ", cursor :" + response.getmCursor());
                recordModelArrayList = response.getmRecordsArray();

                mArrayContentValues = new ContentValues[recordModelArrayList.size()];
                int i = 0;
                for(RecordsToNet note : recordModelArrayList){
                    ContentValues cv = new ContentValues();

                    cv.put(UserRecordsDB.ID, note.getDate());
                    cv.put(UserRecordsDB.NIK_NAME, note.getNikName());
                    cv.put(UserRecordsDB.MOVES, Integer.parseInt(note.getMoves()));
                    cv.put(UserRecordsDB.CODES, Integer.parseInt(note.getCodes()));
                    cv.put(UserRecordsDB.TIME, note.getTime());
                    cv.put(UserRecordsDB.USER_PHOTO_URL, note.getUserUrlPhoto());

                    mArrayContentValues[i] = cv;

                    ++i;
                }

                closeProgressBar();
                firstTimeShowRecycler();

                mCursor = response.getmCursor();

                return response;
            }
        };
    }

    private void firstTimeShowRecycler() {
        RecyclerView mRecordRecyclerView = findViewById(R.id.records_recycler_view);
        adapter = new RecordRecyclerViewAdapter(this, recordModelArrayList) {

            @Override
            public String showInfoFragment(String pUserName) {
                String userName = super.showInfoFragment(pUserName);
                closeInfoUserFragment();
                showInfoUserFragment();
                getUserInformation(userName);
                return userName;
            }
        };

        if(mArrayContentValues != null){
            new DBOperations().bulkInsert(UserRecordsDB.TABLE, mArrayContentValues);
        }

        mRecordRecyclerView.setHasFixedSize(false);
        mRecordRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecordRecyclerView.addOnScrollListener(mScrollListener);
        mRecordRecyclerView.setAdapter(adapter);

        adapter.notifyDataSetChanged();
    }

    private void getUserInformation(String pUserName) {
        UserBaseManager userManager = new UserBaseManager() {
            @Override
            public UserDataBase getFullUserInfoCallback(UserDataBase pUserData) {
                UserDataBase user = super.getFullUserInfoCallback(pUserData);
                mUserInfoFragment.showInfoAboutUser(RecordsCardActivityFromBD.this, user);

                return pUserData;
            }
        };
        userManager.checkInfoAboutUser(pUserName, "1111");
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

        if (CheckConnection.checkConnection(this)) {
            showProgressBar();
            manager.getRecordSBackend(new RequestRecordModel(mCursor));
        }
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

    private void showInfoUserFragment() {
        mInfoFrameLayout.setVisibility(View.VISIBLE);
        mFragmentTransaction = getFragmentManager().beginTransaction();
        mFragmentTransaction.add(R.id.user_info_record_frame_layout, mUserInfoFragment);
        mFragmentTransaction.commit();
        mFragmentManager = getFragmentManager();
        mFragmentManager.executePendingTransactions();
    }

    private void closeInfoUserFragment() {
        mInfoFrameLayout.setVisibility(View.INVISIBLE);
        mFragmentTransaction = getFragmentManager().beginTransaction();
        mFragmentTransaction.remove(mUserInfoFragment);
        mFragmentTransaction.commit();
    }

    @Override
    public void onClick(View v) {
        closeInfoUserFragment();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        UserLoginHolder.getInstance().keepUserOnline();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UserLoginHolder.getInstance().setOffline();
    }
}
