package com.droid.manasshrestha.rxandroid.weathermodels;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Flags implements Parcelable{

    @SerializedName("sources")
    private List<String> sources = new ArrayList<String>();

    @SerializedName("isd-stations")
    private List<String> isdStations = new ArrayList<String>();

    @SerializedName("units")
    private String units;

    protected Flags(Parcel in) {
        sources = in.createStringArrayList();
        isdStations = in.createStringArrayList();
        units = in.readString();
    }

    public static final Creator<Flags> CREATOR = new Creator<Flags>() {
        @Override
        public Flags createFromParcel(Parcel in) {
            return new Flags(in);
        }

        @Override
        public Flags[] newArray(int size) {
            return new Flags[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeStringList(sources);
        parcel.writeStringList(isdStations);
        parcel.writeString(units);
    }
}