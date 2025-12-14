package com.example.realtimesafety.ml

import android.content.Context
import android.media.Image
import androidx.camera.core.ImageProxy
import com.example.realtimesafety.data.Detection
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.task.core.BaseOptions
import org.tensorflow.lite.task.vision.detector.ObjectDetector

/**
 * Handles the loading of the TFLite model and runs inference on camera frames.
 *
 * @param context The application context.
 */
class ObjectDetector(
    private val context: Context
) {
    private var detector: ObjectDetector? = null

    init {
        setupDetector()
    }

    private fun setupDetector() {
        val baseOptions = BaseOptions.builder().useGpu().build()
        val options = ObjectDetector.ObjectDetectorOptions.builder()
            .setBaseOptions(baseOptions)
            .setMaxResults(5) // Limit to 5 detections
            .setScoreThreshold(0.5f) // Confidence threshold
            .build()

        try {
            detector = ObjectDetector.createFromFileAndOptions(
                context,
                "yolov8n.tflite", // Make sure this model is in your assets folder
                options
            )
        } catch (e: Exception) {
            // Handle model loading failure
        }
    }

    fun detect(imageProxy: ImageProxy, onResult: (List<Detection>) -> Unit) {
        if (detector == null) {
            onResult(emptyList())
            return
        }

        val image = imageProxy.image ?: run {
            onResult(emptyList())
            return
        }

        val tensorImage = TensorImage.fromMediaImage(image, imageProxy.imageInfo.rotationDegrees)
        val results = detector?.detect(tensorImage)

        onResult(results?.map {
            Detection(
                boundingBox = it.boundingBox,
                label = it.categories.firstOrNull()?.label ?: "Unknown",
                score = it.categories.firstOrNull()?.score ?: 0f
            )
        } ?: emptyList())
    }
}
