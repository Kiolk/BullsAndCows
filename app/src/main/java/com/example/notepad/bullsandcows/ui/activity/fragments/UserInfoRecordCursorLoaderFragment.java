package com.example.notepad.bullsandcows.ui.activity.fragments;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.notepad.bullsandcows.R;
import com.example.notepad.bullsandcows.data.databases.models.UserInfoDB;
import com.example.notepad.bullsandcows.data.factories.JsonParser;
import com.example.notepad.bullsandcows.data.holders.UserLoginHolder;
import com.example.notepad.bullsandcows.data.managers.UserBaseManager;
import com.example.notepad.bullsandcows.data.managers.UserLoginCallback;
import com.example.notepad.bullsandcows.data.providers.RecordsContentProvider;
import com.example.notepad.bullsandcows.ui.activity.adapters.UserRecordsRecyclerViewAdapter;
import com.example.notepad.bullsandcows.utils.CountryUtils;
import com.example.notepad.bullsandcows.utils.converters.Converters;
import com.example.notepad.bullsandcows.utils.converters.ModelConverterUtil;
import com.example.notepad.myapplication.backend.userDataBaseApi.model.BestUserRecords;
import com.example.notepad.myapplication.backend.userDataBaseApi.model.UserDataBase;

import java.util.ArrayList;
import java.util.Arrays;

import kiolk.com.github.pen.Pen;

public class UserInfoRecordCursorLoaderFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private TextView mUserName;
    private TextView mLastVisit;
    private TextView mPlayedGames;
    private TextView mYourProfile;
    private ImageView mCountryFlag;
    private ImageView mUseImage;
    private ImageView mOnlineStatus;
    private UserRecordsRecyclerViewAdapter mAdapter;
    private UserRecordsRecyclerViewAdapter mLastRecordAdapter;
    private RecyclerView mRecordRecyclerView;
    private RecyclerView mLastRecordsRecyclerView;
    private String mUserName1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_info_record, null);

        mUserName = view.findViewById(R.id.nik_info_fragment_text_view);
        mRecordRecyclerView = view.findViewById(R.id.best_user_records_recycler_view);
        mCountryFlag = view.findViewById(R.id.country_flag_fragment_image_view);
        mLastVisit = view.findViewById(R.id.last_visit_fragment_image_view);
        mPlayedGames = view.findViewById(R.id.number_played_game_fragment_text_view);
        mLastRecordsRecyclerView = view.findViewById(R.id.last_five_user_records_recycler_view);
        mUseImage = view.findViewById(R.id.user_image_info_fragment_image_view);
        mYourProfile = view.findViewById(R.id.your_profile_page_text_view);
        mOnlineStatus = view.findViewById(R.id.online_status_fragment_image_view);

        return view;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP) //TODO may be not this method not work on early version
    private void setupData(Cursor cursor) {
        cursor.moveToFirst();
//        ContentValues cv = ModelConverterUtil.fromUserDataBaseToCv(pUserInfo);
//        getActivity().getContentResolver().insert(RecordsContentProvider.CONTENT_USERS_URI, cv);
        String json = cursor.getString(cursor.getColumnIndex(UserInfoDB.USERS_BEST_RECORDS));
//        BestUserRecords[] records = new Gson().fromJson(cursor.getString(cursor.getColumnIndex(UserInfoDB.USERS_BEST_RECORDS)), BestUserRecords[].class);
        BestUserRecords[] records = JsonParser.getBestUserRecordsFromJson(json);
        ArrayList<BestUserRecords> listRecords = new ArrayList<BestUserRecords>(Arrays.asList(records));
        //    Log.d("MyLogs", listRecords.toString());

        mUserName.setText(cursor.getString(cursor.getColumnIndex(UserInfoDB.ID)));
        mLastVisit.setText(Converters.convertDirectTimeToString(cursor.getLong(cursor.getColumnIndex(UserInfoDB.LAST_VISIT))));
//        String res = Constants.EMPTY_STRING + pUserInfo.getMNumberPlayedGames();
        mPlayedGames.setText(String.valueOf(cursor.getInt(cursor.getColumnIndex(UserInfoDB.NUMBER_OF_PLAYED_GAMES))));

        if (cursor.getInt(cursor.getColumnIndex(UserInfoDB.IS_ONLINE)) == 1) {
            mOnlineStatus.setImageDrawable(getActivity().getBaseContext().getResources().getDrawable(R.drawable.online));
        }

        if (cursor.getString(cursor.getColumnIndex(UserInfoDB.ID)).equals(UserLoginHolder.getInstance().getUserName())) {
            mYourProfile.setVisibility(View.VISIBLE);
        }

        try {
            int flag = CountryUtils.getCountryResources(cursor.getString(cursor.getColumnIndex(UserInfoDB.COUNTRY)));    //R.drawable.ic_belarus;
            mCountryFlag.setImageDrawable(getResources().getDrawable(flag, null));
        } catch (Exception pE) {
            pE.getStackTrace();
        }

        try {
            Pen.getInstance().getImageFromUrl(cursor.getString(cursor.getColumnIndex(UserInfoDB.AVATAR_URL))).inputTo(mUseImage);
        } catch (Exception pE) {
            pE.getStackTrace();
        }

        mAdapter = new UserRecordsRecyclerViewAdapter(listRecords);

        mRecordRecyclerView.setHasFixedSize(true);
        mRecordRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getBaseContext()));
        mRecordRecyclerView.setAdapter(mAdapter);

        mAdapter.notifyDataSetChanged();

        String jsonLastRecords = cursor.getString(cursor.getColumnIndex(UserInfoDB.USER_LAST_RECORDS));

        BestUserRecords[] lastRecords = JsonParser.getBestUserRecordsFromJson(jsonLastRecords);
        mLastRecordAdapter = new UserRecordsRecyclerViewAdapter(new ArrayList<BestUserRecords>(Arrays.asList(lastRecords)));

        mLastRecordsRecyclerView.setHasFixedSize(true);
        mLastRecordsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getBaseContext()));
        mLastRecordsRecyclerView.setAdapter(mLastRecordAdapter);
    }

    public void showUserInfo(String pUserName) {
        mUserName1 = pUserName;
       final Bundle args = new Bundle();
        args.putString(UserInfoDB.ID, UserInfoDB.ID + " = ?");

        UserBaseManager baseManager = new UserBaseManager();
        baseManager.getUserInfo(getActivity().getBaseContext(), pUserName, new UserLoginCallback() {
            @Override
            public void getUserInfoCallback(UserDataBase pUserInfo) {
                getActivity().getLoaderManager().restartLoader(5, args, UserInfoRecordCursorLoaderFragment.this);
            }
        });


    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String selection = args.getString(UserInfoDB.ID);
        String[] selectionArgs = new String[]{mUserName1};
        return new CursorLoader(getActivity().getBaseContext(), RecordsContentProvider.CONTENT_USERS_URI,
                null, selection, selectionArgs, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, final Cursor data) {

        if (data.getCount() != 0) {
            setupData(data);
        }
//        else {
//
//            UserBaseManager userBaseManager = new UserBaseManager();
//            userBaseManager.getUserInfo(getActivity().getBaseContext(), mUserName1, new UserLoginCallback() {
//                @Override
//                public void getUserInfoCallback(UserDataBase pUserInfo) {
//                    //TODO Question:possible cursor would be final, if it signature for notifications
//                    if (pUserInfo != null) {
//                        ContentValues cv = ModelConverterUtil.fromUserDataBaseToCv(pUserInfo);
//                        Uri profileUri = getActivity().getContentResolver().insert(RecordsContentProvider.CONTENT_USERS_URI, cv);
//                        if(Integer.parseInt(profileUri.getLastPathSegment()) == -1){
//                            getActivity().getContentResolver().update(RecordsContentProvider.CONTENT_USERS_URI, cv, null, null);
//                        }
//                        showUserInfo(mUserName1);
//                    }
//                }
//            });
//        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
