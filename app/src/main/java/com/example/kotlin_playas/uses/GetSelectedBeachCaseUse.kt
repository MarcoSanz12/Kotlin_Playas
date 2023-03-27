package com.example.kotlin_playas.uses

import android.content.Context
import com.example.kotlin_playas.data.BeachRepository
import com.example.kotlin_playas.data.model.beach.Beach

class GetSelectedBeachCaseUse {

    private val repository = BeachRepository()

    suspend operator fun invoke (id:Int) : Beach = repository.getBeachById(id)
}