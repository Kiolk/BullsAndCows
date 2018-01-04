package com.example.notepad.bullsandcows.ui.activity.activities;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.example.notepad.bullsandcows.R;

public class RulesPageActivity extends AppCompatActivity {

    private int mCnt;
    private String[] mRulesArray;
    private TextView mRuleText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_rulespage);

        mCnt = 0;
        mRulesArray = getResources().getStringArray(R.array.rules_items);
        mRuleText = findViewById(R.id.helpFromBull);
    }

    public void submitBull(View view) {
        mRuleText.setText(mRulesArray[mCnt]);
        mCnt++;
        if (mCnt == mRulesArray.length) {
            mCnt = 0;
        }
    }
}
