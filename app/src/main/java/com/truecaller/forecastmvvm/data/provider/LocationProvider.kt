package com.truecaller.forecastmvvm.data.provider

import com.truecaller.forecastmvvm.data.db.entity.WeatherLocation

interface LocationProvider {
    suspend fun hasLocationChanged(lastWeatherLocation: WeatherLocation):Boolean
    suspend fun getPreferredlocationString():String
}