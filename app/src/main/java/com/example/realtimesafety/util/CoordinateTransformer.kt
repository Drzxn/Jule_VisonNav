package com.example.realtimesafety.util

import android.graphics.Matrix
import android.graphics.RectF

/**
 * A utility to transform coordinates between different resolutions.
 */
class CoordinateTransformer(
    private val sourceWidth: Int,
    private val sourceHeight: Int,
    private val destWidth: Int,
    private val destHeight: Int
) {
    private val transformMatrix = Matrix()

    init {
        val scaleX = destWidth.toFloat() / sourceWidth
        val scaleY = destHeight.toFloat() / sourceHeight
        transformMatrix.setScale(scaleX, scaleY)
    }

    fun transform(rect: RectF): RectF {
        val transformedRect = RectF(rect)
        transformMatrix.mapRect(transformedRect)
        return transformedRect
    }
}
