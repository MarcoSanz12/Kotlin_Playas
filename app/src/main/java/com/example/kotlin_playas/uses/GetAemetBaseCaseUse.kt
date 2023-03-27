package com.example.kotlin_playas.uses

import com.example.kotlin_playas.data.BeachRepository
import com.example.kotlin_playas.data.model.aemet.base.AemetBase

class GetAemetBaseCaseUse(val aemetId : Int) {
    private val repository : BeachRepository = BeachRepository()

    suspend operator fun invoke() : AemetBase?  = repository.getAemetBase(aemetId)
}