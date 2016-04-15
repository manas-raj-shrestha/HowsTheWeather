package com.droid.manasshrestha.rxandroid.retrofit;

import android.util.Log;

import com.droid.manasshrestha.rxandroid.weathermodels.WeatherModel;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Calendar;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func7;
import rx.schedulers.Schedulers;

/**
 * Class for managing the retrofit object
 * Manages the network calls
 */
public class RetrofitManager {
    //    private final static String BASE_URL = "http://api.openweathermap.org/data/2.5/";
    private final static String BASE_URL = "https://api.forecast.io/";
    private final static String UNIT = "metric";
    private final static String APP_ID = "c18f5a141f0911f9867030a2d9ee1ea3";

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

//    public void getWeatherForecastByCityName(LatLng latLng, Subscriber subscriber) {
//        Observable<ForecastModel> observable = service.getWeatherForecastObs(String.valueOf(latLng.latitude),
//                String.valueOf(latLng.longitude),
//                APP_ID, UNIT);
//
//        Subscription subscription = observable.subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .unsubscribeOn(Schedulers.io())
//                .subscribe(subscriber);
//    }

    public void getWeatherForecastDaily(LatLng latLng, Subscriber subscriber) {
        Calendar c = Calendar.getInstance();

        Observable<WeatherModel> observable = service.getWeatherDaily("6bf45156088e2afa94ad4bd23f793252",
                String.valueOf(latLng.latitude) + "," + String.valueOf(latLng.longitude) + ","
                        + c.getTimeInMillis() / 1000L,"si");

        c.add(Calendar.DATE, 1);
        Observable<WeatherModel> observable2 = service.getWeatherDaily("6bf45156088e2afa94ad4bd23f793252",
                String.valueOf(latLng.latitude) + "," + String.valueOf(latLng.longitude) + ","
                        + c.getTimeInMillis() / 1000L,"si");

        c.add(Calendar.DATE, 1);
        Observable<WeatherModel> observable3 = service.getWeatherDaily("6bf45156088e2afa94ad4bd23f793252",
                String.valueOf(latLng.latitude) + "," + String.valueOf(latLng.longitude) + ","
                        + c.getTimeInMillis() / 1000L,"si");

        c.add(Calendar.DATE, 1);
        Observable<WeatherModel> observable4 = service.getWeatherDaily("6bf45156088e2afa94ad4bd23f793252",
                String.valueOf(latLng.latitude) + "," + String.valueOf(latLng.longitude) + ","
                        + c.getTimeInMillis()/1000L,"si");

        c.add(Calendar.DATE, 1);
        Observable<WeatherModel> observable5 = service.getWeatherDaily("6bf45156088e2afa94ad4bd23f793252",
                String.valueOf(latLng.latitude) + "," + String.valueOf(latLng.longitude) + ","
                        + c.getTimeInMillis()/1000L,"si");

        c.add(Calendar.DATE, 1);
        Observable<WeatherModel> observable6 = service.getWeatherDaily("6bf45156088e2afa94ad4bd23f793252",
                String.valueOf(latLng.latitude) + "," + String.valueOf(latLng.longitude) + ","
                        + c.getTimeInMillis()/1000L,"si");

        c.add(Calendar.DATE, 1);
        Observable<WeatherModel> observable7 = service.getWeatherDaily("6bf45156088e2afa94ad4bd23f793252",
                String.valueOf(latLng.latitude) + "," + String.valueOf(latLng.longitude) + ","
                        + c.getTimeInMillis()/1000L,"si");

        Observable observable1 = Observable.zip(observable, observable2, observable3, observable4,
                observable5,observable6,observable7,
                new Func7<WeatherModel, WeatherModel,WeatherModel,WeatherModel,WeatherModel,WeatherModel,WeatherModel, ArrayList<WeatherModel>>() {

                    @Override
                    public ArrayList<WeatherModel> call(WeatherModel weath, WeatherModel weath2, WeatherModel weath3, WeatherModel weath4, WeatherModel weath5, WeatherModel weath6, WeatherModel weath7) {
                        Log.e("weath1"," "+weath.getDaily().getData().get(0).getCloudCover());
                        Log.e("weath2"," "+weath2.getDaily().getData().get(0).getCloudCover());
                        Log.e("weath3"," "+weath3.getDaily().getData().get(0).getCloudCover());
                        Log.e("weath4"," "+weath4.getDaily().getData().get(0).getCloudCover());
                        Log.e("weath5"," "+weath5.getDaily().getData().get(0).getCloudCover());
                        Log.e("weath6"," "+weath6.getDaily().getData().get(0).getCloudCover());
                        Log.e("weath7"," "+weath7.getDaily().getData().get(0).getCloudCover());

                        ArrayList<WeatherModel> weatherModels = new ArrayList<WeatherModel>();
                        weatherModels.add(weath);
                        weatherModels.add(weath2);
                        weatherModels.add(weath3);
                        weatherModels.add(weath4);
                        weatherModels.add(weath5);
                        weatherModels.add(weath6);
                        weatherModels.add(weath7);

                        return weatherModels;
                    }
        });

        Subscription subscription = observable1.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(subscriber);
    }


}
