package com.example.kotlin_playas.data

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.example.kotlin_playas.data.model.BeachProvider
import com.example.kotlin_playas.data.model.aemet.base.AemetBase
import com.example.kotlin_playas.data.model.aemet.info.AemetInfo
import com.example.kotlin_playas.data.model.beach.Beach
import com.example.kotlin_playas.data.model.beach.dao.BeachDAO
import com.example.kotlin_playas.data.network.BeachDatabase
import com.example.kotlin_playas.data.network.BeachService
import javax.inject.Inject
import javax.inject.Named

class BeachRepository @Inject constructor(
    private val service: BeachService,
    private val appContext : Application,
    private val beachDAO: BeachDAO,
    @Named("internet_status")
    private val internetStatus: Boolean
) {

    suspend fun getAllBeaches():List<Beach>{
        var beachList : List<Beach> = emptyList()

        if (internetStatus){
            beachList = service.getBeaches()
            saveBeaches(beachList)
        }else{
            beachList = getBeaches()
        }

        return beachList

    }

    suspend fun getBeachById(id : Int):Beach{
        return beachDAO.selectBeachById(id) ?: Beach()
    }

    suspend fun getBeaches() : List<Beach>{
        return beachDAO.selectBeaches() ?: emptyList()
    }

    suspend fun saveBeaches(beachList : List<Beach>){
        for (beach in beachList){
            beachDAO.insertBeach(beach)
        }
    }

    suspend fun getAemetBase(aemetId : Int): AemetBase?{
        return service.getAemetBase(aemetId)
    }

    suspend fun getAemetInfo(direction : String): AemetInfo{
        return service.getAemetInfo(direction).first()
    }

    companion object{

    }
}