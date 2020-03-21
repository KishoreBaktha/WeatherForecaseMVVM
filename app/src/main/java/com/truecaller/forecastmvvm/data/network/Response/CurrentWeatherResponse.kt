package com.truecaller.forecastmvvm.data.network.Response

import com.google.gson.annotations.SerializedName
import com.truecaller.forecastmvvm.data.db.entity.CurrentWeatherEntry
import com.truecaller.forecastmvvm.data.db.entity.WeatherLocation
import com.truecaller.forecastmvvm.data.db.entity.Request


data class CurrentWeatherResponse(
    @SerializedName("current")
    val currentWeatherEntry: CurrentWeatherEntry,
    val location: WeatherLocation,
    val request: Request
)