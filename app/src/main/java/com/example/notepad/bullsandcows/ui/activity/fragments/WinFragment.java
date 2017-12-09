package com.example.notepad.bullsandcows.ui.activity.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.notepad.bullsandcows.R;

public class WinFragment extends Fragment {

    private TextView mWinText;
    private TextView mResultText;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.win_fragment, null);
        mWinText = view.findViewById(R.id.win_text_view);
        mResultText = view.findViewById(R.id.win_result_text_view);
        return view;
    }

    public void setWinMessage(String pUserName, int pCodedNumber, int pMoves, String pWinTime) {
        String winMessage = getResources().getString(R.string.CONGRATULATIONS) + pUserName + getResources().getString(R.string.YOU_WIN);
        mWinText.setText(winMessage);
        String winResult = getResources().getString(R.string.YOUR_RESULT) + pCodedNumber + getResources().getString(R.string.NUMBER_OF_DIGITS) + pMoves + getResources().getString(R.string.WIN_TIME) + pWinTime;
        mResultText.setText(winResult);
    }
}
