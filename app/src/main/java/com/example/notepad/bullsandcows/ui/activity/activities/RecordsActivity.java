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
import com.example.notepad.bullsandcows.data.managers.OnResultCallback;
import com.example.notepad.bullsandcows.data.managers.UserBaseManager;
import com.example.notepad.bullsandcows.data.models.QuerySelectionArgsModel;
import com.example.notepad.bullsandcows.data.providers.RecordsContentProvider;
import com.example.notepad.bullsandcows.services.WaiterNewRecordsService;
import com.example.notepad.bullsandcows.ui.activity.adapters.RecordRecyclerViewAdapter;
import com.example.notepad.bullsandcows.ui.activity.fragments.UserInfoRecordCursorLoaderFragment;
import com.example.notepad.bullsandcows.utils.converters.ModelConverterUtil;
import com.example.notepad.bullsandcows.utils.converters.QuerySelectionFormer;
import com.example.notepad.bullsandcows.utils.converters.TimeConvertersUtil;
import com.example.notepad.myapplication.backend.recordsToNetApi.model.RecordsToNet;

import java.util.HashMap;
import java.util.Map;

import static com.example.notepad.bullsandcows.utils.Constants.DBConstants.CODED_BUNDLE_KEY;
import static com.example.notepad.bullsandcows.utils.Constants.DBConstants.LAST_RESULT_BUNDLE_KEY;
import static com.example.notepad.bullsandcows.utils.Constants.DBConstants.USER_NAME_BUNDLE_KEY;
import static com.example.notepad.bullsandcows.utils.Constants.IntentKeys.RECORDS_FROM_BACKEND_ON_DAY;

//TODO refactor implements to small class and create instances inside activitt
public class RecordsActivity extends AppCompatActivity
        implements View.OnClickListener,
        LoaderManager.LoaderCallbacks<Cursor>{

    private UserInfoRecordCursorLoaderFragment mUserInfoFragment;
    private FrameLayout mInfoFrameLayout;
    private FragmentTransaction mFragmentTransaction;
    private EditText mSortByName;
    private Spinner mCodedSpinner;
    private Spinner mLastTimeSpinner;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
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
        final Button mSortButton = findViewById(R.id.sort_record_button);
        mSortByName = findViewById(R.id.sort_by_name_record_edit_text);
        mSortButton.setOnClickListener(this);
    }

    private void initSpinners() {
        mCodedSpinner = findViewById(R.id.coded_number_sort_spinner);
        final ArrayAdapter<String> codedAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, getResources()
                .getStringArray(R.array.coded_list_array));
        codedAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mCodedSpinner.setAdapter(codedAdapter);

        mLastTimeSpinner = findViewById(R.id.last_time_sort_spinner);
        final ArrayAdapter<String> lastTimeAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.last_result_time_array));
        lastTimeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mLastTimeSpinner.setAdapter(lastTimeAdapter);
    }

    private void initFragments() {
        mInfoFrameLayout = findViewById(R.id.user_info_record_frame_layout);
        mInfoFrameLayout.setOnClickListener(this);
        mUserInfoFragment = new UserInfoRecordCursorLoaderFragment();
    }

    void startWaiterRecordService() {
        final Intent intent = new Intent(this, WaiterNewRecordsService.class);
        intent.putExtra(RECORDS_FROM_BACKEND_ON_DAY, TimeConvertersUtil.getActualDay(System.currentTimeMillis()));
        startService(intent);
    }

    private void firstTimeShowRecycler(final Cursor pCursor) {
        final RecyclerView mRecordRecyclerView = findViewById(R.id.records_recycler_view);

        final RecordRecyclerViewAdapter adapter = new RecordRecyclerViewAdapter(this, pCursor) {

            @Override
            public String showInfoFragment(final String pUserName) {
                final String userName = super.showInfoFragment(pUserName);
                closeInfoUserFragment();
                showInfoUserFragment();
                getUserInformation(userName);
                return userName;
            }

            @Override
            public RecordsToNet updateLateRecordCallback(final RecordsToNet pRecord) {
                final RecordsToNet record = super.updateLateRecordCallback(pRecord);
                Toast.makeText(RecordsActivity.this,
                        record.getTime(), Toast.LENGTH_LONG).show();
                final RecordsManager recordsManager = new RecordsManager();

                recordsManager.postRecordOnBackend(pRecord, new OnResultCallback<RecordsToNet>() {

                    @Override
                    public void onSuccess(final RecordsToNet pResult) {
                        final ContentValues cv = ModelConverterUtil.fromRecordToNetToCv(pRecord);

                        cv.put(UserRecordsDB.IS_UPDATE_ONLINE, UserRecordsDB.UPDATE_ONLINE_HACK);

                        new UserBaseManager().checkNewBestRecord(ModelConverterUtil.fromRecordToNetToBestUserRecords(pRecord));
                        getContentResolver().update(RecordsContentProvider.CONTENT_URI, cv, null, null);
                    }

                    @Override
                    public void onError(final Exception pException) {

                    }
                });
                return record;
            }
        };

        mRecordRecyclerView.setHasFixedSize(false);
        mRecordRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecordRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    protected void getUserInformation(final String pUserName) {
        mUserInfoFragment.showUserInfo(pUserName);
    }

    protected void showInfoUserFragment() {
        mInfoFrameLayout.setVisibility(View.VISIBLE);
        mFragmentTransaction = getFragmentManager().beginTransaction();
        mFragmentTransaction.add(R.id.user_info_record_frame_layout, mUserInfoFragment);
        mFragmentTransaction.commit();
        final FragmentManager mFragmentManager = getFragmentManager();
        mFragmentManager.executePendingTransactions();
    }

    private void closeInfoUserFragment() {
        mInfoFrameLayout.setVisibility(View.INVISIBLE);
        mFragmentTransaction = getFragmentManager().beginTransaction();
        mFragmentTransaction.remove(mUserInfoFragment);
        mFragmentTransaction.commit();
    }

    @Override
    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.user_info_record_frame_layout:
                closeInfoUserFragment();
                break;
            case R.id.sort_record_button:
                final Bundle args = new Bundle();
                args.putString(USER_NAME_BUNDLE_KEY, mSortByName.getText().toString());
                args.putString(CODED_BUNDLE_KEY, mCodedSpinner.getSelectedItem().toString());
                args.putString(LAST_RESULT_BUNDLE_KEY, mLastTimeSpinner.getSelectedItem().toString());

                getLoaderManager().restartLoader(0, args, this);
                break;
            default:
                break;
        }
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
    public Loader<Cursor> onCreateLoader(final int id, final Bundle args) {
        if (args != null) {

            final Map<String, String> selectionArgsMap = new HashMap<>();
            selectionArgsMap.put(UserRecordsDB.NIK_NAME, args.getString(USER_NAME_BUNDLE_KEY));
            selectionArgsMap.put(UserRecordsDB.CODES, args.getString(CODED_BUNDLE_KEY));
            selectionArgsMap.put(UserRecordsDB.ID, args.getString(LAST_RESULT_BUNDLE_KEY));

            final QuerySelectionArgsModel readySelection = QuerySelectionFormer.convertSelectionArg(selectionArgsMap);

            return new CursorLoader(this, RecordsContentProvider.CONTENT_URI,
                    null, readySelection.getSelection(), readySelection.getSelectionArgs(), null);
        }
        return new CursorLoader(this, RecordsContentProvider.CONTENT_URI,
                null, null, null, null);
    }

    @Override
    public void onLoadFinished(final Loader<Cursor> loader, final Cursor data) {
        firstTimeShowRecycler(data);
    }

    @Override
    public void onLoaderReset(final Loader<Cursor> loader) {
    }

//    @Override
//    public void successSetResultListener(final RecordsToNet pRecord) {
//        Log.d(TAG, "Start callback setResult for update i bd");
//        if (pRecord != null) {
//            final ContentValues cv = ModelConverterUtil.fromRecordToNetToCv(pRecord);
//
//            cv.put(UserRecordsDB.IS_UPDATE_ONLINE, UserRecordsDB.UPDATE_ONLINE_HACK);
//
//            new UserBaseManager().checkNewBestRecord(ModelConverterUtil.fromRecordToNetToBestUserRecords(pRecord));
//            getContentResolver().update(RecordsContentProvider.CONTENT_URI, cv, null, null);
//        }
//    }
}
