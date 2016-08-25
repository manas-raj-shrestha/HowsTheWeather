package com.lftechnology.leapfrog.weathernow.weathercards;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
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
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.lftechnology.leapfrog.weathernow.animatedicons.LoadingView;
import com.lftechnology.leapfrog.weathernow.animatedicons.NoPermissionView;
import com.lftechnology.leapfrog.weathernow.data.PrefUtils;
import com.lftechnology.leapfrog.weathernow.locationhandlers.GpsInfo;
import com.lftechnology.leapfrog.weathernow.update.UpdateFetcher;
import com.lftechnology.leapfrog.weathernow.update.UpdateResultReceiver;
import com.lftechnology.leapfrog.weathernow.update.UpdateService;
import com.lftechnology.leapfrog.weathernow.weathermodels.WeatherModel;
import com.pixelcan.inkpageindicator.InkPageIndicator;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Contains view pager with daily weather info
 */
public class WeatherCardsActivity extends AppCompatActivity implements WeatherCardsActivityContract.Views, UpdateFetcher, GoogleApiClient.OnConnectionFailedListener {
    private static final int ANIM_DURATION = 5000;
    private static final float START_ANGLE = 0.0f;
    private static final float END_ANGLE = 360.0f;
    private static final float PIVOT_VALUE = 0.5f;
    private static final int REPEAT_COUNT = -1;
    private static int FETCH_LOCATION_DELAY = 6800;
    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    private GoogleApiClient mGoogleApiClient;
    private boolean locationChanged = false;

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

    @Bind(R.id.iv_update)
    ImageView ivUpdate;

    @Bind(R.id.cv_fragment_container)
    CardView cardViewFragmentContainer;

    private WeatherCardsActivityPresenter weatherCardsActivityPresenter;
    WeatherCardsAdapter weatherCardsAdapter;
    PlaceAutocompleteFragment autocompleteFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.widget_cards_activity);
        ButterKnife.bind(this);

        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();

        weatherCardsActivityPresenter = new WeatherCardsActivityPresenter(this);

        viewPager.setPageTransformer(true, new CardTiltTransformer());

        autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

//        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
//            @Override
//            public void onPlaceSelected(Place place) {
//                cardViewFragmentContainer.setVisibility(View.GONE);
//                PrefUtils.setLastKnownLocation(new GpsInfo(place.getLatLng().latitude,place.getLatLng().longitude));
//                weatherCardsActivityPresenter.startNetworkRequest(true);
//                Log.e("auto", "auto");
//                locationChanged = true;
//            }
//
//            @Override
//            public void onError(Status status) {
//                cardViewFragmentContainer.setVisibility(View.GONE);
//            }
//        });
    }

    @Override
    public void setViewPagerData(ArrayList<WeatherModel> weatherModels) {
        rlContainer.removeAllViews();
        tvStatus.setVisibility(View.GONE);
        viewPager.setAdapter(null);
        viewPager.setVisibility(View.VISIBLE);
        weatherCardsAdapter = new WeatherCardsAdapter(getSupportFragmentManager(), weatherModels);
        viewPager.setAdapter(weatherCardsAdapter);
        inkPageIndicator.setVisibility(View.VISIBLE);
        inkPageIndicator.setViewPager(viewPager);
        ivUpdate.setVisibility(View.VISIBLE);
        tvCityName.setVisibility(View.VISIBLE);
    }

    @Override
    public void setUserLocation(String cityName) {
//        tvCityName.setVisibility(View.VISIBLE);
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
        viewPager.setVisibility(View.GONE);
        tvCityName.setVisibility(View.GONE);
        inkPageIndicator.setVisibility(View.GONE);
        ivUpdate.setVisibility(View.GONE);
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

        Log.e("res","res");
        /*fetching location takes time. if we start location request as soon as we turn on the location, it will return null
        * hence the delay*/
        if (PrefUtils.getWeatherCache() != null) {
            weatherCardsActivityPresenter.startNetworkRequest(locationChanged);
            locationChanged = false;
        } else {
            new Handler().postDelayed(() -> {
                if (viewPager.getAdapter() == null) {
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                        weatherCardsActivityPresenter.checkPermissions();
                    } else {
                        weatherCardsActivityPresenter.startNetworkRequest(false);
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

    @OnClick(R.id.tv_city_name)
    public void onClicks() {
//        cardViewFragmentContainer.setVisibility(View.VISIBLE);

        try {
            Intent intent =
                    new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                            .build(this);
            startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
        } catch (GooglePlayServicesRepairableException e) {
            // TODO: Handle the error.
        } catch (GooglePlayServicesNotAvailableException e) {
            // TODO: Handle the error.
        }

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                Log.i("TAG", "Place: " + place.getName() +" "+ place.getLatLng().latitude+ " " + place.getLatLng().longitude) ;

                cardViewFragmentContainer.setVisibility(View.GONE);
                PrefUtils.setLastKnownLocation(new GpsInfo(place.getLatLng().latitude,place.getLatLng().longitude));
                weatherCardsActivityPresenter.startNetworkRequest(true);
                Log.e("auto", "auto");
                locationChanged = true;

            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                // TODO: Handle the error.
                Log.i("TAG", status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }



}
