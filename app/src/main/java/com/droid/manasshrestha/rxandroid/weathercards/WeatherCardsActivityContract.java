package com.droid.manasshrestha.rxandroid.weathercards;

import com.droid.manasshrestha.rxandroid.weathermodels.WeatherModel;

import java.util.ArrayList;

/**
 * Contract between {@link WeatherCardsActivityPresenter} and {@link WeatherCardsActivity}
 */
public interface WeatherCardsActivityContract {

    void startNetworkRequest();

    void checkPermissions();

    interface Views {
        void setViewPagerData(ArrayList<WeatherModel> weatherModels);

        void setUserLocation(String cityName);
    }

}
