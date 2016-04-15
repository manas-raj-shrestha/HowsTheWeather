package com.droid.manasshrestha.rxandroid.weathercards;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.format.DateFormat;

import com.droid.manasshrestha.rxandroid.R;
import com.droid.manasshrestha.rxandroid.weathermodels.HourlyData;
import com.droid.manasshrestha.rxandroid.weathermodels.Temp;
import com.droid.manasshrestha.rxandroid.weathermodels.WeatherModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;

/**
 * Presenter for weather cards
 */
public class WeatherCardPresenter {

    WeatherCardContract.Views weatherCardContract;
    WeatherModel forecastList;
    Context context;

    public WeatherCardPresenter(Context context, WeatherCardContract.Views weatherCardContract, WeatherModel forecastList) {
        this.context = context;
        this.weatherCardContract = weatherCardContract;
        this.forecastList = forecastList;
    }

    /**
     * process and set data to the views
     */
    public void setData() {
        parseDate();
        pickWeatherIcon();
        setAverageTemperature();
        setCardBackground();
        setHumidity();
        setClouds();
        setTemperatures();
        setDataToChart();


        weatherCardContract.setAtmosphericPressure(forecastList.getDaily().getData().get(0).getPressure() + "hpa");
        weatherCardContract.setWindSpeed(forecastList.getDaily().getData().get(0).getWindSpeed() + "m/s");
        weatherCardContract.setWindDirection(forecastList.getDaily().getData().get(0).getWindBearing() + "degree");

        weatherCardContract.setWeatherDesc(forecastList.getDaily().getData().get(0).getSummary());
    }

    /**
     * Sets data to temperature-time chart
     */
    private void setDataToChart() {
        ArrayList<HourlyData> arrayList = forecastList.getHourly().getData();

        List<PointValue> values = new ArrayList<>();
        for (int i = 0; i < arrayList.size(); i = i + 2) {
            values.add(new PointValue(i, arrayList.get(i).getTemperature().floatValue()));
        }

        //In most cased you can call data model methods in builder-pattern-like manner.
        Line line = new Line(values).setColor(Color.WHITE).setCubic(false).setHasLabelsOnlyForSelected(true);
        List<Line> lines = new ArrayList<>();
        lines.add(line);

        LineChartData data = new LineChartData();
        data.setLines(lines);

        Axis axisX = new Axis();
        Axis axisY = new Axis().setHasLines(true);

        axisX.setName("Time");
        axisY.setName("Temperature");
        data.setAxisXBottom(axisX);
        data.setAxisYLeft(axisY);

        weatherCardContract.setLineSetData(data);
    }

    /**
     * Fill the temperature table at the back of weather card
     */
    private void setTemperatures() {
        Temp temp = new Temp();
        temp.setMaxTemp(forecastList.getDaily().getData().get(0).getTemperatureMax());
        temp.setMinTemp(forecastList.getDaily().getData().get(0).getTemperatureMin());

        temp.setMaxTempTime(parseTime(forecastList.getDaily().getData().get(0).getTemperatureMinTime()));
        temp.setMinTempTime(parseTime(forecastList.getDaily().getData().get(0).getTemperatureMaxTime()));

        weatherCardContract.setTemperature(temp);
    }

    /**
     * Calculate cloud percentage
     */
    private void setClouds() {
        Double d = new Double(forecastList.getDaily().getData().get(0).getCloudCover() * 100);
        weatherCardContract.setClouds(d.intValue());
    }

    /**
     * Calculate humidity percentage
     */
    private void setHumidity() {
        Double d = new Double(forecastList.getDaily().getData().get(0).getHumidity() * 100);
        weatherCardContract.setHumidity(d.intValue());
    }

    /**
     * select background color according to weather code id
     */
    private void setCardBackground() {
        String weatherCondition = forecastList.getDaily().getData().get(0).getIcon();
        int colorId = Color.parseColor("#ffeb3b");
        switch (weatherCondition) {
            case "clear-day":
            case "clear-night":
                colorId = Color.parseColor("#ffeb3b");
                break;
            case "rain":
                colorId = Color.parseColor("#3F51B5");
                break;
            case "snow":
                colorId = Color.parseColor("#607d8b");
                break;
            case "sleet":
                colorId = Color.parseColor("#607d8b");
                break;
            case "wind":
                colorId = Color.parseColor("#607d8b");
                break;
            case "fog":
                colorId = Color.parseColor("#607d8b");
                break;
            case "cloudy":
                colorId = Color.parseColor("#607d8b");
                break;
            case "partly-cloudy-day":
            case "partly-cloudy-night":
                colorId = Color.parseColor("#607d8b");
                break;
        }

        weatherCardContract.setCardBackground(colorId);
    }

    /**
     * calculate average temperature
     */
    private void setAverageTemperature() {
        int averageTemp = (int) (forecastList.getDaily().getData().get(0).getTemperatureMin() +
                forecastList.getDaily().getData().get(0).getTemperatureMax()) / 2;
        weatherCardContract.setAvgTemp(averageTemp);
    }

    /**
     * pick weather icon from weather id
     */
    private void pickWeatherIcon() {
        String weatherCondition = forecastList.getDaily().getData().get(0).getIcon();
//        Drawable weatherIcon = ContextCompat.getDrawable(context, R.drawable.sunny);

//        switch (weatherCondition) {
//            case "clear-day":
//            case "clear-night":
//                weatherIcon = ContextCompat.getDrawable(context, R.drawable.sunny);
//                break;
//            case "rain":
//                weatherIcon = ContextCompat.getDrawable(context, R.drawable.rainy);
//                break;
//            case "snow":
//                weatherIcon = ContextCompat.getDrawable(context, R.drawable.snow);
//                break;
//            case "sleet":
//                weatherIcon = ContextCompat.getDrawable(context, R.drawable.sleet);
//                break;
//            case "wind":
//                weatherIcon = ContextCompat.getDrawable(context, R.drawable.wind);
//                break;
//            case "fog":
//                weatherIcon = ContextCompat.getDrawable(context, R.drawable.fog);
//                break;
//            case "cloudy":
//                weatherIcon = ContextCompat.getDrawable(context, R.drawable.clouds);
//                break;
//            case "partly-cloudy-day":
//            case "partly-cloudy-night":
//                weatherIcon = ContextCompat.getDrawable(context, R.drawable.partly_cloudy);
//                break;
//        }

        weatherCardContract.setWeatherIcon(null);
    }

    /**
     * convert long date to readable format
     */
    private void parseDate() {
        String longV = String.valueOf(forecastList.getDaily().getData().get(0).getTime() * 1000);
        long millisecond = Long.parseLong(longV);
        String dayString = DateFormat.format("EEEE", new Date(millisecond)).toString();
        String dateString = DateFormat.format("MM/dd/yyyy", new Date(millisecond)).toString();

        weatherCardContract.setWeekDay(dayString.toUpperCase());
    }

    /**
     * convert long time to readable format
     */
    private String parseTime(long unixTime) {
        String longV = String.valueOf(unixTime * 1000);
        long millisecond = Long.parseLong(longV);
        String timeString = DateFormat.format("HH:mm", new Date(millisecond)).toString();

        return timeString;
    }

}
