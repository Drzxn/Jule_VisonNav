package com.example.realtimesafety.domain

import com.example.realtimesafety.domain.model.HazardEvent
import com.example.realtimesafety.domain.model.MotionState
import com.example.realtimesafety.domain.model.SeverityLevel
import com.example.realtimesafety.domain.model.TrackedObject

/**
 * Evaluates tracked objects to determine if they pose a hazard.
 *
 * This class uses a set of rules to analyze the properties of a tracked object (e.g., distance,
 * velocity, confidence) and generate a `HazardEvent` with an appropriate severity level.
 */
class HazardEvaluator {
    private val confidenceThreshold = 0.5f
    private val hazardRules = mapOf(
        "Person" to 2.0f,
        "Chair" to 1.5f,
        "Door" to 1.5f,
        "Stairs" to 3.0f
    )
    private val emergencyStopTime = 0.75f

    fun evaluate(tracks: List<TrackedObject>): List<HazardEvent> {
        val events = mutableListOf<HazardEvent>()
        for (track in tracks) {
            if (track.confidence < confidenceThreshold) continue

            if (track.smoothedDistance < emergencyStopTime) {
                events.add(HazardEvent(track.id, SeverityLevel.CRITICAL, "STOP"))
                continue
            }

            if (track.motionState == MotionState.FAST_APPROACHING) {
                events.add(HazardEvent(track.id, SeverityLevel.HIGH, "Fast approaching object"))
                continue
            }

            val distanceThreshold = hazardRules[track.label]
            if (distanceThreshold != null && track.smoothedDistance < distanceThreshold) {
                val severity = if (track.motionState == MotionState.STATIC) SeverityLevel.LOW else SeverityLevel.MEDIUM
                events.add(HazardEvent(track.id, severity, "${track.label} ahead"))
            }
        }
        return events
    }
}
