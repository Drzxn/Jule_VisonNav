package com.example.realtimesafety.domain.model

/**
 * Represents a hazard event that has been detected by the system.
 *
 * @param objectId The ID of the tracked object that triggered the hazard.
 * @param severity The severity level of the hazard.
 * @param message A human-readable message describing the hazard.
 */
data class HazardEvent(
    val objectId: Int,
    val severity: SeverityLevel,
    val message: String
)

/**
 * Represents the severity of a hazard.
 */
enum class SeverityLevel {
    CRITICAL, // e.g., STOP
    HIGH,     // e.g., Stairs
    MEDIUM,   // e.g., Moving object
    LOW       // e.g., Static obstacle
}
