package com.truecaller.forecastmvvm.data.db.unitlocalized

import androidx.room.ColumnInfo

data class ImperialCurrentWeatherEntry(
    @ColumnInfo(name = "cloudcover")
    override val CoverCloud: Int,
    @ColumnInfo(name = "feelslike")
    override val howitFeels: Int,
    override val humidity: Int
) : UnitSpecificCurrentWeatherEntry {

}