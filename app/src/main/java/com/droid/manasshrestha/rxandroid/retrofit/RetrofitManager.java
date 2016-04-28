package com.droid.manasshrestha.rxandroid.retrofit;

import com.droid.manasshrestha.rxandroid.weathermodels.WeatherModel;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Calendar;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Class for managing the retrofit object
 * Manages the network calls
 */
public class RetrofitManager {

    private final static String BASE_URL = "https://api.forecast.io/";
    private final static String API_KEY = "6bf45156088e2afa94ad4bd23f793252";
    private final static String UNIT = "si";
    private final static int DAY_INCREMENT = 1;
    private final static long UNIX_TIME_CONVERSION_CONSTANT = 1000L;

    private static RetrofitManager retrofitManager;
    private static Retrofit retrofit;
    private static RestApi service;

    public static RetrofitManager getInstance() {
        if (retrofitManager == null)
            retrofitManager = new RetrofitManager();

        return retrofitManager;
    }

    private RetrofitManager() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        service = retrofit.create(RestApi.class);
    }

    /**
     * fetches json from seven api ends and zips it into a single array list
     *
     * @param latLng       latitude and longitude of user
     * @param onNextAction action to be performed in subscribers onNext
     * @param onError      action to be performed in subscribers onNext
     */
    public void getWeatherForecastDaily(LatLng latLng, Action1 onNextAction, Action1 onError) {
        Calendar c = Calendar.getInstance();

        Observable<WeatherModel> observable = service.getWeatherDaily(API_KEY,
                String.valueOf(latLng.latitude) + "," + String.valueOf(latLng.longitude) + ","
                        + c.getTimeInMillis() / UNIX_TIME_CONVERSION_CONSTANT, UNIT);

        c.add(Calendar.DATE, DAY_INCREMENT);
        Observable<WeatherModel> observable2 = service.getWeatherDaily(API_KEY,
                String.valueOf(latLng.latitude) + "," + String.valueOf(latLng.longitude) + ","
                        + c.getTimeInMillis() / UNIX_TIME_CONVERSION_CONSTANT, UNIT);

        c.add(Calendar.DATE, DAY_INCREMENT);
        Observable<WeatherModel> observable3 = service.getWeatherDaily(API_KEY,
                String.valueOf(latLng.latitude) + "," + String.valueOf(latLng.longitude) + ","
                        + c.getTimeInMillis() / UNIX_TIME_CONVERSION_CONSTANT, UNIT);

        c.add(Calendar.DATE, DAY_INCREMENT);
        Observable<WeatherModel> observable4 = service.getWeatherDaily(API_KEY,
                String.valueOf(latLng.latitude) + "," + String.valueOf(latLng.longitude) + ","
                        + c.getTimeInMillis() / UNIX_TIME_CONVERSION_CONSTANT, UNIT);

        c.add(Calendar.DATE, DAY_INCREMENT);
        Observable<WeatherModel> observable5 = service.getWeatherDaily(API_KEY,
                String.valueOf(latLng.latitude) + "," + String.valueOf(latLng.longitude) + ","
                        + c.getTimeInMillis() / UNIX_TIME_CONVERSION_CONSTANT, UNIT);

        c.add(Calendar.DATE, DAY_INCREMENT);
        Observable<WeatherModel> observable6 = service.getWeatherDaily(API_KEY,
                String.valueOf(latLng.latitude) + "," + String.valueOf(latLng.longitude) + ","
                        + c.getTimeInMillis() / UNIX_TIME_CONVERSION_CONSTANT, UNIT);

        c.add(Calendar.DATE, DAY_INCREMENT);
        Observable<WeatherModel> observable7 = service.getWeatherDaily(API_KEY,
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

        observable1.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(onNextAction, onError);
    }


}
