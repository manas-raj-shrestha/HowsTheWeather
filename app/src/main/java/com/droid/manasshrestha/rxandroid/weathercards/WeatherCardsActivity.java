package com.droid.manasshrestha.rxandroid.weathercards;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.droid.manasshrestha.rxandroid.R;
import com.droid.manasshrestha.rxandroid.weathermodels.WeatherModel;
import com.pixelcan.inkpageindicator.InkPageIndicator;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import pl.droidsonroids.gif.GifImageView;
import rx.schedulers.Schedulers;

/**
 * Contains view pager with daily weather info
 */
public class WeatherCardsActivity extends AppCompatActivity implements WeatherCardsActivityContract.Views {

    @Bind(R.id.vp_cards)
    ViewPager viewPager;

    @Bind(R.id.gif_cloud_load)
    GifImageView cloudLoader;

    @Bind(R.id.tv_city_name)
    TextView tvCityName;

    @Bind(R.id.indicator)
    InkPageIndicator inkPageIndicator;

    private WeatherCardsActivityPresenter weatherCardsActivityPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.widget_cards_activity);
        ButterKnife.bind(this);

        weatherCardsActivityPresenter = new WeatherCardsActivityPresenter(this);
        weatherCardsActivityPresenter.startNetworkRequest();

        viewPager.setPageTransformer(true, new CardTiltTransformer());

//        final LocationCatcher locationCatcher = new LocationCatcher(this);
//        locationCatcher.getLocation(new LocationCatcher.LocationCallBack() {
//            @Override
//            public void onLocationNotFound() {
//                if (PrefUtils.getLastKnownLatitude() != 0.0) {
//                    //use the location from shared preferences if location was not found
//                    RetrofitManager.getInstance().getWeatherForecastDaily(new LatLng(PrefUtils.getLastKnownLatitude(), PrefUtils.getLastKnownLongitude()), subscriber2);
//                } else {
//                    locationCatcher.showSettingsAlert();
//                }
//            }
//
//            @Override
//            public void onLocationFound(GpsInfo location) {
//                if (location != null) {
//                    locationCatcher.cancelLocationCallback();
//                    RetrofitManager.getInstance().getWeatherForecastDaily(new LatLng(location.getLatitude(), location.getLongitude()), subscriber2);
//
//                    //save the new location as last known location
//                    PrefUtils.setLastKnownLocation(location);
//                }
//            }
//        });

    }

    @Override
    public void setViewPagerData(ArrayList<WeatherModel> weatherModels) {
        cloudLoader.setVisibility(View.GONE);
        viewPager.setAdapter(new WeatherCardsAdapter(getSupportFragmentManager(), weatherModels));
        inkPageIndicator.setViewPager(viewPager);
    }

    @Override
    public void setUserLocation(String cityName) {
        tvCityName.setText(cityName);
    }
}
