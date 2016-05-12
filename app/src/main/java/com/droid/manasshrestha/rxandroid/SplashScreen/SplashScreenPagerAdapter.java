package com.droid.manasshrestha.rxandroid.splashscreen;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Manas on 5/11/2016.
 */
public class SplashScreenPagerAdapter extends FragmentPagerAdapter {

    public SplashScreenPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return SwipeTutorialFragment.getInstance(position);
            case 1:
                return TapTutorialFragment.getInstance(position);
            case 2:
                return PermissionRequestFragment.getInstance(position);
        }
        return SwipeTutorialFragment.getInstance(position);
    }

    @Override
    public int getCount() {
        return 3;
    }

}
