package com.lftechnology.leapfrog.weathernow.weathercards;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.ViewGroup;

import com.google.android.gms.maps.model.LatLng;
import com.lftechnology.leapfrog.weathernow.animatedicons.NoConnectionView;
import com.lftechnology.leapfrog.weathernow.base.presenter.BasePresenter;
import com.lftechnology.leapfrog.weathernow.base.view.BaseView;
import com.lftechnology.leapfrog.weathernow.data.PrefUtils;
import com.lftechnology.leapfrog.weathernow.location.GpsInfo;
import com.lftechnology.leapfrog.weathernow.location.LocationCatcher;
import com.lftechnology.leapfrog.weathernow.update.UpdateService;
import com.lftechnology.leapfrog.weathernow.utils.GeneralUtils;
import com.lftechnology.leapfrog.weathernow.weathercards.contracts.WeatherCardsActivityContract;
import com.lftechnology.leapfrog.weathernow.weathermodels.WeatherModel;
import com.lftechnology.leapfrog.weathernow.widget.UpdateWidget;

import java.util.ArrayList;
import java.util.Calendar;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Presenter for {@link WeatherCardsActivity}
 */
public class WeatherCardsActivityPresenter extends BasePresenter<BaseView> implements WeatherCardsActivityContract {

    @Inject
    WeatherInteractor weatherInteractor;

    public static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 101;

    private static final int ADAPTER_SET_DELAY = 1000;

    private WeatherCardsActivityContract.Views views;
    private LocationCatcher locationCatcher;
    private Context context;

    WeatherCardsActivityPresenter(Context context) {
        this.context = context;
        this.views = (WeatherCardsActivityContract.Views) context;
        locationCatcher = new LocationCatcher(context);
        PrefUtils.setFirstRun(false);
    }

    @Override
    public void startNetworkRequest() {
        if (PrefUtils.getWeatherCache() != null) {
            views.setViewPagerData(PrefUtils.getWeatherCache());
            String[] strings = PrefUtils.getWeatherCache().get(0).getTimezone().split("/");
            views.setUserLocation(strings[1].toUpperCase());
        } else if (GeneralUtils.isNetworkOnline(context)) {

            views.showLoadingIcon();

            Action1<ArrayList<WeatherModel>> onNextAction = (weatherModels) -> new Handler().postDelayed(() -> {
                views.setViewPagerData(weatherModels);
                String[] strings = weatherModels.get(0).getTimezone().split("/");
                views.setUserLocation(strings[1].toUpperCase());
                PrefUtils.setWeatherCache(weatherModels);

                Intent updateWidgetIntent = new Intent(context, UpdateWidget.class);
                context.startService(updateWidgetIntent);
                createAlarm(context);

            }, ADAPTER_SET_DELAY);

            Action1<Exception> onErrorAction = (exception) -> views.setError(new NoConnectionView(context), "Please Check network connection. \n Double tap to try again.");

            locationCatcher.getLocation(new LocationCatcher.LocationCallBack() {
                @Override
                public void onLocationNotFound() {
                    if (PrefUtils.getLastKnownLatitude() != 0.0) {

                        //use the location from shared preferences if location was not found
//                        weatherInteractor.getWeatherForecastDaily(new LatLng(PrefUtils.getLastKnownLatitude(),
//                                PrefUtils.getLastKnownLongitude()), onNextAction, onErrorAction);

//                        observable1.subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .unsubscribeOn(Schedulers.io())
//                .subscribe(onNextAction, onError);

                        addSubscription(weatherInteractor.getWeatherForecastDaily(new LatLng(PrefUtils.getLastKnownLatitude(),
                                PrefUtils.getLastKnownLongitude()), onNextAction, onErrorAction).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe());


                    } else {
                        locationCatcher.showSettingsAlert();
                    }
                }

                @Override
                public void onLocationFound(GpsInfo location) {
                    if (location != null) {
                        locationCatcher.cancelLocationCallback();

//                        weatherInteractor.getWeatherForecastDaily(new LatLng(location.getLatitude(),
//                                location.getLongitude()), onNextAction, onErrorAction);

                        addSubscription(weatherInteractor.getWeatherForecastDaily(new LatLng(location.getLatitude(),
                                location.getLongitude()), onNextAction, onErrorAction).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe());

                        //save the new location as last known location
                        PrefUtils.setLastKnownLocation(location);
                    }
                }
            });
        } else {
            views.setError(new NoConnectionView(context), "Please Check network connection.\nDouble tap to try again.");
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
        new Handler().postDelayed(() -> {
            //Do something after 100ms
            clickCount = 0;
        }, 500);

        if (viewGroup.getChildAt(0) instanceof NoConnectionView) {
            clickCount++;
            if (clickCount == 2) {
                startNetworkRequest();
            }
        }
    }

    private void createAlarm(Context context) {

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
        Intent intent = new Intent(context, UpdateService.class);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.DATE, 1);

        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 6, 0, 0);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }


}
