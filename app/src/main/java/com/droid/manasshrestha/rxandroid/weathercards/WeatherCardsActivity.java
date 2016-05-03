package com.droid.manasshrestha.rxandroid.weathercards;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.droid.manasshrestha.rxandroid.R;
import com.droid.manasshrestha.rxandroid.weathermodels.WeatherModel;
import com.pixelcan.inkpageindicator.InkPageIndicator;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import pl.droidsonroids.gif.GifImageView;

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
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            weatherCardsActivityPresenter.checkPermissions();
        } else {
            weatherCardsActivityPresenter.startNetworkRequest();
        }

        viewPager.setPageTransformer(true, new CardTiltTransformer());
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

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case WeatherCardsActivityPresenter.MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    weatherCardsActivityPresenter.startNetworkRequest();

                } else {
                    Toast.makeText(this, "Application need access to GPS to function properly.", Toast.LENGTH_SHORT).show();
                    finish();
                }
                return;
            }
        }
    }
}
