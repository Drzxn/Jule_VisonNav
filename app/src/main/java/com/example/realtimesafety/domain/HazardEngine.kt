package com.example.realtimesafety.domain

import com.example.realtimesafety.ar.DepthManager
import com.example.realtimesafety.data.Detection
import com.google.ar.core.Frame

/**
 * A simple, rule-based engine to detect hazards.
 */
class HazardEngine {
    private val hazardRules = mapOf(
        "Person" to 2.0f, // Warn if a person is within 2 meters
        "Chair" to 1.5f,  // Warn if a chair is within 1.5 meters
        "Door" to 1.5f,
        "Stairs" to 3.0f
    )
    private val emergencyStopTime = 0.75f

    data class HazardResult(val message: String, val isEmergency: Boolean)

    fun checkForHazards(detections: List<Detection>, frame: Frame, depthManager: DepthManager): HazardResult? {
        for (detection in detections) {
            val centerX = detection.boundingBox.centerX().toInt()
            val centerY = detection.boundingBox.centerY().toInt()
            val distance = depthManager.getDepth(frame, centerX, centerY)

            if (distance < emergencyStopTime) {
                return HazardResult("STOP", true)
            }

            val distanceThreshold = hazardRules[detection.label]
            if (distanceThreshold != null) {
                if (distance < distanceThreshold) {
                    return HazardResult("${detection.label} ahead", false)
                }
            }
        }
        return null
    }
}
