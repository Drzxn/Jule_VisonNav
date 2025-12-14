package com.example.realtimesafety.data

import android.graphics.RectF

/**
 * Represents a single detected object.
 *
 * @param boundingBox The bounding box of the detected object.
 * @param label The class label of the object (e.g., "Person", "Chair").
 * @param score The confidence score of the detection (0.0 to 1.0).
 */
data class Detection(
    val boundingBox: RectF,
    val label: String,
    val score: Float
)
