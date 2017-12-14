package com.example.notepad.bullsandcows.ui.activity.activiteis;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.NotePad.myapplication.backend.VersionOfApp;
import com.example.notepad.bullsandcows.R;
import com.example.notepad.bullsandcows.data.holders.AppInfoHolder;
import com.example.notepad.bullsandcows.data.managers.AppInfoManager;
import com.example.notepad.bullsandcows.utils.AnimationOfView;
import com.example.notepad.bullsandcows.utils.CustomFonts;

public class SplashStartActivity extends Activity {

    public static final int SPLASH_DELAY_MILLIS = 3000;

    private TextView mNameOfGame;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_start);

        TextView mStartText = findViewById(R.id.start_splash_text_view);
        Animation hyperJumpAnimation = AnimationUtils.loadAnimation(SplashStartActivity.this, R.anim.hyperspace_jump);
        mStartText.startAnimation(hyperJumpAnimation);
        mStartText.setTypeface(CustomFonts.getTypeFace(this, CustomFonts.ANNABELLE));
        mNameOfGame = findViewById(R.id.name_of_game_text_view);
        mNameOfGame.setTypeface(CustomFonts.getTypeFace(this, CustomFonts.AASSUANBRK));
        mNameOfGame.post(new Runnable() {

            @Override
            public void run() {
                new AnimationOfView().enteredView(mNameOfGame);
            }
        });

        final LinearLayout layout = findViewById(R.id.image_splash_layout);
        layout.post(new Runnable() {

            @Override
            public void run() {
                new AnimationOfView().enteredView(layout);
            }
        });

        Handler mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {

            @Override
            public void run() {
                Intent intent = new Intent(SplashStartActivity.this, WelcomeActivity.class);
                startActivity(intent);
                finish();
            }
        }, SPLASH_DELAY_MILLIS);
    }
}