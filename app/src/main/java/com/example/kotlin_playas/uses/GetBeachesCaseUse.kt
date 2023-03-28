package com.example.kotlin_playas.uses

import android.content.Context
import com.example.kotlin_playas.data.BeachRepository
import com.example.kotlin_playas.data.model.beach.Beach
import javax.inject.Inject

class GetBeachesCaseUse @Inject constructor(private val repository:BeachRepository) {
    suspend operator fun invoke () : List<Beach> = repository.getAllBeaches()
}