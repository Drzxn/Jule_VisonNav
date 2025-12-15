package com.example.realtimesafety.domain

import android.graphics.RectF
import com.example.realtimesafety.data.Detection
import com.example.realtimesafety.domain.model.TrackedObject
import kotlin.math.max
import kotlin.math.min

/**
 * A lightweight, IOU-based object tracker.
 *
 * This class is responsible for assigning stable IDs to detected objects across frames.
 * It uses an Intersection over Union (IOU) matching algorithm to associate new detections
 * with existing tracks.
 */
class ObjectTracker {
    private val tracks = mutableMapOf<Int, TrackedObject>()
    private var nextId = 0
    private val iouThreshold = 0.5f

    fun update(detections: List<Detection>): List<TrackedObject> {
        val matchedTracks = mutableSetOf<Int>()
        val updatedTracks = mutableListOf<TrackedObject>()

        for (detection in detections) {
            var bestMatch: TrackedObject? = null
            var bestIou = 0f

            for (track in tracks.values) {
                if (track.id in matchedTracks) continue
                val iou = calculateIou(detection.boundingBox, track.boundingBox)
                if (iou > bestIou && iou > iouThreshold) {
                    bestIou = iou
                    bestMatch = track
                }
            }

            if (bestMatch != null) {
                bestMatch.update(detection)
                updatedTracks.add(bestMatch)
                matchedTracks.add(bestMatch.id)
            } else {
                val newTrack = TrackedObject(nextId++, detection.label, detection.boundingBox)
                tracks[newTrack.id] = newTrack
                updatedTracks.add(newTrack)
            }
        }

        // Remove old tracks
        tracks.values.removeIf { it.lastSeenTimestamp < System.currentTimeMillis() - 1000 }

        return tracks.values.toList()
    }

    private fun calculateIou(rect1: RectF, rect2: RectF): Float {
        val xA = max(rect1.left, rect2.left)
        val yA = max(rect1.top, rect2.top)
        val xB = min(rect1.right, rect2.right)
        val yB = min(rect1.bottom, rect2.bottom)

        val intersectionArea = max(0f, xB - xA) * max(0f, yB - yA)
        val rect1Area = (rect1.right - rect1.left) * (rect1.bottom - rect1.top)
        val rect2Area = (rect2.right - rect2.left) * (rect2.bottom - rect2.top)
        val unionArea = rect1Area + rect2Area - intersectionArea

        return if (unionArea > 0) intersectionArea / unionArea else 0f
    }
}
