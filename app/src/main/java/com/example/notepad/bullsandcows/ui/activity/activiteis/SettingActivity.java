package com.example.notepad.bullsandcows.ui.activity.activiteis;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.notepad.bullsandcows.R;
import com.example.notepad.bullsandcows.data.holders.UserLoginHolder;
import com.example.notepad.bullsandcows.utils.Constants;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener {

    public static final int MAX_CODED_NUMBER = 10;

    private int mNumberOfDigits;

    private SeekBar mCodsNumberSeekBar;
    private TextView mCodedNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_setting);

        UserLoginHolder.getInstance().setUserOnline();

        getInfoFromIntent();
        initSeekBar();
        initView();
    }

    private void getInfoFromIntent() {
        Intent intent = getIntent();
        mNumberOfDigits = intent.getIntExtra(Constants.CODED_DIGITS, 4);
    }

    private void initView() {
        mCodedNumber = findViewById(R.id.text);
        mCodedNumber.setText(String.valueOf(mCodsNumberSeekBar.getProgress()));
        Button applyButton = findViewById(R.id.apply_all_changes_button);
        applyButton.setOnClickListener(this);
    }

    private void initSeekBar() {
        mCodsNumberSeekBar = findViewById(R.id.coded_number_seek_bar);
        mCodsNumberSeekBar.setMax(MAX_CODED_NUMBER);
        mCodsNumberSeekBar.setProgress(mNumberOfDigits);

        SeekBar.OnSeekBarChangeListener listener = new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mCodedNumber.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        };

        mCodsNumberSeekBar.setOnSeekBarChangeListener(listener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UserLoginHolder.getInstance().setOffline();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        UserLoginHolder.getInstance().setOffline();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.apply_all_changes_button:
                String number = mCodedNumber.getText().toString();

                Intent intent3 = new Intent();
                intent3.putExtra(Constants.CODED_DIGITS, number);
                setResult(RESULT_OK, intent3);

                UserLoginHolder.getInstance().setOffline();
                finish();
                break;
            default:
                break;
        }
    }
}
