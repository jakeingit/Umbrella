package com.nerdery.umbrella.api;

import com.nerdery.umbrella.BuildConfig;
import com.nerdery.umbrella.model.WeatherData;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.Call;

/**
 * Retrofit interface for fetching weather data
 *
 * @author bherbst
 */
public interface WeatherApi {

    /**
     * Get the forecast for a given zip code
     */
    @GET("/api/" + BuildConfig.API_KEY + "/conditions/hourly/q/{zip}.json")
    Call<WeatherData> getForecastForZip(@Path("zip") int zipCode);
}
