package com.howstheweather.widget;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.widget.RemoteViews;

import com.howstheweather.GeneralUtils;
import com.howstheweather.R;
import com.howstheweather.WeatherApplication;
import com.howstheweather.data.Constants;
import com.howstheweather.data.PrefUtils;
import com.howstheweather.splashscreen.SplashActivity;
import com.howstheweather.weathercards.WeatherCardsActivity;
import com.howstheweather.weathermodels.DailyData;
import com.howstheweather.weathermodels.WeatherModel;

import java.util.ArrayList;

/**
 * background service to update widgets
 */
public class UpdateWidget extends IntentService {

    private static final int NOTIFICATION_ID = 0X1;

    /**
     * intent service requires a default constructor
     */
    public UpdateWidget() {
        super(UpdateWidget.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        ArrayList<WeatherModel> weatherModels = PrefUtils.getWeatherCache();

        Context context = getApplicationContext();
        Intent notificationIntent = new Intent(context, WeatherCardsActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
        ComponentName name = new ComponentName(context, AppWidget.class);
        int[] ids = AppWidgetManager.getInstance(context).getAppWidgetIds(name);

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);

        if (weatherModels != null) {
            DailyData dailyData = weatherModels.get(0).getDaily().getData().get(0);

            RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                    R.layout.widget);
            remoteViews.setOnClickPendingIntent(R.id.rl_widget, contentIntent);

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

            createNotification(dailyData);
        } else {
            for (int i = 0; i < ids.length; i++) {
                int widgetId = ids[i];

                RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget);
                remoteViews.setOnClickPendingIntent(R.id.rl_widget, contentIntent);
                remoteViews.setViewVisibility(R.id.tv_error, View.VISIBLE);
                remoteViews.setViewVisibility(R.id.iv_widget_icon, View.GONE);
                remoteViews.setViewVisibility(R.id.tc_digital, View.GONE);
                remoteViews.setViewVisibility(R.id.tv_widget_day, View.GONE);
                remoteViews.setViewVisibility(R.id.tv_widget_date, View.GONE);

                appWidgetManager.updateAppWidget(widgetId, remoteViews);
            }

        }
    }

    /**
     * creates notification with ticker text according to the weather icon info
     *
     * @param dailyData {@link DailyData}
     */
    private void createNotification(DailyData dailyData) {
        Context context = WeatherApplication.getContext();
        Intent notificationIntent = new Intent(context, SplashActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(getNotificationIcon()).setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.app_icon))
                        .setContentTitle(getResources().getString(R.string.app_name))
                        .setContentText(GeneralUtils.getNotificationTicker(dailyData.getIcon()))
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(GeneralUtils.getNotificationTicker(dailyData.getIcon())))
                        .setContentIntent(contentIntent)
                        .setAutoCancel(true);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }

    /**
     * check the version code and load white icon if the os version is lollipop or greater
     * else returns the app icon
     *
     * @return small nofitication icon
     */
    private int getNotificationIcon() {
        boolean useWhiteIcon = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP;
        return useWhiteIcon ? R.drawable.app_icon_white_tint : R.drawable.app_icon;
    }
}
