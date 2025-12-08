package com.example.indoornavigation.navigation

import com.example.indoornavigation.data.model.MapPoint

/**
 * Handles pathfinding and route guidance based on indoor map data.
 */
class NavigationEngine {

    private var currentPosition: MapPoint? = null
    private var route: List<MapPoint> = emptyList()

    fun setRoute(points: List<MapPoint>) {
        this.route = points
    }

    fun updatePosition(position: MapPoint) {
        this.currentPosition = position
    }

    fun getNextDirection(): String {
        // TODO: Implement pathfinding logic to determine the next turn/instruction
        return "Turn left in 5 meters."
    }
}
