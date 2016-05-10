package com.droid.manasshrestha.rxandroid;

import android.app.Application;
import android.content.Context;
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;

/**
 * Created by ManasShrestha on 3/30/16.
 */
public class WeatherApplication extends Application {
   private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        context = this;
    }

    public static Context getContext(){
        return context;
    }
}
