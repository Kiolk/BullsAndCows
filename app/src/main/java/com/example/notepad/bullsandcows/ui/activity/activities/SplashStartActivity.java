package com.example.notepad.bullsandcows.ui.activity.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.notepad.bullsandcows.R;
import com.example.notepad.bullsandcows.data.holders.UserLoginHolder;
import com.example.notepad.bullsandcows.services.WaiterNewRecordsService;
import com.example.notepad.bullsandcows.utils.CustomFonts;
import com.example.notepad.bullsandcows.utils.animation.SlideAnimationUtil;

import static com.example.notepad.bullsandcows.utils.Constants.IntentKeys.RECORDS_FROM_BACKEND_ON_DAY;
import static com.example.notepad.bullsandcows.utils.Constants.TAG;

public class SplashStartActivity extends Activity {

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_start);

        initView();
    }

    private void prepareService() {
        UserLoginHolder.getInstance().getLastUserVisit(new UserLoginHolder.LastVisitCallback() {

            @Override
            public void getLastVisit(final Long pLastVisit) {
                final Intent intent = new Intent(SplashStartActivity.this, WaiterNewRecordsService.class);
                intent.putExtra(RECORDS_FROM_BACKEND_ON_DAY, pLastVisit);
                Log.d(TAG, "getLastVisit: " + pLastVisit);
                startService(intent);
            }
        });
    }

    protected void checkUserInfo() {
        UserLoginHolder.getInstance().getSavedUserData(this, new UserLoginHolder.checkTokenCallback() {

            @Override
            public void validToken() {
                prepareService();
                final Intent intent = new Intent(SplashStartActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void unValidToken() {
                final Intent intent = new Intent(SplashStartActivity.this, WelcomeActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void initView() {
        final TextView startText = findViewById(R.id.start_splash_text_view);
        startText.setTypeface(CustomFonts.getTypeFace(this, CustomFonts.AASSUANBRK));

        final TextView nameOfGame = findViewById(R.id.name_of_game_text_view);
        nameOfGame.setTypeface(CustomFonts.getTypeFace(this, CustomFonts.AASSUANBRK));

        final ImageView bullImage = findViewById(R.id.bull_splash_image_view);
        final ImageView cowImage = findViewById(R.id.cow_splash_image_view);

        SlideAnimationUtil.slideInFromLeft(this, bullImage, null, SlideAnimationUtil.SLOWLY);
        SlideAnimationUtil.slideInFromRight(this, cowImage, null, SlideAnimationUtil.SLOWLY);
        SlideAnimationUtil.slideInFromTop(this, startText, null, SlideAnimationUtil.SLOWLY);
        SlideAnimationUtil.slideInToTop(this, nameOfGame, new SlideAnimationUtil.SlideAnimationListener() {

            @Override
            public void animationEnd() {
                checkUserInfo();
            }
        }, SlideAnimationUtil.SLOWLY);

    }
}
