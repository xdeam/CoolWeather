package com.coolweather.android.http.service

import com.coolweather.android.gson.mx.MWeatherInfo
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface WeatherService {
    @GET("/app/weather/listWeather")
    fun getMWeather(
        @Query("cityIds") cityid: String
    ): Call<MWeatherInfo>
}