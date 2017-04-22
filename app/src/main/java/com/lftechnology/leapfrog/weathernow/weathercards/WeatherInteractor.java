package com.lftechnology.leapfrog.weathernow.weathercards;

import com.google.android.gms.maps.model.LatLng;
import com.lftechnology.leapfrog.weathernow.weathermodels.WeatherModel;

import rx.Observable;
import rx.functions.Action1;

public interface WeatherInteractor {
     Observable<WeatherModel> getWeatherForecastDaily(LatLng latLng, Action1 onNextAction, Action1 onError);
}
