package com.example.realtimesafety.audio

import com.example.realtimesafety.domain.model.HazardEvent
import com.example.realtimesafety.domain.model.SeverityLevel

/**
 * Manages the audio output, enforcing hazard priority and preventing spam.
 *
 * This class receives a list of `HazardEvent`s, selects the most severe one, and decides whether
 * to speak it based on a per-object cooldown and a priority system. Critical alerts will
 * interrupt less severe ones.
 */
class AudioPriorityManager(
    private val audioEngine: AudioEngine
) {
    private val cooldowns = mutableMapOf<Int, Long>()
    private val cooldownPeriod = 3000 // 3 seconds
    private var currentSpokenHazard: HazardEvent? = null

    fun speak(events: List<HazardEvent>) {
        val mostSevereEvent = events.maxByOrNull { it.severity } ?: return

        val currentTime = System.currentTimeMillis()
        val lastSpokenTime = cooldowns[mostSevereEvent.objectId] ?: 0L

        if (currentTime - lastSpokenTime < cooldownPeriod) {
            return
        }

        if (currentSpokenHazard == null || mostSevereEvent.severity >= currentSpokenHazard!!.severity) {
            val isEmergency = mostSevereEvent.severity == SeverityLevel.CRITICAL
            audioEngine.speak(mostSevereEvent.message, isEmergency)
            cooldowns[mostSevereEvent.objectId] = currentTime
            currentSpokenHazard = mostSevereEvent
        }
    }
}
