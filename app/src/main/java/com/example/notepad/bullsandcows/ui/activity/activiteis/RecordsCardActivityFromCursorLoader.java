package com.example.notepad.bullsandcows.ui.activity.activiteis;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import com.example.notepad.bullsandcows.R;
import com.example.notepad.bullsandcows.data.Loaders.CursorDBLoader;
import com.example.notepad.bullsandcows.data.holders.UserLoginHolder;
import com.example.notepad.bullsandcows.data.managers.UserBaseManager;
import com.example.notepad.bullsandcows.services.WaiterNewRecordsService;
import com.example.notepad.bullsandcows.ui.activity.adapters.RecordRecyclerViewAdapter;
import com.example.notepad.bullsandcows.ui.activity.fragments.UserInfoRecordFragment;
import com.example.notepad.bullsandcows.utils.CheckConnection;
import com.example.notepad.myapplication.backend.userDataBaseApi.model.UserDataBase;

import java.util.Timer;
import java.util.TimerTask;
import static com.example.notepad.bullsandcows.utils.Constants.TAG;

public class RecordsCardActivityFromCursorLoader extends AppCompatActivity implements View.OnClickListener, LoaderManager.LoaderCallbacks<Cursor> {

    private UserInfoRecordFragment mUserInfoFragment;
    private FrameLayout mInfoFrameLayout;
    private FragmentTransaction mFragmentTransaction;
    private TimerTask mTimerTask;
    private Timer mTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_records_card);

        UserLoginHolder.getInstance().setUserOnline();

        initFragments();
        initTimer();

        getLoaderManager().initLoader(0, null, this);
        getLoaderManager().getLoader(0).forceLoad();
    }

    private void initFragments() {
        mInfoFrameLayout = findViewById(R.id.user_info_record_frame_layout);
        mInfoFrameLayout.setOnClickListener(this);
        mUserInfoFragment = new UserInfoRecordFragment();
    }

    void initTimer() {
        mTimerTask = new TimerTask() {
            @Override
            public void run() {
                Log.d(TAG, "TimerTask run");
                Intent intent = new Intent(RecordsCardActivityFromCursorLoader.this, WaiterNewRecordsService.class);
                startService(intent);
            }
        };

        mTimer = new Timer();

        if (CheckConnection.checkConnection(RecordsCardActivityFromCursorLoader.this)) {
            mTimer.scheduleAtFixedRate(mTimerTask, 1000, 15000);
        }
    }

    private void firstTimeShowRecycler(Cursor pCursor) {
        RecyclerView mRecordRecyclerView = findViewById(R.id.records_recycler_view);

        RecordRecyclerViewAdapter adapter = new RecordRecyclerViewAdapter(this, pCursor) {

            @Override
            public String showInfoFragment(String pUserName) {
                String userName = super.showInfoFragment(pUserName);
                closeInfoUserFragment();
                showInfoUserFragment();
                getUserInformation(userName);
                return userName;
            }
        };


        mRecordRecyclerView.setHasFixedSize(false);
        mRecordRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecordRecyclerView.setAdapter(adapter);
    }

    private void getUserInformation(String pUserName) {
        UserBaseManager userManager = new UserBaseManager() {
            @Override
            public UserDataBase getFullUserInfoCallback(UserDataBase pUserData) {
                UserDataBase user = super.getFullUserInfoCallback(pUserData);
                mUserInfoFragment.showInfoAboutUser(RecordsCardActivityFromCursorLoader.this, user);

                return pUserData;
            }
        };
        userManager.checkInfoAboutUser(pUserName, "1111");
    }

    private void showInfoUserFragment() {
        mInfoFrameLayout.setVisibility(View.VISIBLE);
        mFragmentTransaction = getFragmentManager().beginTransaction();
        mFragmentTransaction.add(R.id.user_info_record_frame_layout, mUserInfoFragment);
        mFragmentTransaction.commit();
        FragmentManager mFragmentManager = getFragmentManager();
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
        mTimer.cancel();
        mTimerTask.cancel();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorDBLoader(RecordsCardActivityFromCursorLoader.this);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            firstTimeShowRecycler(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }
}
