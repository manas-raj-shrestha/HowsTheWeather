package com.droid.manasshrestha.rxandroid.weathercards;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.droid.manasshrestha.rxandroid.R;
import com.droid.manasshrestha.rxandroid.weathermodels.Temp;
import com.droid.manasshrestha.rxandroid.weathermodels.WeatherModel;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.view.LineChartView;

/**
 * Contains individual weather cards
 */
public class WeatherCardsFragment extends Fragment implements WeatherCardContract.Views {

    private final static String KEY_BUNDLE = "bundle";

    @Bind(R.id.tv_week_day)
    TextView tvWeekDay;

    @Bind(R.id.iv_water_drop)
    ImageView ivWaterDrop;

    @Bind(R.id.iv_clouds)
    ImageView ivClouds;

    @Bind(R.id.tv_avg_temp)
    TextView tvAvgTemp;

    @Bind(R.id.fl_animated_icons)
    FrameLayout flAnimatedIcons;

    @Bind(R.id.tv_humidity)
    TextView tvHumidity;

    @Bind(R.id.iv_bg_back)
    ImageView ivBgBack;

    @Bind(R.id.tv_clouds)
    TextView tvClouds;

    @Bind(R.id.ll_card_front)
    LinearLayout cardContainerFront;

    @Bind(R.id.card_front)
    CardView cardFront;

    @Bind(R.id.card_back)
    CardView cardBack;

    @Bind(R.id.main_activity_root)
    RelativeLayout rvMainRoot;

    @Bind(R.id.tv_pressure)
    TextView tvPressure;

    @Bind(R.id.tv_wind_speed)
    TextView tvWindSpeed;

    @Bind(R.id.tv_wind_direction)
    TextView tvWindDirection;

    @Bind(R.id.tv_max_temp_time)
    TextView tvMaxTempTime;

    @Bind(R.id.tv_max_temp)
    TextView tvMaxTemp;

    @Bind(R.id.tv_min_temp)
    TextView tvMinTemp;

    @Bind(R.id.tv_temp_min_time)
    TextView tvMinTempTime;

    @Bind(R.id.tv_weather_desc)
    TextView tvWeatherDesc;

    @Bind(R.id.chart)
    LineChartView lineChartView;

    @Bind(R.id.iv_bg)
    ImageView ivBgFont;

    @Bind(R.id.tv_weather_ticker)
    TextView tvTicker;

    @Bind(R.id.view_overlay)
    View overlay;

    WeatherModel forecastList;
    WeatherCardPresenter weatherCardPresenter;

    public static WeatherCardsFragment newInstance(WeatherModel forecastList) {
        WeatherCardsFragment fragmentPackageConfirmation = new WeatherCardsFragment();
        Bundle args = new Bundle();
        args.putParcelable(KEY_BUNDLE, forecastList);
        fragmentPackageConfirmation.setArguments(args);
        return fragmentPackageConfirmation;
    }

    public WeatherCardsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.weather_card_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        forecastList = getArguments().getParcelable(KEY_BUNDLE);
        weatherCardPresenter = new WeatherCardPresenter(getActivity(), this, forecastList);
        weatherCardPresenter.setData();

        Glide.with(this).load(R.drawable.water_drop).into(ivWaterDrop);
        Glide.with(this).load(R.drawable.clouds).into(ivClouds);

    }

    @Override
    public void setWeekDay(String weekDay) {
        tvWeekDay.setText(weekDay);
    }

    @Override
    public void setWeatherIcon(ViewGroup viewGroup) {

        RelativeLayout.LayoutParams layoutParam = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        viewGroup.setLayoutParams(layoutParam);

        flAnimatedIcons.addView(viewGroup);
    }

    @Override
    public void setHumidity(int humidity) {
        tvHumidity.setText(String.valueOf(humidity) + "%");
    }

    @Override
    public void setAvgTemp(int temp) {
        tvAvgTemp.setText(temp + "\u00B0");
    }

    @Override
    public void setClouds(int clouds) {
        tvClouds.setText(String.valueOf(clouds) + "%");
    }

    @Override
    public void setCardBackground(int colorId, int drawableId) {
        overlay.setBackgroundColor(colorId);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            Glide.with(this).load(drawableId).into(ivBgFont);
            Glide.with(this).load(drawableId).into(ivBgBack);
        } else {
            Glide.with(this).load(drawableId).bitmapTransform(new RoundedCornersTransformation(getContext(),
                    (int) getResources().getDimension(R.dimen.card_corner_radius), 0)).into(ivBgFont);

            Glide.with(this).load(drawableId).bitmapTransform(new RoundedCornersTransformation(getContext(),
                    (int) getResources().getDimension(R.dimen.card_corner_radius), 0)).into(ivBgBack);
        }

    }

    @Override
    public void setAtmosphericPressure(String pressure) {
        tvPressure.append(pressure);
    }

    @Override
    public void setWindSpeed(String windSpeed) {
        tvWindSpeed.append(windSpeed);
    }

    @Override
    public void setWindDirection(String windDirection) {
        tvWindDirection.append(windDirection);
    }

    @Override
    public void setTemperature(Temp temperature) {
        tvMaxTempTime.setText(temperature.getMaxTempTime());
        tvMaxTemp.setText(temperature.getMaxTemp() + "°C");
        tvMinTemp.setText(temperature.getMinTemp() + "°C");
        tvMinTempTime.setText(temperature.getMinTempTime());
    }

    @Override
    public void setWeatherDesc(String desc) {
        tvWeatherDesc.setText(desc);
    }

    @Override
    public void setLineSetData(LineChartData lineChartData) {
        lineChartView.setLineChartData(lineChartData);
        lineChartView.setInteractive(false);
    }

    @Override
    public void setWeatherTicker(String weatherTicker) {
        tvTicker.setText(weatherTicker);
    }

    private void flipCard(final View currentView, final View nextView) {

        ValueAnimator animator = new ValueAnimator().ofFloat(rvMainRoot.getScaleX(), 0.6f);
        animator.setDuration(250);

        animator.addUpdateListener(valueAnimator -> {
            rvMainRoot.setScaleX((Float) (valueAnimator.getAnimatedValue()));
            rvMainRoot.setScaleY((Float) (valueAnimator.getAnimatedValue()));
        });

        animator.start();

        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {

                FlipAnimation flipAnimation = new FlipAnimation(currentView, nextView);

                if (currentView.getVisibility() == View.GONE) {
                    flipAnimation.reverse();
                }

                flipAnimation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        ValueAnimator animator = new ValueAnimator().ofFloat(rvMainRoot.getScaleX(), 1f);
                        animator.setDuration(250);

                        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                                rvMainRoot.setScaleX((Float) (valueAnimator.getAnimatedValue()));
                                rvMainRoot.setScaleY((Float) (valueAnimator.getAnimatedValue()));
                            }
                        });

                        animator.start();
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                rvMainRoot.startAnimation(flipAnimation);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });

    }

    @OnClick({R.id.card_front, R.id.card_back})
    public void setOnClicks(View view) {
        switch (view.getId()) {
            case R.id.card_front:
                flipCard(cardFront, cardBack);
                break;
            case R.id.card_back:
                flipCard(cardBack, cardFront);
                break;
        }
    }

}
