package com.lftechnology.leapfrog.weathernow.update;

import com.lftechnology.leapfrog.weathernow.weathermodels.WeatherModel;

import java.util.ArrayList;

/**
 * Created by Manas on 6/30/2016.
 */
public interface UpdateFetcher {
    void getUpdatedWeatherModel(ArrayList<WeatherModel> weatherModelArrayList);
}
