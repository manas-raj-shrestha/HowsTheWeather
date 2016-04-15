package com.droid.manasshrestha.rxandroid.data;

import android.content.SharedPreferences;
import android.location.Location;
import android.preference.PreferenceManager;

import com.droid.manasshrestha.rxandroid.WeatherApplication;
import com.droid.manasshrestha.rxandroid.locationhandlers.GpsInfo;

/**
 * Shared preferences handler
 */
public class PrefUtils {

    private static final String KEY_LATITUDE = "latitude";
    private static final String KEY_LONGITUDE = "longitude";

    private static SharedPreferences getSharedPreferences() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(WeatherApplication.getContext());

        return sharedPreferences;
    }

    public static void setLastKnownLocation(GpsInfo location) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putString(KEY_LATITUDE, String.valueOf(location.getLatitude()));
        editor.putString(KEY_LONGITUDE, String.valueOf(location.getLongitude()));
        editor.commit();
    }

    public static Double getLastKnownLatitude() {
        return Double.parseDouble(getSharedPreferences().getString(KEY_LATITUDE, "0.0"));
    }

    public static Double getLastKnownLongitude() {
        return Double.parseDouble(getSharedPreferences().getString(KEY_LONGITUDE, "0.0"));
    }

}
