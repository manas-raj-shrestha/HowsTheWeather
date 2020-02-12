package com.howstheweather;

import android.app.Application;
import android.content.Context;
//import com.crashlytics.android.Crashlytics;
import com.orhanobut.hawk.Hawk;
import com.orhanobut.hawk.HawkBuilder;
import com.orhanobut.hawk.LogLevel;

//import io.fabric.sdk.android.Fabric;

/**
 * Created by ManasShrestha on 3/30/16.
 */
public class WeatherApplication extends Application {
   private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
//        Fabric.with(this, new Crashlytics());
        context = this;

        Hawk.init(this)
                .setEncryptionMethod(HawkBuilder.EncryptionMethod.MEDIUM)
                .setStorage(HawkBuilder.newSharedPrefStorage(this))
                .setLogLevel(LogLevel.FULL)
                .build();
    }

    public static Context getContext(){
        return context;
    }
}
