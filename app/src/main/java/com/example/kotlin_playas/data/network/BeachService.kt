package com.example.kotlin_playas.data.network

import android.util.Log
import com.example.kotlin_playas.core.RetrofitHelper
import com.example.kotlin_playas.data.model.aemet.base.AemetBase
import com.example.kotlin_playas.data.model.aemet.info.AemetInfo
import com.example.kotlin_playas.data.model.beach.Beach
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import retrofit2.Retrofit
import java.io.EOFException
import java.io.IOException

class BeachService {


    suspend fun getBeaches():List<Beach>{

        var retrofit = RetrofitHelper.getRetrofit(RetrofitHelper.BASE_URL)

        return withContext(Dispatchers.IO){

            val response: Response<List<Beach>> =
                retrofit.create(BeachApiClient::class.java).getAllBeaches()

            response.body() ?: emptyList()
        }
    }

    suspend fun getAemetBase(aemetId : Int):AemetBase?{

        var retrofit = RetrofitHelper.getRetrofit(RetrofitHelper.AEMET_BASE_URL
        )
        return withContext(Dispatchers.IO){
            var responseAemetBase : AemetBase? = null
            try {
                val response: Response<AemetBase> =
                    retrofit.create(BeachApiClient::class.java).getAemetBase(aemetId)
                responseAemetBase = response.body()
            }catch(e : IOException){
                getAemetBase(aemetId)
            }

           responseAemetBase
        }
    }

    suspend fun getAemetInfo(direction : String):List<AemetInfo>{
        var retrofit = RetrofitHelper.getRetrofit(RetrofitHelper.AEMET_INFO_URL)

        return withContext(Dispatchers.IO){

            val response:Response<List<AemetInfo>> =
                retrofit.create(BeachApiClient::class.java).getAemetInfo(direction)

            response.body() ?: emptyList()


        }
    }
}