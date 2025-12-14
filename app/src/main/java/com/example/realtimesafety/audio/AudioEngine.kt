package com.example.realtimesafety.audio

import android.content.Context
import android.speech.tts.TextToSpeech
import java.util.Locale

/**
 * Manages the TextToSpeech engine and provides audio feedback.
 *
 * @param context The application context.
 */
class AudioEngine(
    context: Context
) : TextToSpeech.OnInitListener {
    private val tts: TextToSpeech = TextToSpeech(context, this)
    private var lastSpokenTime = 0L
    private val cooldown = 3000 // 3 seconds

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            tts.language = Locale.US
        }
    }

    fun speak(text: String, isEmergency: Boolean = false) {
        val currentTime = System.currentTimeMillis()
        if (isEmergency || currentTime - lastSpokenTime > cooldown) {
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "")
            lastSpokenTime = currentTime
        }
    }

    fun getDirectionalCue(x: Float, width: Int): String {
        val center = width / 2
        val quarter = width / 4
        return when {
            x < center - quarter -> "to your left"
            x > center + quarter -> "to your right"
            else -> "ahead"
        }
    }

    fun shutdown() {
        tts.stop()
        tts.shutdown()
    }
}
