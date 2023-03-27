package com.example.kotlin_playas.data.model

import androidx.room.Room
import com.example.kotlin_playas.data.model.beach.Beach
import com.example.kotlin_playas.data.network.BeachDatabase

object BeachProvider {
    var selectedBeachId:Int = 0
}