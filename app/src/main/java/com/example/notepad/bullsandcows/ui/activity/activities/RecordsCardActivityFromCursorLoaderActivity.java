package com.example.notepad.bullsandcows.ui.activity.activities;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.notepad.bullsandcows.R;
import com.example.notepad.bullsandcows.data.databases.models.UserRecordsDB;
import com.example.notepad.bullsandcows.data.holders.UserLoginHolder;
import com.example.notepad.bullsandcows.data.managers.RecordsManager;
import com.example.notepad.bullsandcows.data.managers.UserBaseManager;
import com.example.notepad.bullsandcows.data.models.QuerySelectionArgsModel;
import com.example.notepad.bullsandcows.data.providers.RecordsContentProvider;
import com.example.notepad.bullsandcows.services.WaiterNewRecordsService;
import com.example.notepad.bullsandcows.ui.activity.adapters.RecordRecyclerViewAdapter;
import com.example.notepad.bullsandcows.ui.activity.fragments.UserInfoRecordCursorLoaderFragment;
import com.example.notepad.bullsandcows.ui.activity.listeners.PostRecordSuccessListener;
import com.example.notepad.bullsandcows.utils.converters.ModelConverterUtil;
import com.example.notepad.bullsandcows.utils.converters.QueryConverterUtil;
import com.example.notepad.myapplication.backend.recordsToNetApi.model.RecordsToNet;

import java.util.HashMap;

import static com.example.notepad.bullsandcows.utils.Constants.IntentKeys.RECORDS_FROM_BACKEND_ON_DAY;
import static com.example.notepad.bullsandcows.utils.Constants.TAG;

public class RecordsCardActivityFromCursorLoaderActivity extends AppCompatActivity
        implements View.OnClickListener,
        LoaderManager.LoaderCallbacks<Cursor>,
        PostRecordSuccessListener {

    public static final String USER_NAME_BUNDLE_KEY = "userName";
    public static final String CODED_BUNDL_KEY = "coded";
    public static final String LAST_RESULT_BUNDLE_KEY = "lastResult";

//    private UserInfoRecordFragment mUserInfoFragment;
    private UserInfoRecordCursorLoaderFragment mUserInfoFragment;
    private FrameLayout mInfoFrameLayout;
    private FragmentTransaction mFragmentTransaction;
    private EditText mSortByName;
    private Spinner mCodedSpinner;
    private Spinner mLastTimeSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_records_card);

        UserLoginHolder.getInstance().setUserOnline();

        initView();
        initSpinners();
        initFragments();

        getLoaderManager().restartLoader(0, null, this);
    }

    private void initView() {
        Button mSortButton = findViewById(R.id.sort_record_button);
        mSortByName = findViewById(R.id.sort_by_name_record_edit_text);
        mSortButton.setOnClickListener(this);
    }

    private void initSpinners() {
        mCodedSpinner = findViewById(R.id.coded_number_sort_spinner);
        ArrayAdapter<String> codedAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, getResources()
                .getStringArray(R.array.coded_list_array));
        codedAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mCodedSpinner.setAdapter(codedAdapter);

        mLastTimeSpinner = findViewById(R.id.last_time_sort_spinner);
        ArrayAdapter<String> lastTimeAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.last_result_time_array));
        lastTimeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mLastTimeSpinner.setAdapter(lastTimeAdapter);
    }

    private void initFragments() {
        mInfoFrameLayout = findViewById(R.id.user_info_record_frame_layout);
        mInfoFrameLayout.setOnClickListener(this);
//        mUserInfoFragment = new UserInfoRecordFragment();
        mUserInfoFragment = new UserInfoRecordCursorLoaderFragment();
    }

    void startWaiterRecordService() {
        Intent intent = new Intent(RecordsCardActivityFromCursorLoaderActivity.this, WaiterNewRecordsService.class);
        intent.putExtra(RECORDS_FROM_BACKEND_ON_DAY, 1513724487000L);
        startService(intent);
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

            @Override
            public RecordsToNet updateLateRecordCallback(RecordsToNet pRecord) {
                RecordsToNet record = super.updateLateRecordCallback(pRecord);
                Toast.makeText(RecordsCardActivityFromCursorLoaderActivity.this,
                        record.getTime(), Toast.LENGTH_LONG).show();
                RecordsManager recordsManager = new RecordsManager();
                recordsManager.postRecordOnBackend(pRecord, RecordsCardActivityFromCursorLoaderActivity.this);
                return record;
            }
        };

        mRecordRecyclerView.setHasFixedSize(false);
        mRecordRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecordRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void getUserInformation(String pUserName) {

//        Cursor cursor = getContentResolver().query(RecordsContentProvider.CONTENT_USERS_URI, null, UserInfoDB.ID + " = " + pUserName,
//                null, null);
//        if(cursor.getCount() != 0){
//
//        }else {
        Log.d(TAG, "getUserInformation: " + pUserName);
        mUserInfoFragment.showUserInfo(pUserName);

         /*   UserBaseManager userManager = new UserBaseManager();
            userManager.getUserInfo(pUserName, new UserLoginCallback() {
                @Override
                public void getUserInfoCallback(UserDataBase pUserInfo) {
                    mUserInfoFragment.showInfoAboutUser(RecordsCardActivityFromCursorLoaderActivity.this, pUserInfo);

                }
            });*/
//        }
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
        switch (v.getId()) {
            case R.id.user_info_record_frame_layout:
                closeInfoUserFragment();
                break;
            case R.id.sort_record_button:
                Bundle args = new Bundle();
                args.putString(USER_NAME_BUNDLE_KEY, mSortByName.getText().toString());
                args.putString(CODED_BUNDL_KEY, mCodedSpinner.getSelectedItem().toString());
                args.putString(LAST_RESULT_BUNDLE_KEY, mLastTimeSpinner.getSelectedItem().toString());

                getLoaderManager().restartLoader(0, args, this);
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        UserLoginHolder.getInstance().keepUserOnline();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        startWaiterRecordService();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UserLoginHolder.getInstance().setOffline();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (args != null) {
//            String userName = args.getString(USER_NAME_BUNDLE_KEY);
//            String coded = args.getString(CODED_BUNDL_KEY);
//            String lastTimeSort = args.getString(LAST_RESULT_BUNDLE_KEY);
//            String[] request = new String[]{userName, coded, lastTimeSort};

            HashMap<String, String> selectionArgsMap = new HashMap<>();
            selectionArgsMap.put(UserRecordsDB.NIK_NAME, args.getString(USER_NAME_BUNDLE_KEY));
            selectionArgsMap.put(UserRecordsDB.CODES, args.getString(CODED_BUNDL_KEY));
            selectionArgsMap.put(UserRecordsDB.ID, args.getString(LAST_RESULT_BUNDLE_KEY));

            QuerySelectionArgsModel readySelection = QueryConverterUtil.convertSelectionArg(selectionArgsMap);

            return new CursorLoader(this, RecordsContentProvider.CONTENT_URI,
                    null, readySelection.getSelection(), readySelection.getSelectionArgs(), null);
        }
        return new CursorLoader(this, RecordsContentProvider.CONTENT_URI,
                null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        firstTimeShowRecycler(data);
    }


    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

//    @Override
////    @Deprecated
//    public RecordsToNet setResult(RecordsToNet pRecord) {
//        Log.d(TAG, "Start callback setResult for update i bd");
//        if (pRecord != null) {
//            ContentValues cv = ModelConverterUtil.fromRecordToNetToCv(pRecord);
//
//            cv.put(UserRecordsDB.IS_UPDATE_ONLINE, UserRecordsDB.UPDATE_ONLINE_HACK);
//
//            new UserBaseManager().checkNewBestRecord(ModelConverterUtil.fromRecordToNetToBestUserRecords(pRecord));
//            new DBOperations().update(UserRecordsDB.TABLE, cv);
//            //TODO how correct listen new changes i BD
//            getLoaderManager().getLoader(0).forceLoad();
//        }
//
//        return null;
//    }

    @Override
    public void successSetResultListener(RecordsToNet pRecord) {
        Log.d(TAG, "Start callback setResult for update i bd");
        if (pRecord != null) {
            ContentValues cv = ModelConverterUtil.fromRecordToNetToCv(pRecord);

            cv.put(UserRecordsDB.IS_UPDATE_ONLINE, UserRecordsDB.UPDATE_ONLINE_HACK);

            new UserBaseManager().checkNewBestRecord(ModelConverterUtil.fromRecordToNetToBestUserRecords(pRecord));
            getContentResolver().update(RecordsContentProvider.CONTENT_URI, cv, null, null);
        }
    }
}
