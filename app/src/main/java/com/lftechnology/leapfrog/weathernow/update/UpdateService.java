package com.lftechnology.leapfrog.weathernow.update;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;

import com.lftechnology.leapfrog.weathernow.data.PrefUtils;
import com.lftechnology.leapfrog.weathernow.retrofit.RetrofitManager;
import com.lftechnology.leapfrog.weathernow.weathermodels.WeatherModel;
import com.lftechnology.leapfrog.weathernow.widget.UpdateWidget;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

import rx.functions.Action1;

/**
 * uses retrofit to update the cache weather information
 */
public class UpdateService extends IntentService {
    public static final String KEY_RESULT_RECEIVER = "update_result_receiver";

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public UpdateService() {
        super(UpdateService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.e("alarm fired","alarm fired");
        Action1<ArrayList<WeatherModel>> onNextAction = weatherModel -> {
            PrefUtils.setWeatherCache(weatherModel);

            Context context = getApplicationContext();
            context.startService(new Intent(context, UpdateWidget.class));

            if (intent.hasExtra(KEY_RESULT_RECEIVER)) {
                ResultReceiver updateResultReceiver = intent.getParcelableExtra(KEY_RESULT_RECEIVER);
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList(UpdateResultReceiver.KEY_FETCHED_DATA, weatherModel);
                updateResultReceiver.send(UpdateResultReceiver.CODE_SUCCESS, bundle);
            }
        };

        Action1<Exception> onErrorAction = weatherModel -> {
            Log.e("failure", "failure");
            if (intent.hasExtra(KEY_RESULT_RECEIVER)) {
                Log.e("failure2", "failure2");
                ResultReceiver updateResultReceiver = intent.getParcelableExtra(KEY_RESULT_RECEIVER);
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList(UpdateResultReceiver.KEY_FETCHED_DATA, null);
                updateResultReceiver.send(UpdateResultReceiver.CODE_FAILURE, bundle);
            }
        };

        RetrofitManager.getInstance()
                .getWeatherForecastDaily(new LatLng(PrefUtils.getLastKnownLatitude(), PrefUtils.getLastKnownLongitude()),
                        onNextAction, onErrorAction);
    }

}
