package com.droid.manasshrestha.rxandroid.weathercards;

import android.os.Handler;
import android.util.Log;

import com.droid.manasshrestha.rxandroid.retrofit.RetrofitManager;
import com.droid.manasshrestha.rxandroid.weathermodels.WeatherModel;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

import rx.functions.Action1;

/**
 * Presenter for {@link WeatherCardsActivity}
 */
public class WeatherCardsActivityPresenter implements WeatherCardsActivityContract {

    private static final int ADAPTER_SET_DELAY = 1000;

    private WeatherCardsActivityContract.Views views;

    WeatherCardsActivityPresenter(WeatherCardsActivityContract.Views views) {
        this.views = views;
    }

    @Override
    public void startNetworkRequest() {

        Action1<ArrayList<WeatherModel>> onNextAction = (weatherModels) -> new Handler().postDelayed(() -> {
            views.setViewPagerData(weatherModels);
            String[] strings = weatherModels.get(0).getTimezone().split("/");
            views.setUserLocation(strings[1].toUpperCase());
        }, ADAPTER_SET_DELAY);

        Action1<Exception> onErrorAction = (exception) -> Log.e("Exception :: ", exception.toString());

        //TODO remove this line
        RetrofitManager.getInstance().getWeatherForecastDaily(new LatLng(27.712228, 85.324416), onNextAction, onErrorAction);
    }

}
