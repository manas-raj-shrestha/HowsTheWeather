package com.howstheweather.weathermodels;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Daily implements Parcelable {

    @SerializedName("data")
    private List<DailyData> data = new ArrayList<DailyData>();

    protected Daily(Parcel in) {
        data = in.readArrayList(DailyData.class.getClassLoader());
    }

    public static final Creator<Daily> CREATOR = new Creator<Daily>() {
        @Override
        public Daily createFromParcel(Parcel in) {
            return new Daily(in);
        }

        @Override
        public Daily[] newArray(int size) {
            return new Daily[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeList(data);
    }
}