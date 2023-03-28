package com.example.kotlin_playas.data.network

import com.example.kotlin_playas.core.RetrofitHelper
import com.example.kotlin_playas.data.model.aemet.base.AemetBase
import com.example.kotlin_playas.data.model.aemet.info.AemetInfo
import com.example.kotlin_playas.data.model.beach.Beach
import com.example.kotlin_playas.data.network.api.BeachApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject
import javax.inject.Named

class BeachService @Inject constructor(
    @Named("beach_client")
    val beachApiClient: BeachApiClient,
    @Named("aemet_base_client")
    val aemetBaseApiClient: BeachApiClient,
    @Named("aemet_info_client")
    val aemetInfoApiClient: BeachApiClient
){

    suspend fun getBeaches():List<Beach>{

        return withContext(Dispatchers.IO){
            val response: Response<List<Beach>> = beachApiClient.getAllBeaches()
            response.body() ?: emptyList()
        }
    }

    suspend fun getAemetBase(aemetId : Int):AemetBase?{

        return withContext(Dispatchers.IO){
               val response = aemetBaseApiClient.getAemetBase(aemetId)
                response.body()
        }
    }

    suspend fun getAemetInfo(direction : String):List<AemetInfo>{

        return withContext(Dispatchers.IO){
            val response = aemetInfoApiClient.getAemetInfo(direction)
            response.body() ?: emptyList<AemetInfo>()

        }
    }
}