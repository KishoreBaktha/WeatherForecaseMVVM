package com.truecaller.forecastmvvm.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.truecaller.forecastmvvm.data.db.entity.CURRENT_WEATHER_ID
import com.truecaller.forecastmvvm.data.db.entity.CurrentWeatherEntry
import com.truecaller.forecastmvvm.data.db.unitlocalized.ImperialCurrentWeatherEntry

@Dao
interface CurrentWeatherDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(currentWeatherEntry: CurrentWeatherEntry)

    @Query(value = "select * from current_weather where id = $CURRENT_WEATHER_ID")
    fun getWeatherImperial(): LiveData<ImperialCurrentWeatherEntry>
}