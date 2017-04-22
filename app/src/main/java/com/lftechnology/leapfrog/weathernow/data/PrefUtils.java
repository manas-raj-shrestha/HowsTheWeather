package com.lftechnology.leapfrog.weathernow.data;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.lftechnology.leapfrog.weathernow.utils.GeneralUtils;
import com.lftechnology.leapfrog.weathernow.WeatherApplication;
import com.lftechnology.leapfrog.weathernow.location.GpsInfo;
import com.lftechnology.leapfrog.weathernow.weathermodels.WeatherModel;
import com.orhanobut.hawk.Hawk;

import java.util.ArrayList;

/**
 * Shared preferences handler
 */
public class PrefUtils {

    private static final String KEY_LATITUDE = "latitude";
    private static final String KEY_LONGITUDE = "longitude";
    private static final String KEY_FIRST_RUN = "first_run";
    private static final String KEY_LAST_UPDATED_LONG = "time_in_long";
    private static String KEY_LAST_UPDATED = "last_updated";

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
        setLastUpdated(GeneralUtils.parseDate(Constants.DATE_FORMAT_DAY_MONTH, weatherCache.get(0).getDaily().getData().get(0).getTime()));
        setLastUpdatedLong(System.currentTimeMillis());
    }

    private static void setLastUpdatedLong(Long time) {
        Hawk.put(KEY_LAST_UPDATED_LONG, time);
    }

    public static Long getLastUpdatedLong() {
        return Hawk.get(KEY_LAST_UPDATED_LONG,0l);
    }

    public static ArrayList<WeatherModel> getWeatherCache() {
        return (ArrayList<WeatherModel>) Hawk.get("asd");
    }

    public static void setLastUpdated(String lastUpdated) {
        Hawk.put(KEY_LAST_UPDATED, lastUpdated);
    }

    public static String getLastUpdated() {
        return Hawk.get(KEY_LAST_UPDATED, "App hasn't been initialized yet");
    }

}
