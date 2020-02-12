package com.howstheweather.weathermodels;

public class Temp {

    private Double maxTemp;

    private Double minTemp;

    private String maxTempTime;

    private String minTempTime;


    public Double getMaxTemp() {
        return maxTemp;
    }

    public void setMaxTemp(Double maxTemp) {
        this.maxTemp = maxTemp;
    }

    public Double getMinTemp() {
        return minTemp;
    }

    public void setMinTemp(Double minTemp) {
        this.minTemp = minTemp;
    }

    public String getMaxTempTime() {
        return maxTempTime;
    }

    public void setMaxTempTime(String maxTempTime) {
        this.maxTempTime = maxTempTime;
    }

    public String getMinTempTime() {
        return minTempTime;
    }

    public void setMinTempTime(String minTempTime) {
        this.minTempTime = minTempTime;
    }
}