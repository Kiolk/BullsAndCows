package com.example.notepad.bullsandcows.Ui.activity.Activiteis;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.example.notepad.bullsandcows.R;
import com.example.notepad.bullsandcows.Ui.activity.Adapters.AboutFragmentPagerAdapter;

public class AboutActivity extends AppCompatActivity {

    ViewPager mAboutViewPager;
    TabLayout mAboutTabLayout;
    AboutFragmentPagerAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        initView();

        mAdapter = new AboutFragmentPagerAdapter(this, getSupportFragmentManager());

        mAboutViewPager.setAdapter(mAdapter);
        mAboutTabLayout.setupWithViewPager(mAboutViewPager);
    }

    private void initView() {
        mAboutViewPager = findViewById(R.id.about_page_view_pager);
        mAboutTabLayout = findViewById(R.id.about_page_tab_layout);
    }
}
