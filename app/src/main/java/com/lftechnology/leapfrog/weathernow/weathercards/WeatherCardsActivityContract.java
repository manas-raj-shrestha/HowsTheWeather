package com.lftechnology.leapfrog.weathernow.weathercards;

import android.view.View;
import android.view.ViewGroup;

import com.lftechnology.leapfrog.weathernow.weathermodels.WeatherModel;

import java.util.ArrayList;

/**
 * Contract between {@link WeatherCardsActivityPresenter} and {@link WeatherCardsActivity}
 */
public interface WeatherCardsActivityContract {

    void startNetworkRequest();

    void checkPermissions();

    void checkIconClick(ViewGroup viewGroup);

    interface Views {
        void setViewPagerData(ArrayList<WeatherModel> weatherModels);

        void setUserLocation(String cityName);

        void setError(View errorView, String error);

        void showLoadingIcon();
    }

}
