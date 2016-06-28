package com.droid.manasshrestha.rxandroid.update;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import com.droid.manasshrestha.rxandroid.data.PrefUtils;
import com.droid.manasshrestha.rxandroid.retrofit.RetrofitManager;
import com.droid.manasshrestha.rxandroid.weathermodels.WeatherModel;
import com.droid.manasshrestha.rxandroid.widget.UpdateWidget;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

import rx.functions.Action1;

/**
 * uses retrofit to update the cache weather information
 */
public class UpdateService extends IntentService {

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public UpdateService() {
        super(UpdateService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Action1<ArrayList<WeatherModel>> onNextAction = weatherModel -> {
            PrefUtils.setWeatherCache(weatherModel);

            Context context = getApplicationContext();
            context.startService(new Intent(context, UpdateWidget.class));
        };

        Action1<WeatherModel> onErrorAction = weatherModel -> {

        };

        RetrofitManager.getInstance()
                .getWeatherForecastDaily(new LatLng(PrefUtils.getLastKnownLatitude(), PrefUtils.getLastKnownLongitude()),
                        onNextAction, onErrorAction);
    }

}
