package com.example.notepad.bullsandcows.ui.activity.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.notepad.bullsandcows.R;
import com.example.notepad.bullsandcows.ui.activity.fragments.AboutAppFragment;
import com.example.notepad.bullsandcows.ui.activity.fragments.AboutDeveloperFragment;

public class AboutFragmentPagerAdapter extends FragmentPagerAdapter {

    private static final int ABOUT_APP_PAGE = 0;
    private static final int ABOUT_DEVELOPER_PAGE = 1;
    private static final int TOTAL_NUMBER_OF_FRAGMENTS = 2;

    private Context mContext;

    public AboutFragmentPagerAdapter(Context pContext, FragmentManager pFm) {
        super(pFm);
        mContext = pContext;
    }

    @Override
    public Fragment getItem(int pPosition) {
        switch (pPosition) {
            case ABOUT_APP_PAGE:

                return new AboutAppFragment();
            case ABOUT_DEVELOPER_PAGE:

                return new AboutDeveloperFragment();
            default:

                return null;
        }
    }

    @Override
    public int getCount() {
        return TOTAL_NUMBER_OF_FRAGMENTS;
    }

    @Override
    public CharSequence getPageTitle(int pPosition) {
        switch (pPosition) {
            case ABOUT_APP_PAGE:

                return mContext.getResources().getString(R.string.ABOUT_APP);
            case ABOUT_DEVELOPER_PAGE:

                return mContext.getResources().getString(R.string.ABOUT_DEVELOPER);
            default:
                return null;
        }
    }
}
