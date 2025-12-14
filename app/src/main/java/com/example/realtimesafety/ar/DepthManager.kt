package com.example.realtimesafety.ar

import android.app.Activity
import com.google.ar.core.ArCoreApk
import com.google.ar.core.Session
import com.google.ar.core.Config
import com.google.ar.core.Frame
import com.google.ar.core.exceptions.UnavailableException

/**
 * Manages the ARCore session and provides depth information.
 */
class DepthManager {
    private var session: Session? = null

    fun setup(activity: Activity) {
        if (session == null) {
            try {
                if (ArCoreApk.getInstance().requestInstall(activity, true) == ArCoreApk.InstallStatus.INSTALLED) {
                    session = Session(activity)
                    val config = Config(session)
                    config.depthMode = Config.DepthMode.AUTOMATIC
                    config.updateMode = Config.UpdateMode.LATEST_CAMERA_IMAGE
                    session?.configure(config)
                }
            } catch (e: UnavailableException) {
                // Handle ARCore not available
            }
        }
    }

    fun update(): Frame? {
        return session?.update()
    }

    fun getDepth(frame: Frame, x: Int, y: Int): Float {
        try {
            frame.acquireDepthImage().use { depthImage ->
                if (depthImage != null && x >= 0 && y >= 0 && x < depthImage.width && y < depthImage.height) {
                    val depth = depthImage.getPixel(x, y)
                    // Clamp noisy values
                    return if (depth > 0) depth / 1000f else Float.MAX_VALUE
                }
            }
        } catch (e: Exception) {
            // Handle exceptions
        }
        return Float.MAX_VALUE
    }

    fun close() {
        session?.close()
        session = null
    }
}
