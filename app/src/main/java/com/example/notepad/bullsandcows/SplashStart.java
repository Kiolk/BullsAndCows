package com.example.notepad.bullsandcows;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.notepad.bullsandcows.ui.activity.activiteis.WelcomeActivity;
import com.example.notepad.bullsandcows.utils.AnimationOfView;
import com.example.notepad.bullsandcows.utils.CustomFonts;

public class SplashStart extends Activity {

    TextView mStartText;
    TextView mNameOfGame;
    String mLanguageLocale = "be";
    Handler mHandler;

    ProgressBar mProgressBar;

    SharedPreferences mSplashStartPreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_start);
//        loadPreferences();
        mStartText = (TextView) findViewById(R.id.start_splash_text_view);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/annabelle.ttf");
        Animation hyperJumpAnimation = AnimationUtils.loadAnimation(SplashStart.this, R.anim.hyperspace_jump);
        mStartText.startAnimation(hyperJumpAnimation);
        mStartText.setTypeface(typeface);
        mNameOfGame = findViewById(R.id.name_of_game_text_view);
        mNameOfGame.setTypeface(CustomFonts.getTypeFace(this, CustomFonts.AASSUANBRK));
        mNameOfGame.post(new Runnable() {

            @Override
            public void run() {
                new AnimationOfView().enteredView(mNameOfGame);
            }
        });
        mProgressBar = (ProgressBar) findViewById(R.id.splash_progress_bar);
        final LinearLayout layout = (LinearLayout) findViewById(R.id.image_splash_layout);
        layout.post(new Runnable() {

            @Override
            public void run() {
                new AnimationOfView().enteredView(layout);
            }
        });
//     new AnimationOfView().enteredView( layout);
        mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {

            @Override
            public void run() {
                Intent intent = new Intent(SplashStart.this, WelcomeActivity.class);
                startActivity(intent);
                finish();
            }
        }, 3000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        savePreferences(mLanguageLocale);
    }

//    public void savePreferences(String pLanguage){
//        mSplashStartPreferences = getPreferences(MODE_PRIVATE);
//        SharedPreferences.Editor  editor = mSplashStartPreferences.edit();
//        editor.putString(Constants.CODE_OF_LANGUAGE, pLanguage);
//        editor.commit();
//    }
//
//    public void loadPreferences (){
//        mSplashStartPreferences = getPreferences(MODE_PRIVATE);
//        mLanguageLocale = mSplashStartPreferences.getString(Constants.CODE_OF_LANGUAGE, "en");
//        mLanguageLocale = "be";
//        new LanguageLocale().setLocale(mLanguageLocale, SplashStart.this);
//    }

}
