package com.lovishub.weatherapp

import android.telecom.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface apiinterface {
    @GET("weather")
    fun getweatherdata(
        @Query("q") city:String,
        @Query("appid") appid:String,
        @Query("units") units:String
    ) : retrofit2.Call<weatherapp>
}