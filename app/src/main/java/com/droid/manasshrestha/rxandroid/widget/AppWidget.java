package com.droid.manasshrestha.rxandroid.widget;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.RemoteViews;

import com.droid.manasshrestha.rxandroid.GeneralUtils;
import com.droid.manasshrestha.rxandroid.R;
import com.droid.manasshrestha.rxandroid.data.Constants;
import com.droid.manasshrestha.rxandroid.data.PrefUtils;
import com.droid.manasshrestha.rxandroid.weathermodels.DailyData;
import com.droid.manasshrestha.rxandroid.weathermodels.WeatherModel;

import java.util.ArrayList;

public class AppWidget extends AppWidgetProvider {

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
    }

    private void createAlarm(Context context, int[] appWidgetIds) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
        Intent intent = new Intent(context, AppWidget.class);

        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);

        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.setRepeating(AlarmManager.RTC, System.currentTimeMillis() + 5000, 0, pendingIntent);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
    }


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        createAlarm(context, appWidgetIds);

        context.startService(new Intent(context,UpdateWidget.class));
    }

}