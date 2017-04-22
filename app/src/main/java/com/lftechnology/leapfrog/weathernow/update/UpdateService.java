package com.lftechnology.leapfrog.weathernow.update;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;

import com.google.android.gms.maps.model.LatLng;
import com.lftechnology.leapfrog.weathernow.data.PrefUtils;
import com.lftechnology.leapfrog.weathernow.injection.DaggerWeatherNowComponent;
import com.lftechnology.leapfrog.weathernow.injection.modules.NetworkModule;
import com.lftechnology.leapfrog.weathernow.injection.modules.PresenterModule;
import com.lftechnology.leapfrog.weathernow.weathercards.WeatherInteractor;
import com.lftechnology.leapfrog.weathernow.weathermodels.WeatherModel;
import com.lftechnology.leapfrog.weathernow.widget.UpdateWidget;

import java.util.ArrayList;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * uses retrofit to update the cache weather information
 */
public class UpdateService extends IntentService {
    public static final String KEY_RESULT_RECEIVER = "update_result_receiver";

    private CompositeSubscription compositeSubscription = new CompositeSubscription();

    @Inject
    WeatherInteractor weatherInteractor;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public UpdateService() {
        super(UpdateService.class.getSimpleName());
        DaggerWeatherNowComponent.builder()
                .networkModule(new NetworkModule())
                .presenterModule(new PresenterModule())
                .build().inject(this);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
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
            if (intent.hasExtra(KEY_RESULT_RECEIVER)) {
                ResultReceiver updateResultReceiver = intent.getParcelableExtra(KEY_RESULT_RECEIVER);
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList(UpdateResultReceiver.KEY_FETCHED_DATA, null);
                updateResultReceiver.send(UpdateResultReceiver.CODE_FAILURE, bundle);
            }
        };

        this.compositeSubscription.add(weatherInteractor.getWeatherForecastDaily(new LatLng(PrefUtils.getLastKnownLatitude(), PrefUtils.getLastKnownLongitude()), onNextAction, onErrorAction).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe());

//        RetrofitManager.getInstance()
//                .getWeatherForecastDaily(new LatLng(PrefUtils.getLastKnownLatitude(), PrefUtils.getLastKnownLongitude()),
//                        onNextAction, onErrorAction);
    }

}
