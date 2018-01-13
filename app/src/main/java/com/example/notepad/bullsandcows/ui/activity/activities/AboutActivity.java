package com.example.notepad.bullsandcows.ui.activity.activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.example.notepad.bullsandcows.R;
import com.example.notepad.bullsandcows.ui.activity.adapters.AboutFragmentPagerAdapter;

public class AboutActivity extends AppCompatActivity {

    private ViewPager mAboutViewPager;
    private TabLayout mAboutTabLayout;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        initView();
        initAdapter();
    }

    private void initAdapter() {
        final AboutFragmentPagerAdapter adapter = new AboutFragmentPagerAdapter(this, getSupportFragmentManager());
        mAboutViewPager.setAdapter(adapter);
        mAboutTabLayout.setupWithViewPager(mAboutViewPager);
    }

    private void initView() {
        mAboutViewPager = findViewById(R.id.about_page_view_pager);
        mAboutTabLayout = findViewById(R.id.about_page_tab_layout);
    }
}
