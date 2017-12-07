package com.example.notepad.bullsandcows.ui.activity.fragments;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.notepad.bullsandcows.R;
import com.example.notepad.bullsandcows.ui.activity.adapters.UserRecordsRecyclerViewAdapter;
import com.example.notepad.bullsandcows.utils.Constants;
import com.example.notepad.bullsandcows.utils.Converters;
import com.example.notepad.myapplication.backend.userDataBaseApi.model.BestUserRecords;
import com.example.notepad.myapplication.backend.userDataBaseApi.model.UserDataBase;

import java.util.ArrayList;
import java.util.List;

public class UserInfoRecordFragment extends Fragment {

    private TextView mUserName;
    private TextView mLastVisit;
    private TextView mPlayedGames;
    private ImageView mCountryFlag;
    private UserRecordsRecyclerViewAdapter mAdapter;
    private RecyclerView mRecordRecyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_info_record, null);
        mUserName = (TextView) view.findViewById(R.id.nik_info_fragment_text_view);
        mRecordRecyclerView = view.findViewById(R.id.best_user_records_recycler_view);
        mCountryFlag = view.findViewById(R.id.country_flag_fragment_image_view);
        mLastVisit = view.findViewById(R.id.last_visit_fragment_image_view);
        mPlayedGames = view.findViewById(R.id.number_played_game_fragment_text_view);

        return view;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP) //TODO may be not this method not work on early version
    public void showInfoAboutUser(Context pContext, UserDataBase pUserInfo){
        ArrayList<BestUserRecords> listRecords = (ArrayList<BestUserRecords>) pUserInfo.getBestUserRecords();
        Log.d("MyLogs", listRecords.toString());
        mUserName.setText(pUserInfo.getUserName());
        mLastVisit.setText(Converters.convertDirectTimeToString(pUserInfo.getMLastUserVisit()));
        String res = Constants.EMPTY_STRING + pUserInfo.getMNumberPlayedGames();
        mPlayedGames.setText(res);
        String country = "R.drawable." + "ic_" + "belarus";
        int flag = R.drawable.ic_belarus;
        mCountryFlag.setImageDrawable(getResources().getDrawable(flag,null));
        mAdapter = new UserRecordsRecyclerViewAdapter( listRecords);

        mRecordRecyclerView.setHasFixedSize(true);
        mRecordRecyclerView.setLayoutManager(new LinearLayoutManager(pContext));
        mRecordRecyclerView.setAdapter(mAdapter);

        mAdapter.notifyDataSetChanged();
    }
}
