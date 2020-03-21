package com.truecaller.forecastmvvm.ui.weather.current

import androidx.lifecycle.ViewModel
import com.truecaller.forecastmvvm.data.provider.UnitProvider
import com.truecaller.forecastmvvm.data.repository.ForecastRepository
import com.truecaller.forecastmvvm.internal.UnitSystem
import com.truecaller.forecastmvvm.internal.lazyDeferred

class CurrentWeatherViewModel(
    private val forecastRepository: ForecastRepository,
    unitProvider: UnitProvider
) : ViewModel() {

    private val unitSystem = unitProvider.getUnitSystem()

    public val isMetric: Boolean get() = unitSystem == UnitSystem.METRIC
    val weather by lazyDeferred {
        forecastRepository.getCurrentWeather(isMetric)
    }
    val weatherLocation by lazyDeferred {
        forecastRepository.getWeatherLocation()
    }

    // TODO: Implement the ViewModel
}
