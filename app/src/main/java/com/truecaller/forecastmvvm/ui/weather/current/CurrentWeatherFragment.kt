package com.truecaller.forecastmvvm.ui.weather.current

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.truecaller.forecastmvvm.R
import com.truecaller.forecastmvvm.internal.glide.GlideApp
import com.truecaller.forecastmvvm.ui.base.ScopedFragment
import kotlinx.android.synthetic.main.current_weather_fragment.*
import kotlinx.coroutines.launch
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

class CurrentWeatherFragment : ScopedFragment(), KodeinAware {

    override val kodein: Kodein by closestKodein()

    private val viewModelFactory: CurrentWeatherViewModelFactory by instance()

    private lateinit var viewModel: CurrentWeatherViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.current_weather_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(CurrentWeatherViewModel::class.java)

        bindUI()


//        val apiService = WeatherApiService(ConnectivityInterceptorImpl(this.context!!))
//        val weatherNetworkDataSource = WeatherNetworkDataSourceImpl(apiService)
//        weatherNetworkDataSource.downloadedCurrentWeather.observe(
//            this, Observer {
//                textView.text = it.toString()
//            }
//        )
//        GlobalScope.launch(Dispatchers.Main) {
//            weatherNetworkDataSource.fetchCurrentWeather("London","en")
//            val currentWeatherResponse = apiService.getCurrentWeather("London").await()
//            textView.text = currentWeatherResponse.currentWeatherEntry.toString()
//}
    }

    private fun bindUI() = launch {
        val currentWeather = viewModel.weather.await()
        val weatherLocation = viewModel.weatherLocation.await()

        weatherLocation.observe(this@CurrentWeatherFragment, Observer { location ->
            if (location == null) return@Observer
            updateLocation(location.name)
        })


        currentWeather.observe(this@CurrentWeatherFragment, Observer {
            if (it == null) return@Observer
            group_loading.visibility = View.GONE
            updateDateToToday()
            // updateTemperature(it.howitFeels)

            GlideApp.with(this@CurrentWeatherFragment)
                .load("http ://")
                .into(imageView_condition_icon)
        })
    }

    private fun updateLocation(location: String) {
        (activity as AppCompatActivity)?.supportActionBar?.title = location
    }

    private fun updateDateToToday() {
        (activity as AppCompatActivity)?.supportActionBar?.subtitle = "Today"
    }

    private fun updateTemperature(temperature: Double, feelsLike: Double) {
        val unitAbbreviation = if (viewModel.isMetric) "C" else "F"
        textView_temperature.text = "$temperature$unitAbbreviation"
        textView_temperature.text = "$feelsLike$unitAbbreviation"
    }

    private fun updateCondition(condition: String) {
        textView_condition.text = condition
    }

    private fun updatePrecipitation(preceipitation: Double) {
        val unitAbbreviation = if (viewModel.isMetric) "mm" else "in"
        textView_condition.text = "$preceipitation.toString()$unitAbbreviation"
    }

    private fun updateWind(preceipitation: Double) {
        val unitAbbreviation = if (viewModel.isMetric) "mm" else "in"
        textView_condition.text = "$preceipitation.toString()$unitAbbreviation"
    }

    private fun updateVisibility(preceipitation: Double) {
        val unitAbbreviation = if (viewModel.isMetric) "mm" else "in"
        textView_condition.text = "$preceipitation.toString()$unitAbbreviation"
    }

}
