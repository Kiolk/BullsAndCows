package com.example.notepad.bullsandcows.ui.activity.fragments;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.content.ContentValues;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.notepad.bullsandcows.R;
import com.example.notepad.bullsandcows.data.holders.UserLoginHolder;
import com.example.notepad.bullsandcows.data.providers.RecordsContentProvider;
import com.example.notepad.bullsandcows.ui.activity.adapters.UserRecordsRecyclerViewAdapter;
import com.example.notepad.bullsandcows.utils.Constants;
import com.example.notepad.bullsandcows.utils.converters.TimeConvertersUtil;
import com.example.notepad.bullsandcows.utils.CountryUtils;
import com.example.notepad.bullsandcows.utils.converters.ModelConverterUtil;
import com.example.notepad.myapplication.backend.userDataBaseApi.model.BestUserRecords;
import com.example.notepad.myapplication.backend.userDataBaseApi.model.UserDataBase;

import java.util.ArrayList;

import kiolk.com.github.pen.Pen;
@Deprecated
public class UserInfoRecordFragment extends Fragment {

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
    public void showInfoAboutUser(final Context pContext, final UserDataBase pUserInfo) {

        final ContentValues contentValues = ModelConverterUtil.fromUserDataBaseToCv(pUserInfo);
        getActivity().getContentResolver().insert(RecordsContentProvider.CONTENT_USERS_URI, contentValues);

        final ArrayList<BestUserRecords> listRecords = (ArrayList<BestUserRecords>) pUserInfo.getBestUserRecords();
        Log.d("MyLogs", listRecords.toString());
        mUserName.setText(pUserInfo.getUserName());
        mLastVisit.setText(TimeConvertersUtil.convertDirectTimeToString(pUserInfo.getMLastUserVisit(), getActivity().getBaseContext()));
        final String res = Constants.EMPTY_STRING + pUserInfo.getMNumberPlayedGames();
        mPlayedGames.setText(res);

        if(pUserInfo.getIsOnline()){
            mOnlineStatus.setImageDrawable(pContext.getResources().getDrawable(R.drawable.online));
        }

        if(pUserInfo.getUserName().equals(UserLoginHolder.getInstance().getUserName())){
            mYourProfile.setVisibility(View.VISIBLE);
        }

        try {
            final int flag = CountryUtils.getCountryResources(pUserInfo.getCountry());    //R.drawable.ic_belarus;
            mCountryFlag.setImageDrawable(getResources().getDrawable(flag, null));
        } catch (final Exception pE) {
            pE.getStackTrace();
        }

        try{
            Pen.getInstance().getImageFromUrl(pUserInfo.getMPhotoUrl()).inputTo(mUseImage);
        }catch (final Exception pE){
            pE.getStackTrace();
        }

        mAdapter = new UserRecordsRecyclerViewAdapter(listRecords);

        mRecordRecyclerView.setHasFixedSize(true);
        mRecordRecyclerView.setLayoutManager(new LinearLayoutManager(pContext));
        mRecordRecyclerView.setAdapter(mAdapter);

        mAdapter.notifyDataSetChanged();

        mLastRecordAdapter = new UserRecordsRecyclerViewAdapter((ArrayList<BestUserRecords>) pUserInfo.getLastFiveUserRecords());

        mLastRecordsRecyclerView.setHasFixedSize(true);
        mLastRecordsRecyclerView.setLayoutManager(new LinearLayoutManager(pContext));
        mLastRecordsRecyclerView.setAdapter(mLastRecordAdapter);

        mLastRecordAdapter.notifyDataSetChanged();
    }
}
