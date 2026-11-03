package com.example.model

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.floor

enum class ComplaintStatus(val displayName: String) {
    RAISED("Raised"),
    IN_PROGRESS("In Progress"),
    RESOLVED("Resolved");

    companion object {
        fun fromDisplayName(name: String): ComplaintStatus {
            return entries.firstOrNull { it.displayName.equals(name, ignoreCase = true) } ?: RAISED
        }
    }
}

object ComplaintCategories {
    const val WIFI = "Wi-Fi"
    const val HOSTEL = "Hostel"
    const val CLASSROOM = "Classroom"
    const val LABORATORY = "Laboratory"

    val ALL = listOf(WIFI, HOSTEL, CLASSROOM, LABORATORY)

    fun getWeight(category: String): Int {
        return when (category) {
            WIFI -> 2
            HOSTEL -> 2
            CLASSROOM -> 1
            LABORATORY -> 3
            else -> 1
        }
    }
}

data class Complaint(
    val id: String,
    val category: String,
    val description: String,
    val submittedAt: Long,
    val status: ComplaintStatus
) {
    /**
     * Calculates the hours elapsed since submission.
     */
    fun getHoursPending(currentTime: Long = System.currentTimeMillis()): Long {
        val elapsedMillis = (currentTime - submittedAt).coerceAtLeast(0L)
        return elapsedMillis / (1000L * 60 * 60)
    }

    /**
     * Urgency Score Formula:
     * Urgency Score = Category Weight + floor(Hours Pending / 24)
     */
    fun calculateUrgencyScore(currentTime: Long = System.currentTimeMillis()): Int {
        val weight = ComplaintCategories.getWeight(category)
        val hours = getHoursPending(currentTime)
        val floorDays = floor(hours.toDouble() / 24.0).toInt()
        return weight + floorDays
    }

    /**
     * Readable age display, e.g. "48 hours ago" or "Just now".
     */
    fun getFormattedAge(currentTime: Long = System.currentTimeMillis()): String {
        val hours = getHoursPending(currentTime)
        return when {
            hours == 0L -> {
                val elapsedMinutes = ((currentTime - submittedAt).coerceAtLeast(0L)) / (1000L * 60)
                if (elapsedMinutes < 2) "Just now" else "$elapsedMinutes mins ago"
            }
            hours == 1L -> "1 hour ago"
            hours < 24L -> "$hours hours ago"
            hours == 24L -> "1 day ago"
            hours < 48L -> "1 day ${hours % 24}h ago"
            else -> "${hours / 24} days ago (${hours}h)"
        }
    }

    /**
     * Formatted exact submission date.
     */
    fun getFormattedDate(): String {
        val formatter = SimpleDateFormat("MMM dd, yyyy • hh:mm a", Locale.getDefault())
        return formatter.format(Date(submittedAt))
    }
}
