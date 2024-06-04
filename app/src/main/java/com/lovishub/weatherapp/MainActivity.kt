package com.lovishub.weatherapp

import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputBinding
import android.widget.SearchView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.lovishub.weatherapp.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Tag
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// 3295597426658e4713b610559d0e9acd

class MainActivity : AppCompatActivity() {
    private  val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        fetchweatherdata("jaipur")
        searchcity()
    }

    private fun searchcity() {
        val searchView = binding.searchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    fetchweatherdata(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }

        })
    }

    private fun fetchweatherdata(cityName:String){
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .build().create(apiinterface::class.java)
        val response = retrofit.getweatherdata(cityName , "3295597426658e4713b610559d0e9acd" , "metric")
        response.enqueue(object : Callback<weatherapp>{
            override fun onResponse(call: Call<weatherapp>, response: Response<weatherapp>) {
                val responseBody = response.body()
                if(response.isSuccessful && responseBody !=null){
                    val temperature = responseBody.main.temp.toString()
                    val humidity = responseBody.main.humidity
                    val windspeed = responseBody.wind.speed
                    val sunrise = responseBody.sys.sunrise.toLong()
                    val sunset = responseBody.sys.sunset.toLong()
                    val sealevel = responseBody.main.pressure
                    val condition = responseBody.weather.firstOrNull()?.main?: "unknown"
                    val maxtemp = responseBody.main.temp_max
                    val mintemp = responseBody.main.temp_min
                    binding.temp.text="$temperature °C"
                    binding.weather.text = condition
                    binding.max.text ="MAX TEMP: $maxtemp °C"
                    binding.min.text = "MIN TEMP: $mintemp °C"
                    binding.humidity.text = "$humidity %"
                    binding.windspeed.text = "$windspeed m/s"
                    binding.sunrise.text = "${time(sunrise)}"
                    binding.sunset.text = "${time(sunset)}"
                    binding.sealevel.text = "$sealevel hPa"
                    binding.condition.text = condition
                    binding.day.text = dayname(System.currentTimeMillis())
                        binding.date.text = date()
                        binding.cityname.text = "$cityName"

                    //Log.d("TAG", "onResponse: $temperature")
                    changeimagesaccordingtoweather(condition)
                }
            }

            override fun onFailure(call: Call<weatherapp>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })


    }

    private fun changeimagesaccordingtoweather(conditions : String) {
        when(conditions){
            "Haze" , "Partly Clouds" , "Clouds" , "Overcast" , "Mist" , "Foggy" ->{
                binding.root.setBackgroundResource(R.drawable.colud_background)
                binding.lottieAnimationView.setAnimation(R.raw.cloud)
            }
            "Sunny" , "Clear" , "Clear Sky" ->{
                binding.root.setBackgroundResource(R.drawable.sunny_background)
                binding.lottieAnimationView.setAnimation(R.raw.sun)
            }
            "Drizzle" , "Light Rain" , "Moderate Rain" , "Showers" , "Heavy Rain" ->{
                binding.root.setBackgroundResource(R.drawable.rain_background)
                binding.lottieAnimationView.setAnimation(R.raw.rain)
            }
            "Light Snow" , "Moderate Snow" , "Heavy Snow" , "Blizzard" ->{
                binding.root.setBackgroundResource(R.drawable.snow_background)
                binding.lottieAnimationView.setAnimation(R.raw.snow)
            }
            else ->{
                binding.root.setBackgroundResource(R.drawable.sunny_background)
                binding.lottieAnimationView.setAnimation(R.raw.sun)
            }
        }
        binding.lottieAnimationView.playAnimation()
    }

    private fun date(): String{
        val sdf = SimpleDateFormat("dd MMMM yyyy" , Locale.getDefault())
        return sdf.format((Date()))
    }
    fun dayname(timestamp : Long) : String{
        val sdf = SimpleDateFormat("EEEE" , Locale.getDefault())
        return sdf.format((Date()))
    }
    private fun time(timestamp: Long): String{
        val sdf = SimpleDateFormat("HH:mm" , Locale.getDefault())
        return sdf.format((Date(timestamp*1000)))
    }


}