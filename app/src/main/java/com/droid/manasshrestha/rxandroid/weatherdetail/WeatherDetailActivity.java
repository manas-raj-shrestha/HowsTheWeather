package com.droid.manasshrestha.rxandroid.weatherdetail;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.droid.manasshrestha.rxandroid.R;

/**
 * Weather detail view
 */
public class WeatherDetailActivity extends AppCompatActivity implements WeatherDetailContract.Views{

    TextView tvResult;
    WeatherDetailPresenter weatherDetailPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_card_fragment);
//        tvResult = (TextView) findViewById(R.id.tv_result);
        weatherDetailPresenter = new WeatherDetailPresenter(this);
    }

    public void get(View view) {
        weatherDetailPresenter.fetchWeather();
    }

    @Override
    public void showProgressBar() {

    }

    @Override
    public void hideProgressBar() {

    }

//    @Override
//    public void updateWeatherInfos(WeatherModel weatherModel) {
////        tvResult.setText(weatherModel.getName() + "\n" + weatherModel.getMain().getTemp());
//    }

}
