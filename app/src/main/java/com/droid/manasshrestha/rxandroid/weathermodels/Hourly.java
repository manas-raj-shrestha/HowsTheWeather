package com.droid.manasshrestha.rxandroid.weathermodels;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Hourly {

    @SerializedName("summary")
    private String summary;

    @SerializedName("icon")
    private String icon;

    @SerializedName("data")
    private ArrayList<HourlyData> data = new ArrayList<HourlyData>();

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

}
