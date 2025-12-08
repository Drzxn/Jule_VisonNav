package com.example.indoornavigation.feedback

import android.content.Context
import android.os.Vibrator
import android.os.Build
import android.os.VibrationEffect

class HapticEngine(
    context: Context
) {
    private val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

    fun vibrate(duration: Long) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(duration, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            vibrator.vibrate(duration)
        }
    }

    fun vibratePattern(pattern: LongArray, repeat: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createWaveform(pattern, repeat))
        } else {
            vibrator.vibrate(pattern, repeat)
        }
    }

    fun cancel() {
        vibrator.cancel()
    }
}
