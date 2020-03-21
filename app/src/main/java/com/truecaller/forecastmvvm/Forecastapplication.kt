package com.truecaller.forecastmvvm

import android.app.Application
import androidx.preference.PreferenceManager
import com.google.android.gms.location.LocationServices
import com.jakewharton.threetenabp.AndroidThreeTen
import com.truecaller.forecastmvvm.data.db.ForecastDb
import com.truecaller.forecastmvvm.data.network.*
import com.truecaller.forecastmvvm.data.provider.LocationProvider
import com.truecaller.forecastmvvm.data.provider.LocationProviderImpl
import com.truecaller.forecastmvvm.data.provider.UnitProvider
import com.truecaller.forecastmvvm.data.provider.UnitProviderImpl
import com.truecaller.forecastmvvm.data.repository.ForecastRepository
import com.truecaller.forecastmvvm.data.repository.ForecastRepositoryImpl
import com.truecaller.forecastmvvm.ui.weather.current.CurrentWeatherViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.androidModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

class Forecastapplication :Application(),KodeinAware
{
    override val kodein: Kodein = Kodein.lazy {
        import(androidModule(this@Forecastapplication)) //provide instances of context,services

        bind() from singleton { ForecastDb(instance()) }
        bind() from singleton { instance<ForecastDb>().currentWeatherDao() }
        bind() from singleton { instance<ForecastDb>().weatherlocationDaO() }
        bind<ConnectivityInterceptor>() with singleton { ConnectivityInterceptorImpl(instance()) }
        bind() from singleton { WeatherApiService(instance()) }
        bind<WeatherNetworkDataSource>() with singleton { WeatherNetworkDataSourceImpl(instance()) }
        bind<LocationProvider>() with singleton { LocationProviderImpl(instance(),instance()) }
        bind<ForecastRepository>() with singleton { ForecastRepositoryImpl(instance(),instance(),instance(),instance()) }
        bind<UnitProvider>() with singleton { UnitProviderImpl(instance()) }
        bind() from provider { CurrentWeatherViewModelFactory(instance(),instance()) }
        bind() from provider { LocationServices.getFusedLocationProviderClient(instance()) }
    }

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
        PreferenceManager.setDefaultValues(this,R.xml.preferences,false) //get default values from xml file
    }
}