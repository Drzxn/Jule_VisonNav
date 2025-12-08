package com.example.indoornavigation.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.indoornavigation.data.model.MapPointEntity

@Dao
interface MapPointDao {
    @Query("SELECT * FROM map_points")
    suspend fun getAll(): List<MapPointEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(point: MapPointEntity)
}
