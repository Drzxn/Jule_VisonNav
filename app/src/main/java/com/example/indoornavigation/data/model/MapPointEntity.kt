package com.example.indoornavigation.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "map_points")
data class MapPointEntity(
    @PrimaryKey val id: String,
    val x: Float,
    val y: Float,
    val z: Float,
    val label: String
)
