package com.example.notepad.bullsandcows;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomAdapterForRecords extends BaseAdapter {

    ArrayList<String> mCodNumbers = new ArrayList<>();
    ArrayList<String> mDateWon = new ArrayList<>();
    ArrayList<String> mNikName = new ArrayList<>();
    ArrayList<String> mMovesForWon = new ArrayList<>();
    ArrayList<String> mTimeNeed = new ArrayList<>();
    LayoutInflater mLayoutInflater;
    Context mContext;

    public CustomAdapterForRecords(ArrayList<String> pCodNumbers, ArrayList<String> pDateWon, ArrayList<String> pNikName, ArrayList<String> pMovesForWon, ArrayList<String> pTimeNeed, Context pContext) {
        mCodNumbers = pCodNumbers;
        mDateWon = pDateWon;
        mNikName = pNikName;
        mMovesForWon = pMovesForWon;
        mTimeNeed = pTimeNeed;
        mContext = pContext;
        mLayoutInflater = (LayoutInflater.from(pContext));
    }


    @Override
    public int getCount() {
        return mCodNumbers.size();
    }

    @Override
    public Object getItem(int pI) {
        return null;
    }

    @Override
    public long getItemId(int pI) {
        return 0;
    }

    @Override
    public View getView(int pI, View pView, ViewGroup pViewGroup) {
        pView = mLayoutInflater.inflate(R.layout.list_records, null);
        TextView codeNumbers = (TextView) pView.findViewById(R.id.coded_number_text_view);
        codeNumbers.setText(mCodNumbers.get(pI));
        TextView date = (TextView) pView.findViewById(R.id.date_of_won_text_view);
        date.setText(mDateWon.get(pI));
        TextView nikName = (TextView) pView.findViewById(R.id.nik_name_text_view);
        nikName.setText(mNikName.get(pI));
        TextView moves = (TextView) pView.findViewById(R.id.moves_for_won_text_view);
        moves.setText(mMovesForWon.get(pI));
        TextView time = (TextView) pView.findViewById(R.id.need_time_for_won_text_view);
        time.setText(mTimeNeed.get(pI));
        return pView;
    }
}


