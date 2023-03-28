package com.example.kotlin_playas.ui.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kotlin_playas.data.model.BeachProvider
import com.example.kotlin_playas.data.model.aemet.info.AemetInfo
import com.example.kotlin_playas.data.model.beach.Beach
import com.example.kotlin_playas.uses.GetAemetBaseCaseUse
import com.example.kotlin_playas.uses.GetAemetInfoCaseUse
import com.example.kotlin_playas.uses.GetBeachesCaseUse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BeachViewModel @Inject constructor(
    val getBeachesCaseUse : GetBeachesCaseUse) : ViewModel(){

    val beachList = MutableLiveData<List<Beach>>()
    val isLoading = MutableLiveData<Boolean>()
    val isMapFollowing = MutableLiveData<Boolean>()
    val isMurciaCensored = MutableLiveData<Boolean>()
    val isRoutesVisible = MutableLiveData<Boolean>()
    val distanceRoute = MutableLiveData<Double>()



    fun onCreate(ctx : Context){
        viewModelScope.launch(Dispatchers.IO) {
            isLoading.postValue(true)
            val result = getBeachesCaseUse()

            if (!result.isNullOrEmpty()){
                beachList.postValue(result)
            }
            isLoading.postValue(false)
        }
    }

    fun selectBeach(pBeach: Beach){
        BeachProvider.selectedBeachId = pBeach.nid ?: 0
    }

    fun setIsMapFollowing(following : Boolean){
        isMapFollowing.postValue(following)
    }

    fun setIsMurciaCensored(censored : Boolean){
        isMurciaCensored.postValue(censored)
    }

    fun setIsRoutesVisible(visible:Boolean){
        isRoutesVisible.postValue(visible)
    }

    fun setDistance (distance : Double){
        distanceRoute.postValue(distance)
    }



}