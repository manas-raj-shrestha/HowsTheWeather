package com.lftechnology.leapfrog.weathernow.update;

import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.util.Log;

import com.lftechnology.leapfrog.weathernow.weathermodels.WeatherModel;

import java.util.ArrayList;

/**
 * Created by Manas on 6/30/2016.
 */
public class UpdateResultReceiver extends ResultReceiver {
    public static final int CODE_SUCCESS = 0X1;
    public static final int CODE_FAILURE = 0X2;
    public static final String KEY_FETCHED_DATA = "fetched_result";

    UpdateFetcher updateFetcher;

    public UpdateResultReceiver(Handler handler, UpdateFetcher updateFetcher) {
        super(handler);
        this.updateFetcher = updateFetcher;
    }

    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {
        super.onReceiveResult(resultCode, resultData);

        if (resultCode == CODE_SUCCESS) {
            if (resultData != null) {
                ArrayList<WeatherModel> weatherModelArrayList = resultData.getParcelableArrayList(KEY_FETCHED_DATA);
                updateFetcher.getUpdatedWeatherModel(weatherModelArrayList);
            }
        }else {
            Log.e("failure","failure");
            updateFetcher.getUpdatedWeatherModel(null);
        }
    }
}
