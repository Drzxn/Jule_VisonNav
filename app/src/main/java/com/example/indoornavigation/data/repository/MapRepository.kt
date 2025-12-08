package com.example.indoornavigation.data.repository

import com.example.indoornavigation.data.model.MapPoint

interface MapRepository {
    suspend fun getMapPoints(): List<MapPoint>
    suspend fun saveMapPoint(point: MapPoint)
}
