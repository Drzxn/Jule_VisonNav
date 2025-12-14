package com.example.realtimesafety.data

/**
 * Represents the state of the UI, including the list of detections and the dimensions of the
 * frame they were detected in.
 *
 * @param detections The list of detected objects.
 * @param imageWidth The width of the image the objects were detected in.
 * @param imageHeight The height of the image the objects were detected in.
 */
data class UIState(
    val detections: List<Detection>,
    val imageWidth: Int,
    val imageHeight: Int
)
