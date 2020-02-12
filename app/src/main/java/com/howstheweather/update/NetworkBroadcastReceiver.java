package com.howstheweather.update;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.howstheweather.data.PrefUtils;

/**
 * Created by Manas on 7/14/2016.
 */
public class NetworkBroadcastReceiver extends BroadcastReceiver {

    private final static long INTERVAL_IN_MS = 86400000;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (isOnline(context) && ((System.currentTimeMillis() - PrefUtils.getLastUpdatedLong()) > INTERVAL_IN_MS)) {
            context.startService(new Intent(context, UpdateService.class));
        }
    }

    public boolean isOnline(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        //should check null because in airplane mode it will be null
        return (netInfo != null && netInfo.isConnected());
    }
}
