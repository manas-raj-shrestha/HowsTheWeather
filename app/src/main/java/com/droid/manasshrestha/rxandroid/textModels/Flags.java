package com.droid.manasshrestha.rxandroid.textModels;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Flags {

    @SerializedName("sources")
    private List<String> sources = new ArrayList<String>();

    @SerializedName("isd-stations")
    private List<String> isdStations = new ArrayList<String>();

    @SerializedName("units")
    private String units;

    /**
     * @return The sources
     */
    public List<String> getSources() {
        return sources;
    }

    /**
     * @param sources The sources
     */
    public void setSources(List<String> sources) {
        this.sources = sources;
    }

    /**
     * @return The isdStations
     */
    public List<String> getIsdStations() {
        return isdStations;
    }

    /**
     * @param isdStations The isd-stations
     */
    public void setIsdStations(List<String> isdStations) {
        this.isdStations = isdStations;
    }

    /**
     * @return The units
     */
    public String getUnits() {
        return units;
    }

    /**
     * @param units The units
     */
    public void setUnits(String units) {
        this.units = units;
    }

}