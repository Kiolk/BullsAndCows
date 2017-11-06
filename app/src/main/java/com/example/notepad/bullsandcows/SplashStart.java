package com.example.notepad.bullsandcows;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.notepad.bullsandcows.utils.CustomFonts;

public class SplashStart extends Activity {

    TextView mStartText;

    TextView mNameOfGame;

    Handler mHandler;
    ProgressBar mProgressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_start);
        mStartText = (TextView) findViewById(R.id.start_splash_text_view);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/annabelle.ttf");
        mStartText.setTypeface(typeface);
        mNameOfGame = findViewById(R.id.name_of_game_text_view);
        mNameOfGame.setTypeface(CustomFonts.getTypeFace(this, CustomFonts.AASSUANBRK));
        mProgressBar = (ProgressBar) findViewById(R.id.splash_progress_bar);

        mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {

            @Override
            public void run() {
                Intent intent = new Intent(SplashStart.this, Welcome.class);
                startActivity(intent);
                finish();
            }
        }, 3000);
    }
}
