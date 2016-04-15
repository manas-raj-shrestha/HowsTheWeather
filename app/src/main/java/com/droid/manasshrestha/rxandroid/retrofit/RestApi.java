package com.droid.manasshrestha.rxandroid.retrofit;


import com.droid.manasshrestha.rxandroid.weathermodels.WeatherModel;

import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;
import rx.Observable;

/**
 * Contract for retrofit
 */
public interface RestApi {

    @GET("forecast/{apiKey}/{latlng}")
    Observable<WeatherModel> getWeatherDaily(@Path("apiKey") String apiKey, @Path("latlng") String latLng,
                                      @Query("units") String units);

}
