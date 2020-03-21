package com.truecaller.forecastmvvm.data.repository

import android.os.Build
import androidx.lifecycle.LiveData
import com.truecaller.forecastmvvm.data.db.CurrentWeatherDao
import com.truecaller.forecastmvvm.data.db.WeatherLocationDao
import com.truecaller.forecastmvvm.data.db.entity.WeatherLocation
import com.truecaller.forecastmvvm.data.db.unitlocalized.UnitSpecificCurrentWeatherEntry
import com.truecaller.forecastmvvm.data.network.Response.CurrentWeatherResponse
import com.truecaller.forecastmvvm.data.network.WeatherNetworkDataSource
import com.truecaller.forecastmvvm.data.provider.LocationProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class ForecastRepositoryImpl(
    private val currentWeatherDao: CurrentWeatherDao,
    private val weatherNetworkDataSource: WeatherNetworkDataSource,
    private val weatherLocationDao: WeatherLocationDao,
    private val locationProvider: LocationProvider
) : ForecastRepository {
    init {
        weatherNetworkDataSource.downloadedCurrentWeather.observeForever { newCurrentWeather ->
            persistFetchedCurrentWeather(newCurrentWeather)
        }
    }

    override suspend fun getCurrentWeather(metric: Boolean): LiveData<out UnitSpecificCurrentWeatherEntry> {
        initWeatherData()
        return withContext(Dispatchers.IO) {
            return@withContext currentWeatherDao.getWeatherImperial()
        }
    }

    override suspend fun getWeatherLocation(): LiveData<WeatherLocation> {
        return withContext(Dispatchers.IO) {
            return@withContext weatherLocationDao.getLocation()
        }
    }

    private fun persistFetchedCurrentWeather(fetchedWeather: CurrentWeatherResponse) {
        GlobalScope.launch(Dispatchers.IO) {
            currentWeatherDao.upsert(fetchedWeather.currentWeatherEntry)
            weatherLocationDao.upsert(fetchedWeather.location)
        }
    }

    private suspend fun initWeatherData() {

        val lastWeatherLocation = weatherLocationDao.getLocation().value

        if (lastWeatherLocation == null || locationProvider.hasLocationChanged(lastWeatherLocation)) {
            fetchCurrentWeather()
            return
        }
        if (isFetchCurrentNeeded(lastWeatherLocation.zonedDateTime)) {
            fetchCurrentWeather()
        }

    }

    private suspend fun fetchCurrentWeather() {
        weatherNetworkDataSource.fetchCurrentWeather(locationProvider.getPreferredlocationString(), Locale.getDefault().language)
    }
}

private fun isFetchCurrentNeeded(lastFetchTime: java.time.ZonedDateTime): Boolean {  //ZonedDateTime is backward compatible
    val thirtyMinutesAgo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        java.time.ZonedDateTime.now().minusMinutes(30)
    } else {
        TODO("VERSION.SDK_INT < O")
    }
    return lastFetchTime.isBefore(thirtyMinutesAgo)
}