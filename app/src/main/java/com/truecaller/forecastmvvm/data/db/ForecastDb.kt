package com.truecaller.forecastmvvm.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.truecaller.forecastmvvm.data.db.entity.CurrentWeatherEntry
import com.truecaller.forecastmvvm.data.db.entity.WeatherLocation

@Database(
    entities = [CurrentWeatherEntry::class,WeatherLocation::class],
    version = 1
)
abstract class ForecastDb : RoomDatabase() {
    abstract fun currentWeatherDao(): CurrentWeatherDao
    abstract fun weatherlocationDaO():WeatherLocationDao

    companion object {
        @Volatile
        private var instance: ForecastDb? = null
        private val Lock = Any() //To avoid two instance at the same time by different threads

        operator fun invoke(context: Context) = instance ?: synchronized(Lock) {
            instance ?: buildDataBase(context).also {
                instance = it
            }
        }

        private fun buildDataBase(context: Context) = Room.databaseBuilder(
            context.applicationContext, ForecastDb::class.java,
            "forecast.db"
        ).build()

    }
}