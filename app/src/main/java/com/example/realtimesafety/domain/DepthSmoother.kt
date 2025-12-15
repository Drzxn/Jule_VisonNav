package com.example.realtimesafety.domain

import com.example.realtimesafety.domain.model.TrackedObject

/**
 * Stabilizes raw ARCore depth data using temporal smoothing.
 *
 * This class applies an Exponential Moving Average (EMA) to the distance history of a tracked object
 * to reduce noise and provide a more stable distance estimate. It also calculates a confidence score
 * based on the number of recent measurements.
 */
class DepthSmoother {
    private val alpha = 0.2f // EMA smoothing factor
    private val maxHistorySize = 10

    fun smooth(track: TrackedObject, newDistance: Float) {
        if (newDistance == Float.MAX_VALUE) {
            // Penalize confidence for unknown distance
            track.confidence = maxOf(0.0f, track.confidence - 0.1f)
            return
        }

        // Add new measurement to history
        track.distanceHistory.add(Pair(System.currentTimeMillis(), newDistance))
        if (track.distanceHistory.size > maxHistorySize) {
            track.distanceHistory.removeAt(0)
        }

        // Update smoothed distance using EMA
        track.smoothedDistance = if (track.smoothedDistance == 0.0f) {
            newDistance
        } else {
            alpha * newDistance + (1 - alpha) * track.smoothedDistance
        }

        // Update confidence
        track.confidence = minOf(1.0f, track.distanceHistory.size / maxHistorySize.toFloat())
    }
}
