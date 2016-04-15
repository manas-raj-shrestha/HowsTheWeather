package com.droid.manasshrestha.rxandroid.weathercards;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.droid.manasshrestha.rxandroid.R;
import com.droid.manasshrestha.rxandroid.textModels.Temp;
import com.droid.manasshrestha.rxandroid.textModels.Weath;

import java.lang.reflect.Field;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.view.LineChartView;

/**
 * Weather card fragment
 */
public class WeatherCardsFragment extends Fragment implements WeatherCardContract.Views {

    @Bind(R.id.tv_week_day)
    TextView tvWeekDay;

    @Bind(R.id.tv_avg_temp)
    TextView tvAvgTemp;

    @Bind(R.id.iv_weather_icon)
    ImageView ivWeatherIcon;

    @Bind(R.id.tv_humidity)
    TextView tvHumidity;

    @Bind(R.id.tv_clouds)
    TextView tvClouds;

    @Bind(R.id.ll_card_front)
    LinearLayout cardContainerFront;

    @Bind(R.id.ll_card_back)
    LinearLayout cardContainerBack;

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

    Weath forecastList;
    WeatherCardPresenter weatherCardPresenter;

    public WeatherCardsFragment(Weath forecastList) {
        this.forecastList = forecastList;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.weather_card_fragment, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        weatherCardPresenter = new WeatherCardPresenter(getActivity(), this, forecastList);
        weatherCardPresenter.setData();
    }

    @Override
    public void setWeekDay(String weekDay) {
        tvWeekDay.setText(weekDay);
    }

    @Override
    public void setWeatherIcon(Drawable drawable) {
        ivWeatherIcon.setImageDrawable(drawable);
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
    public void setCardBackground(int colorId) {
        cardContainerFront.setBackgroundColor(colorId);
        cardContainerBack.setBackgroundColor(colorId);
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
        lineChartView.setInteractive(true);
    }

    private void flipCard(View currentView, View nextView) {

        FlipAnimation flipAnimation = new FlipAnimation(currentView, nextView);

        if (currentView.getVisibility() == View.GONE) {
            flipAnimation.reverse();
        }

        rvMainRoot.startAnimation(flipAnimation);
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
