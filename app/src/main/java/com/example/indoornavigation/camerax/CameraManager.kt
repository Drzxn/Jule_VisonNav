package com.example.indoornavigation.camerax

import android.content.Context
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 * Manages the camera hardware using CameraX.
 *
 * This class is responsible for initializing the camera, setting up the preview and image
 * analysis use cases, and binding them to the lifecycle of an activity or fragment.
 *
 * @param context The application context.
 * @param finderView The surface provider for the camera preview.
 * @param lifecycleOwner The lifecycle owner to bind the camera to.
 * @param imageAnalyzer The analyzer to process camera frames.
 */
class CameraManager(
    private val context: Context,
    private val finderView: Preview.SurfaceProvider,
    private val lifecycleOwner: LifecycleOwner,
    private val imageAnalyzer: ImageAnalysis.Analyzer
) {

    private var cameraProvider: ProcessCameraProvider? = null
    private val cameraExecutor: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
        cameraProviderFuture.addListener({
            cameraProvider = cameraProviderFuture.get()
            bindCameraUseCases()
        }, ContextCompat.getMainExecutor(context))
    }

    private fun bindCameraUseCases() {
        val cameraProvider = cameraProvider ?: throw IllegalStateException("Camera initialization failed.")

        val preview = Preview.Builder().build().also {
            it.setSurfaceProvider(finderView)
        }

        val imageAnalysis = ImageAnalysis.Builder()
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()
            .also {
                it.setAnalyzer(cameraExecutor, imageAnalyzer)
            }

        val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

        try {
            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(
                lifecycleOwner,
                cameraSelector,
                preview,
                imageAnalysis
            )
        } catch (exc: Exception) {
            android.util.Log.e("CameraManager", "Use case binding failed", exc)
        }
    }

    fun shutDown() {
        cameraExecutor.shutdown()
    }
}
