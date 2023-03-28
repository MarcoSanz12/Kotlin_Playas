package com.example.kotlin_playas.uses

import android.content.Context
import com.example.kotlin_playas.data.BeachRepository
import com.example.kotlin_playas.data.model.beach.Beach
import javax.inject.Inject

class GetSelectedBeachCaseUse @Inject constructor(
    private val repository : BeachRepository
) {
    suspend operator fun invoke (id:Int) : Beach = repository.getBeachById(id)
}