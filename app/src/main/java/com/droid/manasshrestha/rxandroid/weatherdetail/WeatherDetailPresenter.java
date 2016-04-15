package com.droid.manasshrestha.rxandroid.weatherdetail;

import android.util.Log;

import com.droid.manasshrestha.rxandroid.retrofit.RetrofitManager;

import rx.Subscriber;

/**
 * Presenter for weather detail
 */
public class WeatherDetailPresenter implements WeatherDetailContract.UserInteractions {

    WeatherDetailContract.Views views;

    public WeatherDetailPresenter(WeatherDetailContract.Views weatherDetailContract){
        views = weatherDetailContract;
    }

    @Override
    public void fetchWeather() {
//        Subscriber subscriber = new Subscriber<ForecastModel>() {
//            @Override
//            public void onCompleted() {
//                Log.e("completed","completed");
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                Log.e("error",e.toString());
//            }
//
//            @Override
//            public void onNext(ForecastModel weatherModel) {
////                views.updateWeatherInfos(weatherModel);
//                Log.e("success","success " + weatherModel.getCity().getName() +weatherModel.getForecastList().size());
//
//            }
//        };

//        RetrofitManager.getInstance().getWeatherForecastByCityName("London, Uk", subscriber);
    }

}
