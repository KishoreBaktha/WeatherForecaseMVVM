package com.truecaller.forecastmvvm.data.repository

import androidx.lifecycle.LiveData
import com.truecaller.forecastmvvm.data.db.WeatherLocationDao
import com.truecaller.forecastmvvm.data.db.entity.WeatherLocation
import com.truecaller.forecastmvvm.data.db.unitlocalized.UnitSpecificCurrentWeatherEntry

interface ForecastRepository {
    suspend fun getCurrentWeather(metric:Boolean) : LiveData< out UnitSpecificCurrentWeatherEntry>
    suspend fun getWeatherLocation():LiveData<WeatherLocation>
}