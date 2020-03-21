package com.truecaller.forecastmvvm.data.network

import androidx.lifecycle.LiveData
import com.truecaller.forecastmvvm.data.network.Response.CurrentWeatherResponse

interface WeatherNetworkDataSource {
      val downloadedCurrentWeather:LiveData<CurrentWeatherResponse>

    suspend fun fetchCurrentWeather(
      location:String,
      languageCode:String
    ){

    }
}