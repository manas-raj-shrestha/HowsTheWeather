package com.howstheweather.weathermodels;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Hourly implements Parcelable {

    @SerializedName("summary")
    private String summary;

    @SerializedName("icon")
    private String icon;

    @SerializedName("data")
    private ArrayList<HourlyData> data = new ArrayList<HourlyData>();

    protected Hourly(Parcel in) {
        summary = in.readString();
        icon = in.readString();
        data = in.readArrayList(HourlyData.class.getClassLoader());
    }

    public static final Creator<Hourly> CREATOR = new Creator<Hourly>() {
        @Override
        public Hourly createFromParcel(Parcel in) {
            return new Hourly(in);
        }

        @Override
        public Hourly[] newArray(int size) {
            return new Hourly[size];
        }
    };

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
     * @return The data
     */
    public ArrayList<HourlyData> getData() {
        return data;
    }

    /**
     * @param data The data
     */
    public void setData(ArrayList<HourlyData> data) {
        this.data = data;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(summary);
        parcel.writeString(icon);
        parcel.writeList(data);
    }
}
