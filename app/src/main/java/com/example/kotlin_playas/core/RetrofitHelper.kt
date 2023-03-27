package com.example.kotlin_playas.core

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitHelper {
    const val BASE_URL = "https://turismolamangades.grupotecopy.es/api/"
    const val AEMET_BASE_URL = "https://opendata.aemet.es/opendata/api/"
    const val AEMET_INFO_URL = "https://opendata.aemet.es/opendata/sh/"

    fun getRetrofit(url:String):Retrofit{
        return Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

}