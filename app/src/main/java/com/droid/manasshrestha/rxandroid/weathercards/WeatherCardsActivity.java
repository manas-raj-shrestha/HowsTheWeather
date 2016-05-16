package com.droid.manasshrestha.rxandroid.weathercards;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.droid.manasshrestha.rxandroid.R;
import com.droid.manasshrestha.rxandroid.animatedicons.LoadingView;
import com.droid.manasshrestha.rxandroid.animatedicons.NoPermissionView;
import com.droid.manasshrestha.rxandroid.weathermodels.WeatherModel;
import com.pixelcan.inkpageindicator.InkPageIndicator;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Contains view pager with daily weather info
 */
public class WeatherCardsActivity extends AppCompatActivity implements WeatherCardsActivityContract.Views {

    @Bind(R.id.vp_cards)
    ViewPager viewPager;

    @Bind(R.id.rl_container)
    RelativeLayout rlContainer;

    @Bind(R.id.tv_city_name)
    TextView tvCityName;

    @Bind(R.id.indicator)
    InkPageIndicator inkPageIndicator;

    @Bind(R.id.tv_error)
    TextView tvStatus;

    private WeatherCardsActivityPresenter weatherCardsActivityPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.widget_cards_activity);
        ButterKnife.bind(this);

        weatherCardsActivityPresenter = new WeatherCardsActivityPresenter(this);

        viewPager.setPageTransformer(true, new CardTiltTransformer());
    }

    @Override
    public void setViewPagerData(ArrayList<WeatherModel> weatherModels) {
        rlContainer.removeAllViews();
        tvStatus.setVisibility(View.GONE);
        viewPager.setAdapter(new WeatherCardsAdapter(getSupportFragmentManager(), weatherModels));
        inkPageIndicator.setViewPager(viewPager);
    }

    @Override
    public void setUserLocation(String cityName) {
        tvCityName.setText(cityName);
    }

    @Override
    public void setError(View view, String errorMessage) {
        tvStatus.setText(errorMessage);
        tvStatus.setVisibility(View.VISIBLE);

        rlContainer.removeAllViews();
        rlContainer.addView(view);
    }

    @Override
    public void showLoadingIcon() {
        rlContainer.removeAllViews();
        rlContainer.addView(new LoadingView(this));
        tvStatus.setVisibility(View.VISIBLE);
        tvStatus.setText("Its just gonna take a minute.");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case WeatherCardsActivityPresenter.MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    weatherCardsActivityPresenter.startNetworkRequest();
                } else {
                    setError(new NoPermissionView(this), "Please go to settings and allow \nlocation permission for \n Weather Now.");
                }
                return;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("onRes called", "onRes called");
        new Handler().postDelayed(() -> {
            if (viewPager.getAdapter() == null) {
                Log.e("onResumee", "onResume");
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    weatherCardsActivityPresenter.checkPermissions();
                } else {
                    weatherCardsActivityPresenter.startNetworkRequest();
                }
            }
        },2000);

    }

    @OnClick(R.id.rl_container)
    public void setOnClicks() {
        weatherCardsActivityPresenter.checkIconClick(rlContainer);
    }
}
