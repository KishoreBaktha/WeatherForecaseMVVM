package com.truecaller.forecastmvvm.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.truecaller.forecastmvvm.data.db.entity.WEATHER_LOCATION_ID
import com.truecaller.forecastmvvm.data.db.entity.WeatherLocation


@Dao
interface WeatherLocationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(weatherLocation: WeatherLocation) //update or insert

    @Query("select * from weather_location where id = $WEATHER_LOCATION_ID")
    fun getLocation():LiveData<WeatherLocation>
}