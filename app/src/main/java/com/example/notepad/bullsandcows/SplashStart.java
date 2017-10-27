package com.example.notepad.bullsandcows;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.widget.ProgressBar;

public class SplashStart extends Activity {

    Handler mHandler;
    ProgressBar mProgressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_start);
        mProgressBar = (ProgressBar) findViewById(R.id.splash_progress_bar);

        mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {

            @Override
            public void run() {
                Intent intent = new Intent(SplashStart.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 3000);
    }
}
