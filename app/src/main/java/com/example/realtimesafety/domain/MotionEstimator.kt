package com.example.realtimesafety.domain

import com.example.realtimesafety.domain.model.MotionState
import com.example.realtimesafety.domain.model.TrackedObject

/**
 * Estimates the motion and velocity of a tracked object.
 *
 * This class analyzes the smoothed distance history of a tracked object to determine its velocity
 * and classify its motion state (e.g., STATIC, FAST_APPROACHING).
 */
class MotionEstimator {
    private val velocityThreshold = 1.0f // meters per second
    private val fastVelocityThreshold = 2.0f // meters per second

    fun estimate(track: TrackedObject) {
        if (track.distanceHistory.size < 2) {
            track.motionState = MotionState.STATIC
            return
        }

        val first = track.distanceHistory.first()
        val last = track.distanceHistory.last()
        val timeDelta = (last.first - first.first) / 1000.0f // in seconds
        val distanceDelta = last.second - first.second // in meters

        if (timeDelta == 0.0f) {
            track.motionState = MotionState.STATIC
            return
        }

        val velocity = distanceDelta / timeDelta

        track.motionState = when {
            velocity < -fastVelocityThreshold -> MotionState.FAST_APPROACHING
            velocity < -velocityThreshold -> MotionState.FAST_MOVING
            velocity < -0.1f -> MotionState.SLOW_MOVING
            velocity > 0.1f -> MotionState.SLOW_MOVING
            else -> MotionState.STATIC
        }
    }
}
