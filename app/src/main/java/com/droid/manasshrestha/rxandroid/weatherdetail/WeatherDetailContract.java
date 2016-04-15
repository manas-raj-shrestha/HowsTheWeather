package com.droid.manasshrestha.rxandroid.weatherdetail;


/**
 * Created by ManasShrestha on 3/28/16.
 */
public interface WeatherDetailContract {

    interface Views {
        void showProgressBar();

        void hideProgressBar();

//        void updateWeatherInfos(WeatherModel weatherModel);
    }

    interface UserInteractions {
        void fetchWeather();
    }

}
