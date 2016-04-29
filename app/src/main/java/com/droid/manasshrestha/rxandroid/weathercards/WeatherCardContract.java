package com.droid.manasshrestha.rxandroid.weathercards;

import android.graphics.drawable.Drawable;
import android.view.ViewGroup;


import com.droid.manasshrestha.rxandroid.weathermodels.Temp;

import lecho.lib.hellocharts.model.LineChartData;

/**
 * Created by ManasShrestha on 3/29/16.
 */
public interface WeatherCardContract {

    interface Views {
        void setWeekDay(String weekDay);

        void setWeatherIcon(ViewGroup viewGroup);

        void setHumidity(int humidity);

        void setAvgTemp(int temp);

        void setClouds(int clouds);

        void setCardBackground(int colorId,int drawableId);

        void setAtmosphericPressure(String pressure);

        void setWindSpeed(String windSpeed);

        void setWindDirection(String windDirection);

        void setTemperature(Temp temperature);

        void setWeatherDesc(String desc);

        void setLineSetData(LineChartData lineChartData);

        void setWeatherTicker(String weatherTicker);
    }

}
