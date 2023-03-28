package com.example.kotlin_playas.ui.viewmodel

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kotlin_playas.data.model.BeachProvider
import com.example.kotlin_playas.data.model.aemet.info.AemetInfo
import com.example.kotlin_playas.data.model.aemet.symbol.AemetSymbol
import com.example.kotlin_playas.data.model.beach.Beach
import com.example.kotlin_playas.uses.GetAemetBaseCaseUse
import com.example.kotlin_playas.uses.GetAemetInfoCaseUse
import com.example.kotlin_playas.uses.GetSelectedBeachCaseUse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val getSelectedBeachCaseUse : GetSelectedBeachCaseUse,
    private val getAemetBaseCaseUse: GetAemetBaseCaseUse,
    private val getAemetInfoCaseUse: GetAemetInfoCaseUse,
    @Named("symbol_list")
    private val symbolList : List<AemetSymbol>
) : ViewModel() {

    val selBeach = MutableLiveData<Beach>()
    val aemetInfo = MutableLiveData<AemetInfo>()
    val isLoading = MutableLiveData<Boolean>()
    val isAemetInfo = MutableLiveData<Boolean>()


    @SuppressLint("NullSafeMutableLiveData")
    fun loadAemetInfo(){

        viewModelScope.launch(Dispatchers.IO) {
            isLoading.postValue(true)
            isAemetInfo.postValue(false)

            val selectedBeach = getSelectedBeachCaseUse(BeachProvider.selectedBeachId)

            selBeach.postValue(selectedBeach)

            var idAemet = selectedBeach.idAemet ?: 0
            val aemetBase = getAemetBaseCaseUse(idAemet)

            aemetBase.let {
                    aemetBase ->
                if (aemetBase!= null) {
                    val aemetValue: AemetInfo = getAemetInfoCaseUse(getDetailsFromUrl(aemetBase.data!!))
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

        var symbol = symbolList.first().url

        for (i in 0 until symbolList.size){
            // buscamos descripción que encaje con la del simbolo
            if (stringDescr.equals(symbolList[i].name)){
                symbol = symbolList[i].url
                break
            }
        }
        return symbol

    }
}