package com.example.kotlin_playas.uses

import com.example.kotlin_playas.data.BeachRepository
import com.example.kotlin_playas.data.model.aemet.base.AemetBase
import com.example.kotlin_playas.data.model.aemet.info.AemetInfo

class GetAemetInfoCaseUse(val details:String) {
    private val repository : BeachRepository = BeachRepository()

    suspend operator fun invoke() : AemetInfo = repository.getAemetInfo(details)
}