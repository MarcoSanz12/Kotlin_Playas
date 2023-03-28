package com.example.kotlin_playas.uses

import com.example.kotlin_playas.data.BeachRepository
import com.example.kotlin_playas.data.model.aemet.base.AemetBase
import com.example.kotlin_playas.data.model.aemet.info.AemetInfo
import javax.inject.Inject

class GetAemetInfoCaseUse @Inject constructor(
    private val repository : BeachRepository
)
{ suspend operator fun invoke(details:String) : AemetInfo = repository.getAemetInfo(details)
}