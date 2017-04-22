package com.lftechnology.leapfrog.weathernow.base.interactor;

import com.lftechnology.leapfrog.weathernow.injection.DaggerWeatherNowComponent;
import com.lftechnology.leapfrog.weathernow.injection.modules.NetworkModule;
import com.lftechnology.leapfrog.weathernow.injection.modules.PresenterModule;
import com.lftechnology.leapfrog.weathernow.retrofit.RestApi;

import javax.inject.Inject;

import okhttp3.OkHttpClient;

/**
 * Needs to extends by all interactor class
 */
public abstract class BaseInteractor {

    @Inject
    public RestApi retrofitServices;

    @Inject
    protected OkHttpClient okHttpClient;

    public BaseInteractor() {
        DaggerWeatherNowComponent.builder()
                .networkModule(new NetworkModule())
                .presenterModule(new PresenterModule())
                .build().inject(this);
    }

    public void cancelCallbacks() {
        if (okHttpClient != null)
            okHttpClient.dispatcher().cancelAll();
    }

}
