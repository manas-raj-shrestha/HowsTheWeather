package com.droid.manasshrestha.rxandroid.weathercards;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.ViewGroup;

import com.droid.manasshrestha.rxandroid.GeneralUtils;
import com.droid.manasshrestha.rxandroid.animatedicons.AnimatedNoConnection;
import com.droid.manasshrestha.rxandroid.data.PrefUtils;
import com.droid.manasshrestha.rxandroid.locationhandlers.GpsInfo;
import com.droid.manasshrestha.rxandroid.locationhandlers.LocationCatcher;
import com.droid.manasshrestha.rxandroid.retrofit.RetrofitManager;
import com.droid.manasshrestha.rxandroid.weathermodels.WeatherModel;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

import rx.functions.Action1;

/**
 * Presenter for {@link WeatherCardsActivity}
 */
public class WeatherCardsActivityPresenter implements WeatherCardsActivityContract {

    public static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 101;

    private static final int ADAPTER_SET_DELAY = 1000;

    private WeatherCardsActivityContract.Views views;
    private LocationCatcher locationCatcher;
    private Context context;

    WeatherCardsActivityPresenter(Context context) {
        this.context = context;
        this.views = (WeatherCardsActivityContract.Views) context;
        locationCatcher = new LocationCatcher(context);
    }

    @Override
    public void startNetworkRequest() {
        if (GeneralUtils.isNetworkOnline(context)) {

            views.showLoadingIcon();

            Action1<ArrayList<WeatherModel>> onNextAction = (weatherModels) -> new Handler().postDelayed(() -> {
                views.setViewPagerData(weatherModels);
                String[] strings = weatherModels.get(0).getTimezone().split("/");
                views.setUserLocation(strings[1].toUpperCase());
            }, ADAPTER_SET_DELAY);

            Action1<Exception> onErrorAction = (exception) -> views.setError(new AnimatedNoConnection(context), "Please Check network connection. \n Double tap to try again.");

            locationCatcher.getLocation(new LocationCatcher.LocationCallBack() {
                @Override
                public void onLocationNotFound() {
                    if (PrefUtils.getLastKnownLatitude() != 0.0) {
                        //use the location from shared preferences if location was not found
                        RetrofitManager.getInstance().getWeatherForecastDaily(new LatLng(PrefUtils.getLastKnownLatitude(),
                                PrefUtils.getLastKnownLongitude()), onNextAction, onErrorAction);
                    } else {
                        locationCatcher.showSettingsAlert();
                    }
                }

                @Override
                public void onLocationFound(GpsInfo location) {
                    if (location != null) {
                        locationCatcher.cancelLocationCallback();
                        RetrofitManager.getInstance().getWeatherForecastDaily(new LatLng(location.getLatitude(),
                                location.getLongitude()), onNextAction, onErrorAction);

                        //save the new location as last known location
                        PrefUtils.setLastKnownLocation(location);
                    }
                }
            });
        } else {
            views.setError(new AnimatedNoConnection(context), "Please Check network connection.\nDouble tap to try again.");
        }
    }

    @Override
    public void checkPermissions() {

        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale((WeatherCardsActivity) context,
                    Manifest.permission.ACCESS_COARSE_LOCATION)) {
                ActivityCompat.requestPermissions((WeatherCardsActivity) context,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            } else {
                ActivityCompat.requestPermissions((WeatherCardsActivity) context,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            }

        } else {
            //App already has permission to access fine location
            startNetworkRequest();
        }
    }

    int clickCount = 0;

    @Override
    public void checkIconClick(ViewGroup viewGroup) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 100ms
                clickCount = 0;
            }
        }, 500);

        if (viewGroup.getChildAt(0) instanceof AnimatedNoConnection) {
            clickCount++;
            if (clickCount == 2) {
                startNetworkRequest();
            }
        }
    }


}
