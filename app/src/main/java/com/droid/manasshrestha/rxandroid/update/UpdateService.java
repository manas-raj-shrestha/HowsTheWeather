package com.droid.manasshrestha.rxandroid.update;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.droid.manasshrestha.rxandroid.data.PrefUtils;
import com.droid.manasshrestha.rxandroid.retrofit.RetrofitManager;
import com.droid.manasshrestha.rxandroid.weathermodels.WeatherModel;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Calendar;

import rx.functions.Action1;

/**
 * Created by ManasShrestha on 6/9/16.
 */
public class UpdateService extends IntentService {

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public UpdateService() {
        super("Update Service");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("onCreate", "onCreate");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.e("onHandleIntent", "onHandleTriggered");

        Action1<ArrayList<WeatherModel>> onNextAction = weatherModel -> {
            PrefUtils.setWeatherCache(weatherModel);
            Log.e("Call finished", "Call Finished");
        };
        Action1<WeatherModel> onErrorAction = weatherModel -> {

        };

        RetrofitManager.getInstance().getWeatherForecastDaily(new LatLng(27.714203, 85.327163), onNextAction, onErrorAction);
    }

    @Override
    public void onDestroy() {

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Log.e("onDestroy", "onDestroy" + alarmManager.getNextAlarmClock());
//        Intent intent = new Intent(this, UpdateService.class);
//        PendingIntent pendingIntent = PendingIntent.getService(this,
//                0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);


        super.onDestroy();
    }
}
