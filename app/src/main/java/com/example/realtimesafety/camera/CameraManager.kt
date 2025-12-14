package com.example.realtimesafety.camera

import android.content.Context
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.google.common.util.concurrent.ListenableFuture
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 * Manages the camera setup and frame delivery for analysis.
 *
 * @param context The application context.
 * @param imageAnalyzer The analyzer to process camera frames.
 */
class CameraManager(
    private val context: Context,
    private val imageAnalyzer: ImageAnalysis.Analyzer,
    private val lifecycleOwner: ServiceLifecycleOwner
) {
    private var cameraProvider: ProcessCameraProvider? = null
    private val cameraExecutor: ExecutorService = Executors.newSingleThreadExecutor()
    private lateinit var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>


    fun startCamera(onSurfaceProviderReady: (Preview.SurfaceProvider) -> Unit) {
        cameraProviderFuture = ProcessCameraProvider.getInstance(context)
        cameraProviderFuture.addListener({
            cameraProvider = cameraProviderFuture.get()
            bindCameraUseCases(onSurfaceProviderReady)
        }, ContextCompat.getMainExecutor(context))
    }

    private fun bindCameraUseCases(onSurfaceProviderReady: (Preview.SurfaceProvider) -> Unit) {
        val cameraProvider = cameraProvider ?: throw IllegalStateException("Camera initialization failed.")

        val preview = Preview.Builder().build()
        onSurfaceProviderReady(preview.surfaceProvider)

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
            // Handle exceptions
        }
    }

    fun shutDown() {
        cameraExecutor.shutdown()
        cameraProvider?.unbindAll()
    }
}
