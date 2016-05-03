package com.droid.manasshrestha.rxandroid.weathermodels;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class WeatherModel implements Parcelable {

    @SerializedName("latitude")
    private Double latitude;

    @SerializedName("longitude")
    private Double longitude;

    @SerializedName("timezone")
    private String timezone;

    @SerializedName("offset")
    private Double offset;

    @SerializedName("currently")
    private Currently currently;

    @SerializedName("hourly")
    private Hourly hourly;

    @SerializedName("daily")
    private Daily daily;

    @SerializedName("flags")
    private Flags flags;

    protected WeatherModel(Parcel in) {
        latitude = in.readDouble();
        longitude = in.readDouble();
        timezone = in.readString();
        offset = in.readDouble();
        currently = in.readParcelable(Currently.class.getClassLoader());
        hourly = in.readParcelable(Hourly.class.getClassLoader());
    }

    public static final Creator<WeatherModel> CREATOR = new Creator<WeatherModel>() {
        @Override
        public WeatherModel createFromParcel(Parcel in) {
            return new WeatherModel(in);
        }

        @Override
        public WeatherModel[] newArray(int size) {
            return new WeatherModel[size];
        }
    };

    /**
     * @return The latitude
     */
    public Double getLatitude() {
        return latitude;
    }

    /**
     * @param latitude The latitude
     */
    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    /**
     * @return The longitude
     */
    public Double getLongitude() {
        return longitude;
    }

    /**
     * @param longitude The longitude
     */
    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    /**
     * @return The timezone
     */
    public String getTimezone() {
        return timezone;
    }

    /**
     * @param timezone The timezone
     */
    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    /**
     * @return The offset
     */
    public Double getOffset() {
        return offset;
    }

    /**
     * @param offset The offset
     */
    public void setOffset(Double offset) {
        this.offset = offset;
    }

    /**
     * @return The currently
     */
    public Currently getCurrently() {
        return currently;
    }

    /**
     * @param currently The currently
     */
    public void setCurrently(Currently currently) {
        this.currently = currently;
    }

    /**
     * @return The hourly
     */
    public Hourly getHourly() {
        return hourly;
    }

    /**
     * @param hourly The hourly
     */
    public void setHourly(Hourly hourly) {
        this.hourly = hourly;
    }

    /**
     * @return The daily
     */
    public Daily getDaily() {
        return daily;
    }

    /**
     * @param daily The daily
     */
    public void setDaily(Daily daily) {
        this.daily = daily;
    }

    /**
     * @return The flags
     */
    public Flags getFlags() {
        return flags;
    }

    /**
     * @param flags The flags
     */
    public void setFlags(Flags flags) {
        this.flags = flags;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

    }

}


