package com.example.kotlin_playas.data.network.api

import com.example.kotlin_playas.data.model.aemet.base.AemetBase
import com.example.kotlin_playas.data.model.aemet.info.AemetInfo
import com.example.kotlin_playas.data.model.beach.Beach
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface BeachApiClient {

    @GET("beach/0?_format=json")
    suspend fun getAllBeaches():Response<List<Beach>>

    @GET("prediccion/especifica/playa/{idAemet}/?api_key=eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzYW11ZWxzYW50b3NAZ3J1cG90ZWNvcHkuZXMiLCJqdGkiOiJkMGY2ODBmOC1lNzA1LTQyMjctODRiZC04MjJmNGE2YWJiZjQiLCJpc3MiOiJBRU1FVCIsImlhdCI6MTYyMTMyODY1NCwidXNlcklkIjoiZDBmNjgwZjgtZTcwNS00MjI3LTg0YmQtODIyZjRhNmFiYmY0Iiwicm9sZSI6IiJ9.WDuLkBpbsEpY9glb9COa6eh4zWOuvzUq4ZPade7wQoY")
    suspend fun getAemetBase(@Path("idAemet") idAemet : Int) : Response<AemetBase>

    @GET("{direction}")
    suspend fun getAemetInfo(@Path("direction") direction : String) : Response<List<AemetInfo>>
}