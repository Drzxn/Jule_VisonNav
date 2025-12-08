package com.example.indoornavigation.map

import com.example.indoornavigation.data.model.MapPoint

class MapBuilder {

    private val points = mutableListOf<MapPoint>()

    fun addPoint(x: Float, y: Float, z: Float, label: String) {
        val id = "point_${points.size}"
        points.add(MapPoint(id, x, y, z, label))
    }

    fun getMap(): List<MapPoint> {
        return points
    }

    fun clear() {
        points.clear()
    }
}
