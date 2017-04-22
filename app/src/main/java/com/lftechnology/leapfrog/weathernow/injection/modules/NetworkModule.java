package com.lftechnology.leapfrog.weathernow.injection.modules;


import android.util.Log;

import com.lftechnology.leapfrog.weathernow.retrofit.RestApi;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Prepares environment for making http network request
 * <ul>
 * <li>Creates instance of OkHttp client {@link OkHttpClient}</li>
 * </ul>
 */
@Module
public class NetworkModule {

    private static final long CONNECT_TIMEOUT = 20000;
    public static final int READ_TIMEOUT = 20000;

    private final static String BASE_URL = "https://api.forecast.io/";
    private final static String API_KEY = "6bf45156088e2afa94ad4bd23f793252";

    @Singleton
    OkHttpClient okHttpClient;

    @Provides
    @Singleton
    public OkHttpClient getOkHttpClient() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Log.e("HTTP", message);
            }
        });
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder builder = new okhttp3.OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS);

        okHttpClient = builder.build();

        return okHttpClient;
    }

    @Provides
    @Singleton
    public GsonConverterFactory getGsonConverterFactory() {
        return GsonConverterFactory.create();
    }

    @Provides
    @Singleton
    public Retrofit getRetrofit(OkHttpClient okHttpClient, GsonConverterFactory gsonConverterFactory) {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(gsonConverterFactory)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }

    @Provides
    @Singleton
    public RestApi getBartellService(Retrofit retrofit) {
        return retrofit.create(RestApi.class);
    }

}


