package com.droid.manasshrestha.rxandroid.weathermodels;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Daily {

    @SerializedName("data")
    private List<Datu> data = new ArrayList<Datu>();

    /**
     * @return The data
     */
    public List<Datu> getData() {
        return data;
    }

    /**
     * @param data The data
     */
    public void setData(List<Datu> data) {
        this.data = data;
    }

}