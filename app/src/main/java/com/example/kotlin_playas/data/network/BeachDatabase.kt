package com.example.kotlin_playas.data.network

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.kotlin_playas.data.model.beach.Beach
import com.example.kotlin_playas.data.model.beach.dao.BeachDAO

@Database(entities = [Beach::class], version = 1)
@TypeConverters(BeachConverter::class)
abstract class BeachDatabase : RoomDatabase () {
    abstract fun beachDao() : BeachDAO
}