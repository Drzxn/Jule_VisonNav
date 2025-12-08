package com.example.indoornavigation.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.indoornavigation.R

class NavigationService : Service() {

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val notification = NotificationCompat.Builder(this, "navigation_channel")
            .setContentTitle("Indoor Navigation")
            .setContentText("Navigation is active.")
            .setSmallIcon(R.mipmap.ic_launcher) // Replace with a real icon
            .build()

        startForeground(1, notification)

        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}
