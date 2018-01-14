package com.example.notepad.bullsandcows.ui.activity.fragments;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.notepad.bullsandcows.R;
import com.example.notepad.bullsandcows.data.databases.models.UserInfoDB;
import com.example.notepad.bullsandcows.data.managers.OnResultCallback;
import com.example.notepad.bullsandcows.data.parsers.JsonParser;
import com.example.notepad.bullsandcows.data.holders.UserLoginHolder;
import com.example.notepad.bullsandcows.data.managers.UserBaseManager;
import com.example.notepad.bullsandcows.data.providers.RecordsContentProvider;
import com.example.notepad.bullsandcows.ui.activity.adapters.UserRecordsRecyclerViewAdapter;
import com.example.notepad.bullsandcows.utils.CountryUtils;
import com.example.notepad.bullsandcows.utils.animation.SlideAnimationUtil;
import com.example.notepad.bullsandcows.utils.converters.TimeConvertersUtil;
import com.example.notepad.myapplication.backend.userDataBaseApi.model.BestUserRecords;
import com.example.notepad.myapplication.backend.userDataBaseApi.model.UserDataBase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import kiolk.com.github.pen.Pen;

public class UserInfoRecordCursorLoaderFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private String mUserNik;

    private TextView mUserName;
    private TextView mLastVisit;
    private TextView mPlayedGames;
    private TextView mYourProfile;

    private ImageView mCountryFlag;
    private ImageView mUseImage;
    private ImageView mOnlineStatus;

    private RecyclerView mRecordRecyclerView;
    private RecyclerView mLastRecordsRecyclerView;

    private RelativeLayout mUpperBlock;
    private RelativeLayout mMiddleBlock;
    private RelativeLayout mLastBlock;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_user_info_record, null);

        mUserName = view.findViewById(R.id.nik_info_fragment_text_view);
        mRecordRecyclerView = view.findViewById(R.id.best_user_records_recycler_view);
        mCountryFlag = view.findViewById(R.id.country_flag_fragment_image_view);
        mLastVisit = view.findViewById(R.id.last_visit_fragment_image_view);
        mPlayedGames = view.findViewById(R.id.number_played_game_fragment_text_view);
        mLastRecordsRecyclerView = view.findViewById(R.id.last_five_user_records_recycler_view);
        mUseImage = view.findViewById(R.id.user_image_info_fragment_image_view);
        mYourProfile = view.findViewById(R.id.your_profile_page_text_view);
        mOnlineStatus = view.findViewById(R.id.online_status_fragment_image_view);
        mUpperBlock = view.findViewById(R.id.upper_user_info_block_relative_layout);
        mMiddleBlock = view.findViewById(R.id.middle_user_info_block_relative_layout);
        mLastBlock = view.findViewById(R.id.last_user_info_block_relative_layout);

        return view;
    }

    private void setupData(final Cursor cursor) {
        cursor.moveToFirst();
        final String json = cursor.getString(cursor.getColumnIndex(UserInfoDB.USERS_BEST_RECORDS));
        final BestUserRecords[] records = JsonParser.getBestUserRecordsFromJson(json);
        final List<BestUserRecords> listRecords = new ArrayList<>(Arrays.asList(records));

        mUserName.setText(cursor.getString(cursor.getColumnIndex(UserInfoDB.ID)));
        mLastVisit.setText(TimeConvertersUtil.convertDirectTimeToString(cursor.getLong(cursor.getColumnIndex(UserInfoDB.LAST_VISIT)), mUserName.getContext()));
        mPlayedGames.setText(String.valueOf(cursor.getInt(cursor.getColumnIndex(UserInfoDB.NUMBER_OF_PLAYED_GAMES))));

        if (cursor.getInt(cursor.getColumnIndex(UserInfoDB.IS_ONLINE)) == 1) {
            mOnlineStatus.setImageDrawable(getActivity().getBaseContext().getResources().getDrawable(R.drawable.online));
        }

        if (cursor.getString(cursor.getColumnIndex(UserInfoDB.ID)).equals(UserLoginHolder.getInstance().getUserName())) {
            mYourProfile.setVisibility(View.VISIBLE);
        }

        //TODO refactor!
        try {
            final int flag = CountryUtils.getCountryResources(cursor.getString(cursor.getColumnIndex(UserInfoDB.COUNTRY)));    //R.drawable.ic_belarus;
            mCountryFlag.setImageDrawable(getResources().getDrawable(flag));
        } catch (final Exception pE) {
            pE.getStackTrace();
        }

        try {
            Pen.getInstance().getImageFromUrl(cursor.getString(cursor.getColumnIndex(UserInfoDB.AVATAR_URL))).inputTo(mUseImage);
        } catch (final Exception pE) {
            pE.getStackTrace();
        }

        final UserRecordsRecyclerViewAdapter adapter = new UserRecordsRecyclerViewAdapter(listRecords);

        mRecordRecyclerView.setHasFixedSize(true);
        mRecordRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getBaseContext()));
        mRecordRecyclerView.setAdapter(adapter);

        adapter.notifyDataSetChanged();

        final String jsonLastRecords = cursor.getString(cursor.getColumnIndex(UserInfoDB.USER_LAST_RECORDS));

        final BestUserRecords[] lastRecords = JsonParser.getBestUserRecordsFromJson(jsonLastRecords);
        final UserRecordsRecyclerViewAdapter lastRecordAdapter = new UserRecordsRecyclerViewAdapter(new ArrayList<>(Arrays.asList(lastRecords)));

        mLastRecordsRecyclerView.setHasFixedSize(true);
        mLastRecordsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getBaseContext()));
        mLastRecordsRecyclerView.setAdapter(lastRecordAdapter);
    }

    public void showUserInfo(final String pUserName) {
        mUserNik = pUserName;
        final Bundle args = new Bundle();
        args.putString(UserInfoDB.ID, UserInfoDB.ID + " = ?");

        final UserBaseManager baseManager = new UserBaseManager();
        baseManager.getUserInfo(getActivity().getBaseContext(), pUserName, new OnResultCallback<UserDataBase>() {

            @Override
            public void onSuccess(final UserDataBase pResult) {
                getActivity().getLoaderManager().restartLoader(5, args, UserInfoRecordCursorLoaderFragment.this);
            }

            @Override
            public void onError(final Exception pException) {

            }
        });

//        new UserLoginCallback() {
//
//            @Override
//            public void getUserInfoCallback(final UserDataBase pUserInfo) {
//                getActivity().getLoaderManager().restartLoader(5, args, UserInfoRecordCursorLoaderFragment.this);
//            }
//        });

        mUpperBlock.setVisibility(View.VISIBLE);
        SlideAnimationUtil.slideInFromLeft(getActivity().getBaseContext(), mUpperBlock, null, SlideAnimationUtil.FASTER);
        mMiddleBlock.setVisibility(View.VISIBLE);
        SlideAnimationUtil.slideInFromRight(getActivity().getBaseContext(), mMiddleBlock, null, SlideAnimationUtil.FASTER);
        mLastBlock.setVisibility(View.VISIBLE);
        SlideAnimationUtil.slideInFromLeft(getActivity().getBaseContext(), mLastBlock, null, SlideAnimationUtil.FASTER);

    }

    @Override
    public Loader<Cursor> onCreateLoader(final int id, final Bundle args) {
        final String selection = args.getString(UserInfoDB.ID);
        final String[] selectionArgs = new String[]{mUserNik};
        return new CursorLoader(getActivity().getBaseContext(), RecordsContentProvider.CONTENT_USERS_URI,
                null, selection, selectionArgs, null);
    }

    @Override
    public void onLoadFinished(final Loader<Cursor> loader, final Cursor data) {

        if (data.getCount() != 0) {
            setupData(data);
        }
    }

    @Override
    public void onLoaderReset(final Loader<Cursor> loader) {

    }
}
