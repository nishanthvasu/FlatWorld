package com.flatworld;

import com.flatworld.HomeScreen.DataModel.NewsData;
import com.flatworld.HomeScreen.DataModel.WeatherData;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Url;

/**
 * Created by Nishanth on 4/6/16.
 */
@SuppressWarnings("ALL")
public interface RetrofitAPICall {

    /*
     * Retrofit get annotation with our URL
     */

    @GET("everything?q=bitcoin&from=2019-01-30&sortBy=publishedAt&apiKey=0eef29b9b22e4e9ea70b79246b34f063")
    Call<NewsData> getNewsList(@Header("Authorization") String basic);

    @GET("c0573aaa4236de07ea016c388359affa/12.8345363,77.3498897")
    Call<WeatherData> getWeather(@Header("Authorization") String basic);

}