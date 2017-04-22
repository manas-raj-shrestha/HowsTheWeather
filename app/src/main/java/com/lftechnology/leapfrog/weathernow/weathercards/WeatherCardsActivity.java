package com.lftechnology.leapfrog.weathernow.weathercards;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.droid.manasshrestha.weathernow.R;
import com.lftechnology.leapfrog.weathernow.animatedicons.LoadingView;
import com.lftechnology.leapfrog.weathernow.animatedicons.NoPermissionView;
import com.lftechnology.leapfrog.weathernow.data.PrefUtils;
import com.lftechnology.leapfrog.weathernow.update.UpdateFetcher;
import com.lftechnology.leapfrog.weathernow.update.UpdateResultReceiver;
import com.lftechnology.leapfrog.weathernow.update.UpdateService;
import com.lftechnology.leapfrog.weathernow.weathercards.contracts.WeatherCardsActivityContract;
import com.lftechnology.leapfrog.weathernow.weathercards.transformer.CardTiltTransformer;
import com.lftechnology.leapfrog.weathernow.weathermodels.WeatherModel;
import com.pixelcan.inkpageindicator.InkPageIndicator;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Contains view pager with daily weather info
 */
public class WeatherCardsActivity extends AppCompatActivity implements WeatherCardsActivityContract.Views, UpdateFetcher {
    private static final int ANIM_DURATION = 5000;
    private static final float START_ANGLE = 0.0f;
    private static final float END_ANGLE = 360.0f;
    private static final float PIVOT_VALUE = 0.5f;
    private static final int REPEAT_COUNT = -1;
    private static int FETCH_LOCATION_DELAY = 6800;

    @BindView(R.id.vp_cards)
    ViewPager viewPager;

    @BindView(R.id.rl_container)
    RelativeLayout rlContainer;

    @BindView(R.id.tv_city_name)
    TextView tvCityName;

    @BindView(R.id.indicator)
    InkPageIndicator inkPageIndicator;

    @BindView(R.id.tv_error)
    TextView tvStatus;

    @BindView(R.id.iv_update)
    ImageView ivUpdate;

    private WeatherCardsActivityPresenter weatherCardsActivityPresenter;
    WeatherCardsAdapter weatherCardsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.widget_cards_activity);
        ButterKnife.bind(this);

        weatherCardsActivityPresenter = new WeatherCardsActivityPresenter(this);


    }

    @Override
    public void setViewPagerData(ArrayList<WeatherModel> weatherModels) {
        viewPager.setPageTransformer(true, new CardTiltTransformer());
        rlContainer.removeAllViews();
        tvStatus.setVisibility(View.GONE);
        viewPager.setAdapter(null);
        weatherCardsAdapter = new WeatherCardsAdapter(getSupportFragmentManager(), weatherModels);
        viewPager.setAdapter(weatherCardsAdapter);
        inkPageIndicator.setViewPager(viewPager);
        ivUpdate.setVisibility(View.VISIBLE);

    }

    @Override
    public void setUserLocation(String cityName) {
        Log.e("name",cityName);
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
        tvStatus.setText(getString(R.string.txt_loading_ticker));
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
                    setError(new NoPermissionView(this), getString(R.string.txt_allow_permission));
                }
                return;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        /*fetching location takes time. if we start location request as soon as we turn on the location, it will return null
        * hence the delay*/
        if (PrefUtils.getWeatherCache() != null) {
            weatherCardsActivityPresenter.startNetworkRequest();
        } else {
            new Handler().postDelayed(() -> {
                if (viewPager.getAdapter() == null) {
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                        weatherCardsActivityPresenter.checkPermissions();
                    } else {
                        weatherCardsActivityPresenter.startNetworkRequest();
                    }
                }
            }, FETCH_LOCATION_DELAY);
        }

    }

    UpdateResultReceiver updateResultReceiver;

    @OnClick({R.id.rl_container, R.id.iv_update})
    public void setOnClicks(View view) {
        switch (view.getId()) {
            case R.id.rl_container:
                weatherCardsActivityPresenter.checkIconClick(rlContainer);
                break;
            case R.id.iv_update:
                updateResultReceiver = new UpdateResultReceiver(new Handler(), WeatherCardsActivity.this);
                Intent intent = new Intent(this, UpdateService.class);
                intent.putExtra(UpdateService.KEY_RESULT_RECEIVER, updateResultReceiver);
                startService(intent);

                Animation animation = new RotateAnimation(START_ANGLE, END_ANGLE,
                        Animation.RELATIVE_TO_SELF, PIVOT_VALUE, Animation.RELATIVE_TO_SELF,
                        PIVOT_VALUE);
                animation.setInterpolator(new LinearInterpolator());
                animation.setDuration(ANIM_DURATION);

                animation.setRepeatCount(REPEAT_COUNT);
                ivUpdate.startAnimation(animation);

                break;
        }

    }

    @Override
    public void getUpdatedWeatherModel(ArrayList<WeatherModel> weatherModelArrayList) {

        if (weatherModelArrayList != null && viewPager != null) {
            viewPager.setAdapter(new WeatherCardsAdapter(getSupportFragmentManager(), weatherModelArrayList));
        } else {
            Toast.makeText(this, "Error connecting. Please check your internet connection.", Toast.LENGTH_SHORT).show();
        }

        ivUpdate.clearAnimation();
    }
}
