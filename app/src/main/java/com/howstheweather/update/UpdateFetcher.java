package com.howstheweather.update;

import com.howstheweather.weathermodels.WeatherModel;

import java.util.ArrayList;

/**
 * Created by Manas on 6/30/2016.
 */
public interface UpdateFetcher {
    void getUpdatedWeatherModel(ArrayList<WeatherModel> weatherModelArrayList);
}
