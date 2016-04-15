package com.droid.manasshrestha.rxandroid.retrofit;


import com.droid.manasshrestha.rxandroid.textModels.Weath;

import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;
import rx.Observable;

/**
 * Contract for retrofit
 */
public interface RestApi {

    @GET("forecast/{apiKey}/{latlng}")
    Observable<Weath> getWeatherDaily(@Path("apiKey") String apiKey, @Path("latlng") String latLng,
                                      @Query("units") String units);

}
