package com.droid.manasshrestha.rxandroid.data;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.droid.manasshrestha.rxandroid.WeatherApplication;
import com.droid.manasshrestha.rxandroid.locationhandlers.GpsInfo;
import com.droid.manasshrestha.rxandroid.weathermodels.WeatherModel;
import com.orhanobut.hawk.Hawk;

import java.util.ArrayList;

/**
 * Shared preferences handler
 */
public class PrefUtils {

    private static final String KEY_LATITUDE = "latitude";
    private static final String KEY_LONGITUDE = "longitude";
    private static final String KEY_FIRST_RUN = "first_run";

    private static SharedPreferences getSharedPreferences() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(WeatherApplication.getContext());

        return sharedPreferences;
    }

    /**
     * save user gps location
     *
     * @param location {@link GpsInfo}
     */
    public static void setLastKnownLocation(GpsInfo location) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putString(KEY_LATITUDE, String.valueOf(location.getLatitude()));
        editor.putString(KEY_LONGITUDE, String.valueOf(location.getLongitude()));
        editor.commit();
    }

    /**
     * returns last saved latitude
     *
     * @return latitude
     */
    public static Double getLastKnownLatitude() {
        return Double.parseDouble(getSharedPreferences().getString(KEY_LATITUDE, "0.0"));
    }

    /**
     * returns last saved longitude
     *
     * @return longitude
     */
    public static Double getLastKnownLongitude() {
        return Double.parseDouble(getSharedPreferences().getString(KEY_LONGITUDE, "0.0"));
    }

    /**
     * sets if app is running for first time
     *
     * @param firstRun {@link Boolean} first run
     */
    public static void setFirstRun(Boolean firstRun) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putBoolean(KEY_FIRST_RUN, firstRun);
        editor.commit();
    }

    /**
     * returns if app is running for the first time
     *
     * @return <li>true if first run</li>
     * <li>false otherwise</li>
     */
    public static boolean getFirstRun() {
        return getSharedPreferences().getBoolean(KEY_FIRST_RUN, true);
    }

    public static void setWeatherCache(ArrayList<WeatherModel> weatherCache) {
        Hawk.put("asd", weatherCache);
        Log.e("test", ((ArrayList<WeatherModel>) Hawk.get("asd")).get(0).getTimezone());
    }

}
