package com.droid.manasshrestha.rxandroid.weathercards;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.droid.manasshrestha.rxandroid.R;
import com.droid.manasshrestha.rxandroid.data.PrefUtils;
import com.droid.manasshrestha.rxandroid.locationhandlers.GpsInfo;
import com.droid.manasshrestha.rxandroid.locationhandlers.LocationCatcher;
import com.droid.manasshrestha.rxandroid.retrofit.RetrofitManager;
import com.droid.manasshrestha.rxandroid.weathermodels.WeatherModel;
import com.google.android.gms.maps.model.LatLng;
import com.pixelcan.inkpageindicator.InkPageIndicator;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import pl.droidsonroids.gif.GifImageView;
import rx.Subscriber;

/**
 * Contains view pager with daily weather info
 */
public class WidgetCardsActivity extends AppCompatActivity {

    @Bind(R.id.vp_cards)
    ViewPager viewPager;

    @Bind(R.id.gif_cloud_load)
    GifImageView cloudLoader;

    @Bind(R.id.tv_city_name)
    TextView tvCityName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.widget_cards_activity);
        ButterKnife.bind(this);

        viewPager.setPageTransformer(true, new CardTiltTransformer());

        final Subscriber subscriber2 = new Subscriber<ArrayList<WeatherModel>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.e("error", e.toString());
            }

            @Override
            public void onNext(final ArrayList<WeatherModel> example) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        cloudLoader.setVisibility(View.GONE);
                        viewPager.setAdapter(new WeatherCardsAdapter(getSupportFragmentManager(), example));
                        InkPageIndicator inkPageIndicator = (InkPageIndicator) findViewById(R.id.indicator);
                        inkPageIndicator.setViewPager(viewPager);
                        String[] strings = example.get(0).getTimezone().split("/");
                        Log.e("splitted", String.valueOf(strings.length));
                        tvCityName.setText(strings[1].toUpperCase());
                    }
                }, 1000);
            }
        };

        final LocationCatcher locationCatcher = new LocationCatcher(this);
        locationCatcher.getLocation(new LocationCatcher.LocationCallBack() {
            @Override
            public void onLocationNotFound() {
                if (PrefUtils.getLastKnownLatitude() != 0.0) {
                    RetrofitManager.getInstance().getWeatherForecastDaily(new LatLng(PrefUtils.getLastKnownLatitude(), PrefUtils.getLastKnownLongitude()), subscriber2);
                } else {
                    locationCatcher.showSettingsAlert();
                }
            }

            @Override
            public void onLocationFound(GpsInfo location) {
                if (location != null) {
                    locationCatcher.cancelLocationCallback();
                    RetrofitManager.getInstance().getWeatherForecastDaily(new LatLng(location.getLatitude(), location.getLongitude()), subscriber2);
                    PrefUtils.setLastKnownLocation(location);
                }
            }
        });

    }
}
