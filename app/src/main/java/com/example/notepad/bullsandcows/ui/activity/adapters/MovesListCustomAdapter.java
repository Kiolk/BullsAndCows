package com.example.notepad.bullsandcows.ui.activity.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.notepad.bullsandcows.R;

import java.util.ArrayList;

public class MovesListCustomAdapter extends BaseAdapter{

    Context mContext;
    ArrayList<String> mMoves = new ArrayList<>();
    ArrayList<String> mNumbers = new ArrayList<>();
    ArrayList<String> mCows = new ArrayList<>();
    ArrayList<String> mBuls = new ArrayList<>();
    LayoutInflater mLayoutInflater;
    Boolean mColorMode;

    public MovesListCustomAdapter(Context aplicationContext, ArrayList<String> mMoves, ArrayList<String> mNumbers, ArrayList<String> mBuls, ArrayList<String> mCows, Boolean pBoolean){
        this.mContext = aplicationContext;
        this.mMoves = mMoves;
        this.mNumbers = mNumbers;
        this.mCows = mCows;
        this.mBuls = mBuls;
        mLayoutInflater = (LayoutInflater.from(aplicationContext));
        this.mColorMode = pBoolean;
    }


    @Override
    public int getCount() {

        return mMoves.size();
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


        pView = mLayoutInflater.inflate(R.layout.list_moves, null);
        if (mColorMode) {
            TextView pBull = (TextView) pView.findViewById(R.id.bulls_list_text_view);
            pBull.setTextColor(Color.WHITE);
            TextView pCow = (TextView) pView.findViewById(R.id.cows_list_text_view);
            pCow.setTextColor(Color.WHITE);
            TextView pNumber = (TextView) pView.findViewById(R.id.number_list_text_view);
            pNumber.setTextColor(Color.WHITE);
            TextView pCnt = (TextView) pView.findViewById(R.id.cnt_move_list_text_view);
            pCnt.setTextColor(Color.WHITE);
        } else {
            TextView pBull = (TextView) pView.findViewById(R.id.bulls_list_text_view);
            pBull.setTextColor(Color.BLACK);
            TextView pCow = (TextView) pView.findViewById(R.id.cows_list_text_view);
            pCow.setTextColor(Color.BLACK);
            TextView pNumber = (TextView) pView.findViewById(R.id.number_list_text_view);
            pNumber.setTextColor(Color.BLACK);
            TextView pCnt = (TextView) pView.findViewById(R.id.cnt_move_list_text_view);
            pCnt.setTextColor(Color.BLACK);
        }
        TextView cnt = (TextView) pView.findViewById(R.id.cnt_move_list_text_view);
        cnt.setText(mMoves.get(pI));
        TextView number = (TextView) pView.findViewById(R.id.number_list_text_view);
        number.setText(mNumbers.get(pI));
        TextView bulls = (TextView) pView.findViewById(R.id.bulls_list_text_view);
        bulls.setText(mBuls.get(pI));
        TextView cows = (TextView) pView.findViewById(R.id.cows_list_text_view);
        cows.setText(mCows.get(pI));
        return pView;
    }
}
