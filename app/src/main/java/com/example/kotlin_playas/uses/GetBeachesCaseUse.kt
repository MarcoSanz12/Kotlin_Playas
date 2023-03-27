package com.example.kotlin_playas.uses

import android.content.Context
import com.example.kotlin_playas.data.BeachRepository
import com.example.kotlin_playas.data.model.beach.Beach

class GetBeachesCaseUse {

    private val repository = BeachRepository()

    suspend operator fun invoke (ctx:Context) : List<Beach> = repository.getAllBeaches(ctx)
}