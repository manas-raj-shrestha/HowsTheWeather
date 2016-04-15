package com.droid.manasshrestha.rxandroid.textModels;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Hourly {

    @SerializedName("summary")
    private String summary;

    @SerializedName("icon")
    private String icon;

    @SerializedName("data")
    private ArrayList<Datum> data = new ArrayList<Datum>();

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
    public ArrayList<Datum> getData() {
        return data;
    }

    /**
     * @param data The data
     */
    public void setData(ArrayList<Datum> data) {
        this.data = data;
    }

}
