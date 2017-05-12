package com.lftechnology.leapfrog.weathernow.weathercards;

import com.google.android.gms.maps.model.LatLng;
import com.lftechnology.leapfrog.weathernow.base.interactor.BaseInteractor;
import com.lftechnology.leapfrog.weathernow.weathermodels.WeatherModel;

import java.util.ArrayList;
import java.util.Calendar;

import rx.Observable;
import rx.functions.Action1;

public class WeatherInteractorImpl extends BaseInteractor implements WeatherInteractor {

    private final static String API_KEY = "6bf45156088e2afa94ad4bd23f793252";
    private final static String UNIT = "si";
    private final static int DAY_INCREMENT = 1;
    private final static long UNIX_TIME_CONVERSION_CONSTANT = 1000L;

    @Override
    public Observable<WeatherModel> getWeatherForecastDaily(LatLng latLng, Action1 onNextAction, Action1 onError) {
        Calendar calendar = Calendar.getInstance();

        Observable<WeatherModel> dayOneObservable = getWeatherObservable(latLng, calendar);
        Observable<WeatherModel> dayTwoObservable = getWeatherObservable(latLng, calendar);
        Observable<WeatherModel> dayThreeObservable = getWeatherObservable(latLng, calendar);
        Observable<WeatherModel> dayFourObservable = getWeatherObservable(latLng, calendar);
        Observable<WeatherModel> dayFiveObservable = getWeatherObservable(latLng, calendar);
        Observable<WeatherModel> daySixObservable = getWeatherObservable(latLng, calendar);
        Observable<WeatherModel> daySevenObservable = getWeatherObservable(latLng, calendar);

        Observable zippedObservable = Observable.zip(dayOneObservable, dayTwoObservable, dayThreeObservable, dayFourObservable,
                dayFiveObservable, daySixObservable, daySevenObservable, (dayOneModel, dayTwoModel,
                                                                          dayThreeModel, dayFourModel,
                                                                          dayFiveModel, daySixModel,
                                                                          daySevenModel) -> {

                    ArrayList<WeatherModel> weatherModels = new ArrayList<>();
                    weatherModels.add(dayOneModel);
                    weatherModels.add(dayTwoModel);
                    weatherModels.add(dayThreeModel);
                    weatherModels.add(dayFourModel);
                    weatherModels.add(dayFiveModel);
                    weatherModels.add(daySixModel);
                    weatherModels.add(daySevenModel);

                    return weatherModels;
                });

        return zippedObservable;
    }

    private Observable<WeatherModel> getWeatherObservable(LatLng latLng, Calendar calendar) {
        Observable<WeatherModel> weatherObservable = retrofitServices.getWeatherDaily(API_KEY,
                String.valueOf(latLng.latitude) + "," + String.valueOf(latLng.longitude) + ","
                        + calendar.getTimeInMillis() / UNIX_TIME_CONVERSION_CONSTANT, UNIT);

        calendar.add(Calendar.DATE, DAY_INCREMENT);

        return weatherObservable;
    }
}
