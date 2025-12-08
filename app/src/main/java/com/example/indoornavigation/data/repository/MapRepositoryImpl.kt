package com.example.indoornavigation.data.repository

import com.example.indoornavigation.data.database.MapPointDao
import com.example.indoornavigation.data.model.MapPoint
import com.example.indoornavigation.data.model.MapPointEntity
import javax.inject.Inject

class MapRepositoryImpl @Inject constructor(
    private val mapPointDao: MapPointDao
) : MapRepository {
    override suspend fun getMapPoints(): List<MapPoint> {
        return mapPointDao.getAll().map {
            MapPoint(it.id, it.x, it.y, it.z, it.label)
        }
    }

    override suspend fun saveMapPoint(point: MapPoint) {
        val entity = MapPointEntity(point.id, point.x, point.y, point.z, point.label)
        mapPointDao.insert(entity)
    }
}
