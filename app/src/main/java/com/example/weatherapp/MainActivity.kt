package com.example.weatherapp

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.SearchView
import android.widget.Toast
import com.example.weatherapp.apiInterface.ApiInterface
import com.example.weatherapp.databinding.ActivityMainBinding
import com.example.weatherapp.model.WeatherApp
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

//9f0c817355b980c5d04156fbb2d0c856
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    val base_url = "https://api.openweathermap.org/data/2.5/"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fetchWeatherData("peshawar")
        searchCity()

    }

    private fun searchCity() {
        val searchView = binding.searchLocation
        searchView.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {

                if (query != null) {
                    fetchWeatherData(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }

        })
    }

    private fun fetchWeatherData(cityname: String) {

        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(base_url)
            .build()
            .create(ApiInterface::class.java)

        val response =
            retrofit.getweatherData("peshawar", "9f0c817355b980c5d04156fbb2d0c856", "metric")


        response.enqueue(object : Callback<WeatherApp> {
            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call<WeatherApp>, response: Response<WeatherApp>) {

                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    val temp = responseBody.main.temp.toString()
                    // Log.d("TAG", "onres : $temp")
                    val humidity = responseBody.main.humidity
                    val windspeed = responseBody.wind.speed
                    val sunrise = responseBody.sys.sunrise
                    val sunset = responseBody.sys.sunset
                    val seaLevel = responseBody.main.pressure
                    val condintion = responseBody.weather.firstOrNull()?.main ?: "unknown"
                    val tempMin = responseBody.main.temp_min
                    val tempMax = responseBody.main.temp_max.toString()

                    binding.weatherTemp.text = temp
                    binding.humidity.text = "$humidity %"
                    binding.windSpeed.text = "$windspeed m/s"
                    binding.sunriseTime.text = "$sunrise"
                    binding.sunsetTime.text = "$sunset"
                    binding.seaLevel.text = "$seaLevel"
                    binding.weatherCondition.text = "$condintion"
                    binding.tvTempMin.text = "MIn: $tempMin"
                    binding.tvTempMax.text = "Max: $tempMax"


                    binding.todayDay.text = dayName(System.currentTimeMillis())
                    binding.weatherFullDate.text = date()
                    binding.locationName.text = "$cityname"

                    changeImgAcourdingWeather(condintion)
                }
            }

            override fun onFailure(call: Call<WeatherApp>, t: Throwable) {
                Toast.makeText(this@MainActivity, "failed", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun changeImgAcourdingWeather(condintion: String) {
        when (condintion) {
            "Haze" -> {
                binding.root.setBackgroundResource(R.drawable.cloud_back)
                binding.animationView.setAnimation(R.raw.cloud_anima)
            }
        }
    }

    private fun date(): String {
        val sdf = SimpleDateFormat("dd MMMM YYYY", Locale.getDefault())
        return sdf.format(Date())
    }

    fun dayName(timestamp: Long): String {
        val sdf = SimpleDateFormat("EEEE", Locale.getDefault())
        return sdf.format(Date())
    }
}