package com.example.kotlin_playas.ui.viewmodel

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kotlin_playas.data.model.BeachProvider
import com.example.kotlin_playas.data.model.aemet.info.AemetInfo
import com.example.kotlin_playas.data.model.aemet.symbol.SymbolProvider
import com.example.kotlin_playas.data.model.beach.Beach
import com.example.kotlin_playas.uses.GetAemetBaseCaseUse
import com.example.kotlin_playas.uses.GetAemetInfoCaseUse
import com.example.kotlin_playas.uses.GetSelectedBeachCaseUse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailViewModel : ViewModel() {

    val selBeach = MutableLiveData<Beach>()
    val aemetInfo = MutableLiveData<AemetInfo>()
    val isLoading = MutableLiveData<Boolean>()
    val isAemetInfo = MutableLiveData<Boolean>()

    var getSelectedBeachCaseUse = GetSelectedBeachCaseUse()
    @SuppressLint("NullSafeMutableLiveData")
    fun loadAemetInfo(){



        viewModelScope.launch(Dispatchers.IO) {
            isLoading.postValue(true)
            isAemetInfo.postValue(false)

            val selectedBeach = getSelectedBeachCaseUse(BeachProvider.selectedBeachId)

            selBeach.postValue(selectedBeach)

            var idAemet = selectedBeach.idAemet ?: 0
            var getAemetBaseCaseUse = GetAemetBaseCaseUse(idAemet)

            val aemetBase = getAemetBaseCaseUse()

            aemetBase.let {
                    aemetBase ->
                if (aemetBase!= null) {
                    var getAemetInfoCaseUse =
                        GetAemetInfoCaseUse(getDetailsFromUrl(aemetBase.data!!))
                    val aemetValue: AemetInfo = getAemetInfoCaseUse()
                    aemetInfo.postValue(aemetValue)
                    if (aemetValue.id != null){
                        isAemetInfo.postValue(true)
                    }
                }
            }
            isLoading.postValue(false)
        }
    }

    private fun getDetailsFromUrl(url:String) : String{

        return url.split("/").last()
    }

    fun getSymbolUrl(stringDescr: String?):String{
        val provider = SymbolProvider()
        var symbol = provider.symbolList.first().url

        for (i in 0 until provider.symbolList.size){
            // buscamos descripción que encaje con la del simbolo
            if (stringDescr.equals(provider.symbolList[i].name)){
                symbol = provider.symbolList[i].url
                break
            }
        }
        return symbol

    }
}