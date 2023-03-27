package com.example.kotlin_playas.data

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.example.kotlin_playas.data.model.BeachProvider
import com.example.kotlin_playas.data.model.aemet.base.AemetBase
import com.example.kotlin_playas.data.model.aemet.info.AemetInfo
import com.example.kotlin_playas.data.model.beach.Beach
import com.example.kotlin_playas.data.network.BeachDatabase
import com.example.kotlin_playas.data.network.BeachService

class BeachRepository {

    private val service = BeachService()


    suspend fun getAllBeaches(ctx:Context):List<Beach>{
        var beachList : List<Beach> = emptyList()
        if (database == null){
            initiateDatabase(ctx)
        }
        if (internetStatus(ctx)){
            beachList = service.getBeaches()
            saveBeaches(beachList)
        }else{
            beachList = getBeaches()
        }

        return beachList

    }
    suspend fun initiateDatabase (context: Context){
        database = Room.databaseBuilder(context,BeachDatabase::class.java,"beachdb").build()
    }

    suspend fun getBeachById(id : Int):Beach{
        return database?.beachDao()?.selectBeachById(id) ?: Beach()
    }

    suspend fun getBeaches() : List<Beach>{
        return database?.beachDao()?.selectBeaches() ?: emptyList()
    }

    suspend fun saveBeaches(beachList : List<Beach>){
        for (beach in beachList){
            database?.beachDao()?.insertBeach(beach)
        }
    }

    suspend fun getAemetBase(aemetId : Int): AemetBase?{

        val responseBase = service.getAemetBase(aemetId)
        return responseBase
    }

    suspend fun getAemetInfo(direction : String): AemetInfo{
        val responseInfo = service.getAemetInfo(direction)
        return responseInfo[0]
    }

    companion object{
        private var database : BeachDatabase? = null
        fun internetStatus(context: Context): Boolean {
            var cm: ConnectivityManager = context.getSystemService(AppCompatActivity.CONNECTIVITY_SERVICE) as ConnectivityManager

            var status = false
            if (cm != null) {
                var netCap = cm.getNetworkCapabilities(cm.activeNetwork)

                status = netCap?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) ?: false
            }
            return status
        }
    }
}