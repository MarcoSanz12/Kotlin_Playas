package com.example.kotlin_playas.di

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.example.kotlin_playas.data.model.aemet.symbol.AemetSymbol
import com.example.kotlin_playas.data.model.beach.dao.BeachDAO
import com.example.kotlin_playas.data.network.BeachDatabase
import com.example.kotlin_playas.data.network.api.BeachApiClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    @Named ("beach_url")
    fun provideBeachUrl():String {
        return "https://smart.lamanga365.es/api/"}

    @Singleton
    @Provides
    @Named ("aemet_base_url")
    fun provideAemetBaseUrl():String {
        return "https://opendata.aemet.es/opendata/api/"}

    @Singleton
    @Provides
    @Named ("aemet_info_url")
    fun provideAemetInfoUrl():String {
        return "https://opendata.aemet.es/opendata/sh/"}

    @Singleton
    @Provides
    @Named ("beach_client")
    fun provideBeachApiClient(@Named("beach_url") url:String) : BeachApiClient{
            return Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(BeachApiClient::class.java)
    }

    @Singleton
    @Provides
    @Named ("aemet_base_client")
    fun provideAemetBaseApiClient(@Named("aemet_base_url") url:String) : BeachApiClient{
        return Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(BeachApiClient::class.java)
    }

    @Singleton
    @Provides
    @Named ("aemet_info_client")
    fun provideAemetInfoApiClient(@Named("aemet_info_url") url:String) : BeachApiClient{
        return Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(BeachApiClient::class.java)
    }

    @Singleton
    @Provides
    fun provideBeachDatabase(@ApplicationContext appContext:Context):BeachDatabase{
        return Room.databaseBuilder(appContext,BeachDatabase::class.java,"beachdb").build()
    }

    @Singleton
    @Provides
    fun provideBeachDao(database:BeachDatabase) : BeachDAO{
        return database.beachDao()
    }

    @Singleton
    @Provides
    @Named ("internet_status")
    fun provideInternetStatus(@ApplicationContext appContext: Context): Boolean {
        var cm: ConnectivityManager = appContext.getSystemService(AppCompatActivity.CONNECTIVITY_SERVICE) as ConnectivityManager

        var status = false
        if (cm != null) {
            var netCap = cm.getNetworkCapabilities(cm.activeNetwork)

            status = netCap?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) ?: false
        }
        return status
    }

    @Singleton
    @Provides
    @Named("symbol_list")
    fun provideSymbolList():List<AemetSymbol>{
        return listOf<AemetSymbol>(
            AemetSymbol("despejado","https://www.aemet.es/imagenes_gcd/_iconos_municipios/11.png"),
            AemetSymbol("nuboso","https://www.aemet.es/imagenes_gcd/_iconos_municipios/14.png"),
            AemetSymbol("muy nuboso","https://www.aemet.es/imagenes_gcd/_iconos_municipios/15.png"),
            AemetSymbol("chubascos","https://www.aemet.es/imagenes_gcd/_iconos_municipios/43.png"),
            AemetSymbol("muy nuboso con lluvia","https://www.aemet.es/imagenes_gcd/_iconos_municipios/25.png")
        )
    }
}