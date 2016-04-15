package com.droid.manasshrestha.rxandroid.weathermodels;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Daily {

    @SerializedName("data")
    private List<DailyData> data = new ArrayList<DailyData>();

    /**
     * @return The data
     */
    public List<DailyData> getData() {
        return data;
    }

    /**
     * @param data The data
     */
    public void setData(List<DailyData> data) {
        this.data = data;
    }

}