package com.example.indoornavigation.map

import com.example.indoornavigation.data.model.MapPoint

class PoiManager {

    private val pois = mutableMapOf<String, MapPoint>()

    fun addPoi(point: MapPoint) {
        pois[point.id] = point
    }

    fun getPoi(id: String): MapPoint? {
        return pois[id]
    }

    fun getAllPois(): List<MapPoint> {
        return pois.values.toList()
    }
}
