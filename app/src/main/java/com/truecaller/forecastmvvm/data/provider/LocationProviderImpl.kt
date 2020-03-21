package com.truecaller.forecastmvvm.data.provider

import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.truecaller.forecastmvvm.data.db.entity.WeatherLocation
import com.truecaller.forecastmvvm.internal.NoLocationPermissionGrantedException
import com.truecaller.forecastmvvm.internal.asDeferred
import kotlinx.coroutines.Deferred

const val USE_DEVICE_LOCATION = "USE_DEVICE_LOCATION"
const val CUSTOM_LOCATION = "CUSTOM_LOCATION"

class LocationProviderImpl(
    context: Context,
    private val fusedLocationProviderClient: FusedLocationProviderClient
) : PreferenceProvider(context), LocationProvider {

    private val appContext = context.applicationContext
    override suspend fun hasLocationChanged(lastWeatherLocation: WeatherLocation): Boolean {

        val deviceLocationChanged = try {
            hasDeviceLocationChanged(lastWeatherLocation)
        } catch (e: Exception) {
            false
        }
        return deviceLocationChanged || hasCustomLocationChanged(lastWeatherLocation);
    }

    private suspend fun hasDeviceLocationChanged(lastWeatherLocation: WeatherLocation): Boolean {
        if (!isUsingDeviceLocation()) {
            return false
        }

        val deviceLocation = getLastDeviceLocation().await() ?: return false;

        val comparisonThreshold = 0.03
        return Math.abs(deviceLocation.latitude - lastWeatherLocation.lat) > comparisonThreshold
                && Math.abs(deviceLocation.longitude - lastWeatherLocation.lon) > comparisonThreshold

    }

    private fun hasCustomLocationChanged(lastWeatherLocation: WeatherLocation): Boolean {
        val customLocationName = getCustomLocationName()
        return customLocationName != lastWeatherLocation.name
    }

    private fun getLastDeviceLocation(): Deferred<Location?> {

        return if (hasLocationPermission()) {
            fusedLocationProviderClient.lastLocation.asDeferred()
        } else {
            throw NoLocationPermissionGrantedException()
        }
    }

    private fun hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            appContext,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun isUsingDeviceLocation(): Boolean {
        return preferences.getBoolean(USE_DEVICE_LOCATION, true)
    }

    private fun getCustomLocationName(): String? {
        return preferences.getString(CUSTOM_LOCATION, null)
    }


    override suspend fun getPreferredlocationString(): String {

        if (isUsingDeviceLocation()) {
            try {
                val deviceLocation = getLastDeviceLocation().await()
                    ?: return "${getCustomLocationName()}"
                return "${deviceLocation.latitude},${deviceLocation.longitude}"
            } catch (e: java.lang.Exception) {
                return "${getCustomLocationName()}"
            }
        } else {
            return "${getCustomLocationName()}" //to handle nullable string
        }
    }
}