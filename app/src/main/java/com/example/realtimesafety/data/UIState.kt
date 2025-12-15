package com.example.realtimesafety.data

import com.example.realtimesafety.domain.model.TrackedObject

/**
 * Represents the state of the UI, including the list of tracked objects and the dimensions of the
 * frame they were detected in.
 *
 * @param trackedObjects The list of tracked objects.
 * @param imageWidth The width of the image the objects were detected in.
 * @param imageHeight The height of the image the objects were detected in.
 */
data class UIState(
    val trackedObjects: List<TrackedObject>,
    val imageWidth: Int,
    val imageHeight: Int
)
