package com.truecaller.forecastmvvm.data.provider

import com.truecaller.forecastmvvm.internal.UnitSystem

interface UnitProvider {
    fun getUnitSystem():UnitSystem
}