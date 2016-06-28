package com.droid.manasshrestha.rxandroid.widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
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

/**
 * background service to update widgets
 */
public class UpdateWidget extends IntentService {

    public UpdateWidget() {
        super(UpdateWidget.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        ArrayList<WeatherModel> weatherModels = PrefUtils.getWeatherCache();

        Context context = getApplicationContext();
        ComponentName name = new ComponentName(context, AppWidget.class);
        int[] ids = AppWidgetManager.getInstance(context).getAppWidgetIds(name);

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);

        if (weatherModels != null) {
            DailyData dailyData = weatherModels.get(0).getDaily().getData().get(0);

            RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                    R.layout.widget);
            for (int i = 0; i < ids.length; i++) {
                int widgetId = ids[i];

                remoteViews.setTextViewText(R.id.tv_widget_day,
                        GeneralUtils.parseDate(Constants.DATE_FORMAT_DAY, dailyData.getTime()));

                remoteViews.setTextViewText(R.id.tv_widget_date,
                        GeneralUtils.parseDate(Constants.DATE_FORMAT_DAY_MONTH, dailyData.getTime()));

                remoteViews.setImageViewResource(R.id.iv_widget_icon, GeneralUtils.getWidgetIcons(dailyData.getIcon()));

                String averageTemp = String.valueOf((int) (dailyData.getTemperatureMax() + dailyData.getTemperatureMin()) / 2);

                remoteViews.setTextViewText(R.id.tv_widget_temp, averageTemp + "°C");

                remoteViews.setTextViewText(R.id.tv_widget_last_updated, "Last Updated: " + PrefUtils.getLastUpdated());

                remoteViews.setViewVisibility(R.id.tv_error, View.GONE);
                remoteViews.setViewVisibility(R.id.iv_widget_icon, View.VISIBLE);
                remoteViews.setViewVisibility(R.id.tc_digital, View.VISIBLE);
                remoteViews.setViewVisibility(R.id.tv_widget_day, View.VISIBLE);
                remoteViews.setViewVisibility(R.id.tv_widget_date, View.VISIBLE);

                appWidgetManager.updateAppWidget(widgetId, remoteViews);
            }
        } else {
            for (int i = 0; i < ids.length; i++) {
                int widgetId = ids[i];

                RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget);
                remoteViews.setViewVisibility(R.id.tv_error, View.VISIBLE);
                remoteViews.setViewVisibility(R.id.iv_widget_icon, View.GONE);
                remoteViews.setViewVisibility(R.id.tc_digital, View.GONE);
                remoteViews.setViewVisibility(R.id.tv_widget_day, View.GONE);
                remoteViews.setViewVisibility(R.id.tv_widget_date, View.GONE);

                appWidgetManager.updateAppWidget(widgetId, remoteViews);
            }

        }
    }
}
