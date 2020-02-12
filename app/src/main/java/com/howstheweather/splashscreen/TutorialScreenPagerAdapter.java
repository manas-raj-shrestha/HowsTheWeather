package com.howstheweather.splashscreen;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Pager adapter for tutorial screen
 */
public class TutorialScreenPagerAdapter extends FragmentPagerAdapter {

    private static final int POSITION_SWIPE_TUTORIAL = 0;
    private static final int POSITION_TAP_TUTORIAL = 1;
    private static final int POSITION_PERMISSION_PAGE = 2;
    private static final int TOTAL_TUTORIAL_PAGES = 3;

    public TutorialScreenPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case POSITION_SWIPE_TUTORIAL:
                fragment = SwipeTutorialFragment.getInstance(position);
                break;
            case POSITION_TAP_TUTORIAL:
                fragment = TapTutorialFragment.getInstance(position);
                break;
            case POSITION_PERMISSION_PAGE:
                fragment = PermissionRequestFragment.getInstance(position);
                break;
        }

        return fragment;
    }

    @Override
    public int getCount() {
        return TOTAL_TUTORIAL_PAGES;
    }

}
