package com.example.indoornavigation.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.indoornavigation.data.model.MapPointEntity
import com.example.indoornavigation.data.database.MapPointDao

@Database(entities = [MapPointEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun mapPointDao(): MapPointDao
}
