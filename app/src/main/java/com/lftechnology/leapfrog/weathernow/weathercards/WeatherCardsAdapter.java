package com.lftechnology.leapfrog.weathernow.weathercards;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.lftechnology.leapfrog.weathernow.weathermodels.WeatherModel;

import java.util.ArrayList;

/**
 * pager adapter for weather cards in view pager
 */
public class WeatherCardsAdapter extends FragmentStatePagerAdapter {

    private final static int WEEK_DAYS_COUNT = 7;
    private FragmentManager fragmentManager;
    private ArrayList<WeatherModel> forecastLists = new ArrayList<>();

    public WeatherCardsAdapter(FragmentManager fm, ArrayList<WeatherModel> weatherModels) {
        super(fm);
        this.fragmentManager = fm;
        forecastLists.clear();
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

    public ArrayList<WeatherModel> getForecastLists() {
        return forecastLists;
    }

    public void setForecastLists(ArrayList<WeatherModel> weatherModelArrayList) {
        this.forecastLists = weatherModelArrayList;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
