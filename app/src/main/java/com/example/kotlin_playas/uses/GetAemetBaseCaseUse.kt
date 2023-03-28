package com.example.kotlin_playas.uses

import com.example.kotlin_playas.data.BeachRepository
import com.example.kotlin_playas.data.model.aemet.base.AemetBase
import javax.inject.Inject

class GetAemetBaseCaseUse @Inject constructor(
    private val repository : BeachRepository
) {
    suspend operator fun invoke(aemetId : Int) : AemetBase?  = repository.getAemetBase(aemetId)
}