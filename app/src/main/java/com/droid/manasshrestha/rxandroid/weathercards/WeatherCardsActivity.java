package com.droid.manasshrestha.rxandroid.weathercards;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.droid.manasshrestha.rxandroid.R;
import com.droid.manasshrestha.rxandroid.animatedicons.AnimatedLoading;
import com.droid.manasshrestha.rxandroid.weathermodels.WeatherModel;
import com.pixelcan.inkpageindicator.InkPageIndicator;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Contains view pager with daily weather info
 */
public class WeatherCardsActivity extends AppCompatActivity implements WeatherCardsActivityContract.Views {

    @Bind(R.id.vp_cards)
    ViewPager viewPager;

    @Bind(R.id.rl_container)
    RelativeLayout rlContainer;

    @Bind(R.id.animated_loading)
    AnimatedLoading animatedLoading;

    @Bind(R.id.tv_city_name)
    TextView tvCityName;

    @Bind(R.id.indicator)
    InkPageIndicator inkPageIndicator;

    @Bind(R.id.tv_error)
    TextView tvError;

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
        rlContainer.removeView(animatedLoading);
        viewPager.setAdapter(new WeatherCardsAdapter(getSupportFragmentManager(), weatherModels));
        inkPageIndicator.setViewPager(viewPager);
    }

    @Override
    public void setUserLocation(String cityName) {
        tvCityName.setText(cityName);
    }

    @Override
    public void setError(View view, String errorMessage) {
        tvError.setText(errorMessage);
        tvError.setVisibility(View.VISIBLE);

        rlContainer.removeView(animatedLoading);
        rlContainer.addView(view);
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
//                    setError("Application need access to GPS to function properly. Please go to settings and allow location for Weather Now.");
                }
                return;
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }
}
