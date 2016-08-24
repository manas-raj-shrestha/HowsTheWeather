package com.lftechnology.leapfrog.weathernow;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.lftechnology.leapfrog.weathernow.weathercards.WeatherCardsActivity;

public class GpsLocationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().matches("android.location.PROVIDERS_CHANGED")) {
            if (context instanceof WeatherCardsActivity)
                Toast.makeText(context, "in android.location.PROVIDERS_CHANGED",
                        Toast.LENGTH_SHORT).show();

//            Intent pushIntent = new Intent(context, LocalService.class);
//            context.startService(pushIntent);
        }
    }
}