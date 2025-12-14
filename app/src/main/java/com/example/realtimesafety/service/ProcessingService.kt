package com.example.realtimesafety.service

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.core.app.NotificationCompat
import com.example.realtimesafety.R
import com.example.realtimesafety.audio.AudioEngine
import com.example.realtimesafety.ar.DepthManager
import com.example.realtimesafety.camera.CameraManager
import com.example.realtimesafety.camera.ServiceLifecycleOwner
import com.example.realtimesafety.data.UIState
import com.example.realtimesafety.domain.HazardEngine
import com.example.realtimesafety.ml.ObjectDetector
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@AndroidEntryPoint
class ProcessingService : Service() {

    @Inject lateinit var objectDetector: ObjectDetector
    @Inject lateinit var depthManager: DepthManager
    @Inject lateinit var hazardEngine: HazardEngine
    @Inject lateinit var audioEngine: AudioEngine

    private val binder = LocalBinder()
    private val _uiState = MutableStateFlow(UIState(emptyList(), 1, 1))
    val uiState: StateFlow<UIState> = _uiState

    val surfaceProvider = MutableStateFlow<Preview.SurfaceProvider?>(null)

    private lateinit var cameraManager: CameraManager
    private val serviceLifecycleOwner = ServiceLifecycleOwner()
    private val imageAnalyzer = ImageAnalysis.Analyzer { imageProxy ->
        objectDetector.detect(imageProxy) { detections ->
            _uiState.value = UIState(detections, imageProxy.width, imageProxy.height)
            val frame = depthManager.update()
            if (frame != null && detections.isNotEmpty()) {
                val hazardResult = hazardEngine.checkForHazards(detections, frame, depthManager)
                if (hazardResult != null) {
                    val direction = audioEngine.getDirectionalCue(detections.first().boundingBox.centerX(), imageProxy.width)
                    audioEngine.speak("${hazardResult.message} $direction", hazardResult.isEmergency)
                }
            }
            imageProxy.close()
        }
    }

    inner class LocalBinder : Binder() {
        fun getService(): ProcessingService = this@ProcessingService
    }

    override fun onCreate() {
        super.onCreate()
        serviceLifecycleOwner.onServiceCreated()
        cameraManager = CameraManager(this, imageAnalyzer, serviceLifecycleOwner)
        cameraManager.startCamera {
            surfaceProvider.value = it
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val notification = NotificationCompat.Builder(this, "processing_channel")
            .setContentTitle("Real-Time Safety")
            .setContentText("Processing is active.")
            .setSmallIcon(R.mipmap.ic_launcher)
            .build()

        startForeground(1, notification)
        return START_STICKY
    }

    fun initializeArCore(activity: Activity) {
        depthManager.setup(activity)
    }

    override fun onBind(intent: Intent?): IBinder = binder

    override fun onDestroy() {
        super.onDestroy()
        serviceLifecycleOwner.onServiceDestroyed()
        cameraManager.shutDown()
        depthManager.close()
        audioEngine.shutdown()
    }
}
