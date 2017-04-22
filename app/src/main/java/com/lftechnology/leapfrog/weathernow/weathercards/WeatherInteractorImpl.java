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
        Calendar c = Calendar.getInstance();
//
        Observable<WeatherModel> observable = retrofitServices.getWeatherDaily(API_KEY,
                String.valueOf(latLng.latitude) + "," + String.valueOf(latLng.longitude) + ","
                        + c.getTimeInMillis() / UNIX_TIME_CONVERSION_CONSTANT, UNIT);

        c.add(Calendar.DATE, DAY_INCREMENT);
        Observable<WeatherModel> observable2 = retrofitServices.getWeatherDaily(API_KEY,
                String.valueOf(latLng.latitude) + "," + String.valueOf(latLng.longitude) + ","
                        + c.getTimeInMillis() / UNIX_TIME_CONVERSION_CONSTANT, UNIT);

        c.add(Calendar.DATE, DAY_INCREMENT);
        Observable<WeatherModel> observable3 = retrofitServices.getWeatherDaily(API_KEY,
                String.valueOf(latLng.latitude) + "," + String.valueOf(latLng.longitude) + ","
                        + c.getTimeInMillis() / UNIX_TIME_CONVERSION_CONSTANT, UNIT);

        c.add(Calendar.DATE, DAY_INCREMENT);
        Observable<WeatherModel> observable4 = retrofitServices.getWeatherDaily(API_KEY,
                String.valueOf(latLng.latitude) + "," + String.valueOf(latLng.longitude) + ","
                        + c.getTimeInMillis() / UNIX_TIME_CONVERSION_CONSTANT, UNIT);

        c.add(Calendar.DATE, DAY_INCREMENT);
        Observable<WeatherModel> observable5 = retrofitServices.getWeatherDaily(API_KEY,
                String.valueOf(latLng.latitude) + "," + String.valueOf(latLng.longitude) + ","
                        + c.getTimeInMillis() / UNIX_TIME_CONVERSION_CONSTANT, UNIT);

        c.add(Calendar.DATE, DAY_INCREMENT);
        Observable<WeatherModel> observable6 = retrofitServices.getWeatherDaily(API_KEY,
                String.valueOf(latLng.latitude) + "," + String.valueOf(latLng.longitude) + ","
                        + c.getTimeInMillis() / UNIX_TIME_CONVERSION_CONSTANT, UNIT);

        c.add(Calendar.DATE, DAY_INCREMENT);
        Observable<WeatherModel> observable7 = retrofitServices.getWeatherDaily(API_KEY,
                String.valueOf(latLng.latitude) + "," + String.valueOf(latLng.longitude) + ","
                        + c.getTimeInMillis() / UNIX_TIME_CONVERSION_CONSTANT, UNIT);

        Observable observable1 = Observable.zip(observable, observable2, observable3, observable4,
                observable5, observable6, observable7, (weatherModel, weatherModel2,
                                                        weatherModel3, weatherModel4,
                                                        weatherModel5, weatherModel6,
                                                        weatherModel7) -> {

                    ArrayList<WeatherModel> weatherModels = new ArrayList<>();
                    weatherModels.add(weatherModel);
                    weatherModels.add(weatherModel2);
                    weatherModels.add(weatherModel3);
                    weatherModels.add(weatherModel4);
                    weatherModels.add(weatherModel5);
                    weatherModels.add(weatherModel6);
                    weatherModels.add(weatherModel7);

                    return weatherModels;
                });

        return observable1;
    }
}
