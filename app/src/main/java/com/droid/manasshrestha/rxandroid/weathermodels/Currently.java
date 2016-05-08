package com.droid.manasshrestha.rxandroid.weathermodels;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Currently implements Parcelable {

    @SerializedName("time")
    private Long timeInUnix;

    @SerializedName("summary")
    private String summary;

    @SerializedName("icon")
    private String icon;

    @SerializedName("precipIntensity")
    private Double precipIntensity;

    @SerializedName("precipProbability")
    private Double precipProbability;

    @SerializedName("temperature")
    private Double temperature;

    @SerializedName("apparentTemperature")
    private Double apparentTemperature;

    @SerializedName("dewPoint")
    private Double dewPoint;

    @SerializedName("humidity")
    private Double humidity;

    @SerializedName("windSpeed")
    private Double windSpeed;

    @SerializedName("windBearing")
    private Double windBearing;

    @SerializedName("cloudCover")
    private Double cloudCover;

    @SerializedName("pressure")
    private Double pressure;

    @SerializedName("ozone")
    private Double ozone;

    protected Currently(Parcel in) {
        timeInUnix = in.readLong();
        summary = in.readString();
        icon = in.readString();
        precipIntensity = in.readDouble();
        precipProbability= in.readDouble();
        temperature= in.readDouble();
        apparentTemperature= in.readDouble();
        dewPoint= in.readDouble();
        humidity= in.readDouble();
        windSpeed= in.readDouble();
        windBearing= in.readDouble();
        cloudCover= in.readDouble();
        pressure= in.readDouble();
        ozone= in.readDouble();
    }

    public static final Creator<Currently> CREATOR = new Creator<Currently>() {
        @Override
        public Currently createFromParcel(Parcel in) {
            return new Currently(in);
        }

        @Override
        public Currently[] newArray(int size) {
            return new Currently[size];
        }
    };

    /**
     * @return The timeInUnix
     */
    public Long getTimeInUnix() {
        return timeInUnix;
    }

    /**
     * @param timeInUnix The timeInUnix
     */
    public void setTimeInUnix(Long timeInUnix) {
        this.timeInUnix = timeInUnix;
    }

    /**
     * @return The summary
     */
    public String getSummary() {
        return summary;
    }

    /**
     * @param summary The summary
     */
    public void setSummary(String summary) {
        this.summary = summary;
    }

    /**
     * @return The icon
     */
    public String getIcon() {
        return icon;
    }

    /**
     * @param icon The icon
     */
    public void setIcon(String icon) {
        this.icon = icon;
    }

    /**
     * @return The precipIntensity
     */
    public Double getPrecipIntensity() {
        return precipIntensity;
    }

    /**
     * @param precipIntensity The precipIntensity
     */
    public void setPrecipIntensity(Double precipIntensity) {
        this.precipIntensity = precipIntensity;
    }

    /**
     * @return The precipProbability
     */
    public Double getPrecipProbability() {
        return precipProbability;
    }

    /**
     * @param precipProbability The precipProbability
     */
    public void setPrecipProbability(Double precipProbability) {
        this.precipProbability = precipProbability;
    }

    /**
     * @return The temperature
     */
    public Double getTemperature() {
        return temperature;
    }

    /**
     * @param temperature The temperature
     */
    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    /**
     * @return The apparentTemperature
     */
    public Double getApparentTemperature() {
        return apparentTemperature;
    }

    /**
     * @param apparentTemperature The apparentTemperature
     */
    public void setApparentTemperature(Double apparentTemperature) {
        this.apparentTemperature = apparentTemperature;
    }

    /**
     * @return The dewPoint
     */
    public Double getDewPoint() {
        return dewPoint;
    }

    /**
     * @param dewPoint The dewPoint
     */
    public void setDewPoint(Double dewPoint) {
        this.dewPoint = dewPoint;
    }

    /**
     * @return The humidity
     */
    public Double getHumidity() {
        return humidity;
    }

    /**
     * @param humidity The humidity
     */
    public void setHumidity(Double humidity) {
        this.humidity = humidity;
    }

    /**
     * @return The windSpeed
     */
    public Double getWindSpeed() {
        return windSpeed;
    }

    /**
     * @param windSpeed The windSpeed
     */
    public void setWindSpeed(Double windSpeed) {
        this.windSpeed = windSpeed;
    }

    /**
     * @return The windBearing
     */
    public Double getWindBearing() {
        return windBearing;
    }

    /**
     * @param windBearing The windBearing
     */
    public void setWindBearing(Double windBearing) {
        this.windBearing = windBearing;
    }

    /**
     * @return The cloudCover
     */
    public Double getCloudCover() {
        return cloudCover;
    }

    /**
     * @param cloudCover The cloudCover
     */
    public void setCloudCover(Double cloudCover) {
        this.cloudCover = cloudCover;
    }

    /**
     * @return The pressure
     */
    public Double getPressure() {
        return pressure;
    }

    /**
     * @param pressure The pressure
     */
    public void setPressure(Double pressure) {
        this.pressure = pressure;
    }

    /**
     * @return The ozone
     */
    public Double getOzone() {
        return ozone;
    }

    /**
     * @param ozone The ozone
     */
    public void setOzone(Double ozone) {
        this.ozone = ozone;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(timeInUnix);
        parcel.writeString(summary);
        parcel.writeString(icon);
        parcel.writeDouble(precipIntensity);
        parcel.writeDouble(precipProbability);
        parcel.writeDouble(temperature);
        parcel.writeDouble(apparentTemperature);
        parcel.writeDouble(dewPoint);
        parcel.writeDouble(humidity);
        parcel.writeDouble(windSpeed);
        parcel.writeDouble(windBearing);
        parcel.writeDouble(cloudCover);
        parcel.writeDouble(pressure);
        parcel.writeDouble(ozone);
    }
}