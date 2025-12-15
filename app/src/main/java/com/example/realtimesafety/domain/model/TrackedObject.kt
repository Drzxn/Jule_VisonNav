package com.example.realtimesafety.domain.model

import android.graphics.RectF
import com.example.realtimesafety.data.Detection

/**
 * Represents an object that is being tracked across multiple frames.
 *
 * @param id A unique and stable ID for the tracked object.
 * @param label The class label of the object.
 * @param boundingBox The most recent bounding box of the object.
 * @param lastSeenTimestamp The timestamp when the object was last seen.
 * @param distanceHistory A list of recent distance measurements with their timestamps.
 * @param smoothedDistance The smoothed distance to the object (e.g., using EMA).
 * @param confidence The confidence score for the smoothed distance.
 * @param motionState The current motion state of the object.
 */
data class TrackedObject(
    val id: Int,
    var label: String,
    var boundingBox: RectF,
    var lastSeenTimestamp: Long = System.currentTimeMillis(),
    val distanceHistory: MutableList<Pair<Long, Float>> = mutableListOf(),
    var smoothedDistance: Float = 0.0f,
    var confidence: Float = 0.0f,
    var motionState: MotionState = MotionState.STATIC
) {
    /**
     * Updates the tracked object with a new detection.
     */
    fun update(detection: Detection) {
        this.boundingBox = detection.boundingBox
        this.label = detection.label
        this.lastSeenTimestamp = System.currentTimeMillis()
    }

    fun toDetection(): Detection {
        return Detection(boundingBox, label, confidence)
    }
}

/**
 * Represents the motion state of a tracked object.
 */
enum class MotionState {
    STATIC,
    SLOW_MOVING,
    FAST_MOVING,
    FAST_APPROACHING
}
