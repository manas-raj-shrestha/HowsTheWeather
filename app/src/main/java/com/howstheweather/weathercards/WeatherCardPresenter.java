package com.howstheweather.weathercards;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.ViewGroup;

import com.howstheweather.GeneralUtils;
import com.howstheweather.R;
import com.howstheweather.animatedicons.ClearWeather;
import com.howstheweather.animatedicons.CloudyWeather;
import com.howstheweather.animatedicons.FogWeather;
import com.howstheweather.animatedicons.PartlyCloudyWeather;
import com.howstheweather.animatedicons.RainyWeather;
import com.howstheweather.animatedicons.SleetWeather;
import com.howstheweather.animatedicons.SnowWeather;
import com.howstheweather.animatedicons.WindWeather;
import com.howstheweather.data.Constants;
import com.howstheweather.weathermodels.DailyData;
import com.howstheweather.weathermodels.HourlyData;
import com.howstheweather.weathermodels.Temp;
import com.howstheweather.weathermodels.WeatherModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;

/**
 * Presenter for weather cards
 */
public class WeatherCardPresenter {

    private static final String DAY_FORMAT = "EEEE";
    private static final String TIME_FORMAT = "HH:mm";
    private static final String DATE_FORMAT = "dd/MM/yyyy";
    private static final int MS_CONSTANT = 1000;

    private WeatherCardContract.Views weatherCardContract;
    private WeatherModel forecastList;
    private DailyData dailyData;
    private Context context;

    public WeatherCardPresenter(Context context, WeatherCardContract.Views weatherCardContract, WeatherModel forecastList) {
        this.context = context;
        this.weatherCardContract = weatherCardContract;
        this.forecastList = forecastList;
        dailyData = forecastList.getDaily().getData().get(0);
    }

    /**
     * process and set data to the views
     */
    public void setData() {

        setWeatherVariants();

        parseDay();
        setAverageTemperature();
        setHumidity();
        setClouds();
        setTemperatures();
        setDataToChart();

        weatherCardContract.setAtmosphericPressure(dailyData.getPressure() + "hpa");
        weatherCardContract.setWindSpeed(dailyData.getWindSpeed() + "m/s");
        weatherCardContract.setWindDirection(dailyData.getWindBearing() + "degree");

        weatherCardContract.setWeatherDesc(dailyData.getSummary());
        weatherCardContract.setDate(GeneralUtils.parseDate(DATE_FORMAT, dailyData.getTime()));
    }

    private void setWeatherVariants() {
        String weatherCondition = dailyData.getIcon();
        int colorId = Color.parseColor("#ffeb3b");
        int drawableId = R.drawable.clearsky_bg;
        String weatherTicker = "";
        ViewGroup viewGroup = null;

        switch (weatherCondition) {
            case Constants.KEY_CLEAR_DAY:
            case Constants.KEY_CLEAR_NIGHT:
                colorId = ContextCompat.getColor(context, R.color.colorClear);
                drawableId = R.drawable.clearsky_bg;
                viewGroup = new ClearWeather(context);
                weatherTicker = context.getString(R.string.txt_clear);
                break;
            case Constants.KEY_RAIN:
                colorId = ContextCompat.getColor(context, R.color.colorRain);
                drawableId = R.drawable.rainy_bg;
                viewGroup = new RainyWeather(context);
                weatherTicker = context.getString(R.string.txt_rainy);
                break;
            case Constants.KEY_SNOW:
                colorId = ContextCompat.getColor(context, R.color.colorSnow);
                drawableId = R.drawable.snow_bg;
                viewGroup = new SnowWeather(context);
                weatherTicker = context.getString(R.string.txt_snow);
                break;
            case Constants.KEY_SLEET:
                colorId = ContextCompat.getColor(context, R.color.colorSleet);
                drawableId = R.drawable.sleet_bg;
                viewGroup = new SleetWeather(context);
                weatherTicker = context.getString(R.string.txt_sleet);
                break;
            case Constants.KEY_WIND:
                colorId = ContextCompat.getColor(context, R.color.colorWind);
                drawableId = R.drawable.clearsky_bg;
                viewGroup = new WindWeather(context);
                weatherTicker = context.getString(R.string.txt_windy);
                break;
            case Constants.KEY_FOG:
                colorId = ContextCompat.getColor(context, R.color.colorFog);
                drawableId = R.drawable.fog_bg;
                viewGroup = new FogWeather(context);
                weatherTicker = context.getString(R.string.txt_foggy);
                break;
            case Constants.KEY_CLOUDY:
                colorId = ContextCompat.getColor(context, R.color.colorCloudy);
                drawableId = R.drawable.cloudy_bg;
                viewGroup = new CloudyWeather(context);
                weatherTicker = context.getString(R.string.txt_cloudy);
                break;
            case Constants.KEY_PARTLY_CLOUDY_DAY:
            case Constants.KEY_PARTLY_CLOUDY_NIGHT:
                colorId = ContextCompat.getColor(context, R.color.colorPartlyCloudy);
                drawableId = R.drawable.partialcloud_bg;
                viewGroup = new PartlyCloudyWeather(context);
                weatherTicker = context.getString(R.string.txt_partly_cloudy);
                break;
        }

        weatherCardContract.setCardBackground(colorId, drawableId);
        weatherCardContract.setWeatherIcon(viewGroup);
        weatherCardContract.setWeatherTicker(weatherTicker);
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

        axisX.setName(context.getString(R.string.txt_time));
        axisY.setName(context.getString(R.string.txt_temperature));
        data.setAxisXBottom(axisX);
        data.setAxisYLeft(axisY);

        weatherCardContract.setLineSetData(data);
    }

    /**
     * Fill the temperature table at the back of weather card
     */
    private void setTemperatures() {
        Temp temp = new Temp();
        temp.setMaxTemp(dailyData.getTemperatureMax());
        temp.setMinTemp(dailyData.getTemperatureMin());

        temp.setMaxTempTime(GeneralUtils.parseDate(TIME_FORMAT, dailyData.getTemperatureMinTime()));
        temp.setMinTempTime(GeneralUtils.parseDate(TIME_FORMAT, dailyData.getTemperatureMaxTime()));

        weatherCardContract.setTemperature(temp);
    }

    /**
     * Calculate cloud percentage
     */
    private void setClouds() {
        Double clouds = new Double(dailyData.getCloudCover() * 100);
        weatherCardContract.setClouds(clouds.intValue());
    }

    /**
     * Calculate humidity percentage
     */
    private void setHumidity() {
        Double humidity = new Double(dailyData.getHumidity() * 100);
        weatherCardContract.setHumidity(humidity.intValue());
    }

    /**
     * calculate average temperature
     */
    private void setAverageTemperature() {
        int averageTemp = (int) (dailyData.getTemperatureMin() +
                dailyData.getTemperatureMax()) / 2;
        weatherCardContract.setAvgTemp(averageTemp);
    }

    /**
     * convert long date to readable format
     */
    private void parseDay() {
        String longV = String.valueOf(dailyData.getTime() * MS_CONSTANT);
        long millisecond = Long.parseLong(longV);
        String dayString = DateFormat.format(DAY_FORMAT, new Date(millisecond)).toString();

        Date date = new Date(Long.parseLong(longV));
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);

        Log.e("time", millisecond + " " + System.currentTimeMillis() + " " + calendar.get(Calendar.DAY_OF_MONTH));

        if (calendar.get(Calendar.DAY_OF_MONTH) == Calendar.getInstance().get(Calendar.DAY_OF_MONTH)) {
            weatherCardContract.setWeekDay("TODAY");
        } else if (calendar.get(Calendar.DAY_OF_MONTH) == (Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + 1)) {
            weatherCardContract.setWeekDay("TOMORROW");
        } else {
            weatherCardContract.setWeekDay(dayString.toUpperCase());
        }
    }

}
