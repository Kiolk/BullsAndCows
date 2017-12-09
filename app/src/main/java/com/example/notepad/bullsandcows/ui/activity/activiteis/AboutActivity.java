package com.example.notepad.bullsandcows.ui.activity.activiteis;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.example.notepad.bullsandcows.R;
import com.example.notepad.bullsandcows.data.holders.UserLoginHolder;
import com.example.notepad.bullsandcows.ui.activity.adapters.AboutFragmentPagerAdapter;

public class AboutActivity extends AppCompatActivity {

    ViewPager mAboutViewPager;
    TabLayout mAboutTabLayout;
    AboutFragmentPagerAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        UserLoginHolder.getInstance().setUserOnline();

        initView();

        mAdapter = new AboutFragmentPagerAdapter(this, getSupportFragmentManager());

        mAboutViewPager.setAdapter(mAdapter);
        mAboutTabLayout.setupWithViewPager(mAboutViewPager);
    }

    private void initView() {
        mAboutViewPager = findViewById(R.id.about_page_view_pager);
        mAboutTabLayout = findViewById(R.id.about_page_tab_layout);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        UserLoginHolder.getInstance().keepUserOnline();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UserLoginHolder.getInstance().setOffline();
    }

}
