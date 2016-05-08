package com.droid.manasshrestha.rxandroid.weathercards;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.droid.manasshrestha.rxandroid.weathermodels.WeatherModel;

import java.util.ArrayList;

/**
 * pager adapter for weather cards in view pager
 */
public class WeatherCardsAdapter extends FragmentPagerAdapter {

    private final static int WEEK_DAYS_COUNT = 7;
    private FragmentManager fragmentManager;
    private ArrayList<WeatherModel> forecastLists;

    public WeatherCardsAdapter(FragmentManager fm, ArrayList<WeatherModel> weatherModels) {
        super(fm);
        this.fragmentManager = fm;
        this.forecastLists = weatherModels;
    }

    @Override
    public Fragment getItem(int position) {
        return WeatherCardsFragment.newInstance(forecastLists.get(position));
    }

    @Override
    public int getCount() {
        return WEEK_DAYS_COUNT;
    }
}
