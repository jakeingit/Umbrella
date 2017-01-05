package com.nerdery.umbrella.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nerdery.umbrella.BuildConfig;
import com.nerdery.umbrella.api.parser.ForecastParser;
import com.nerdery.umbrella.model.ForecastCondition;
import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;

/**
 * Manages access to the various APIs we are using
 *
 * @author bherbst
 */
public class ApiManager {
    private static WeatherApi sApi;

    /**
     * Get a WeatherApi instance for access to Weather Underground's APIs
     */
    public static WeatherApi getWeatherApi() {
        if (sApi == null) {
            sApi = buildWeatherApi();
        }

        return sApi;
    }

    /**
     * Get an IconApi instance
     */
    public static IconApi getIconApi() {
        return new IconApi();
    }

    /**
     * Build a new WeatherApi
     */
    private static WeatherApi buildWeatherApi() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(ForecastCondition.class, new ForecastParser())
                .create();

        Retrofit adapter = new Retrofit.Builder()
                .baseUrl(BuildConfig.API_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        return adapter.create(WeatherApi.class);
    }
}
