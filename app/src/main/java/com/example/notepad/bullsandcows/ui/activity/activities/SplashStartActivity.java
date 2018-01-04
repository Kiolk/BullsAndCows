package com.example.notepad.bullsandcows.ui.activity.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.notepad.bullsandcows.R;
import com.example.notepad.bullsandcows.data.holders.UserLoginHolder;
import com.example.notepad.bullsandcows.services.WaiterNewRecordsService;
import com.example.notepad.bullsandcows.utils.CustomFonts;
import com.example.notepad.bullsandcows.utils.animation.AnimationOfView;

import static com.example.notepad.bullsandcows.utils.Constants.DURATION_OF_ANIMATION;
import static com.example.notepad.bullsandcows.utils.Constants.IntentKeys.RECORDS_FROM_BACKEND_ON_DAY;
import static com.example.notepad.bullsandcows.utils.Constants.TAG;

public class SplashStartActivity extends Activity {

    private TextView mNameOfGame;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_start);

        initView();
        timeOut();
        prepareService();
    }

    private void prepareService() {
        UserLoginHolder.getInstance().getLastUserVisit(new UserLoginHolder.LastVisitCallback() {
            @Override
            public void getLastVisit(Long pLastVisit) {
                Intent intent = new Intent(SplashStartActivity.this, WaiterNewRecordsService.class);
                intent.putExtra(RECORDS_FROM_BACKEND_ON_DAY, pLastVisit);
                Log.d(TAG, "getLastVisit: " + pLastVisit);
                startService(intent);
            }
        });
    }

    private void timeOut() {
        Handler mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {

            @Override
            public void run() {
                UserLoginHolder.getInstance().getSavedUserData(SplashStartActivity.this, new UserLoginHolder.checkTokenCallback() {
                    @Override
                    public void isValidToken(boolean isValid) {
                        if (isValid) {
                            Intent intent = new Intent(SplashStartActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Intent intent = new Intent(SplashStartActivity.this, WelcomeActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
            }
        }, DURATION_OF_ANIMATION);
    }

    private void initView() {
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
    }
}
