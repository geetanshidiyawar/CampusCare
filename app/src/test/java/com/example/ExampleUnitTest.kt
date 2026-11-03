package com.example

import com.example.model.Complaint
import com.example.model.ComplaintCategories
import com.example.model.ComplaintStatus
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {

    @Test
    fun urgencyScoring_matchesExactFormula() {
        val now = 1000000000000L
        val oneHour = 3600 * 1000L

        // Wi-Fi complaint, 48 hours old: Score = 2 + floor(48/24) = 4
        val wifiComplaint = Complaint(
            id = "C01",
            category = ComplaintCategories.WIFI,
            description = "Wi-Fi connectivity issue on campus.",
            submittedAt = now - (48 * oneHour),
            status = ComplaintStatus.RAISED
        )
        assertEquals(4, wifiComplaint.calculateUrgencyScore(now))

        // Hostel complaint, 8 hours old: Score = 2 + floor(8/24) = 2
        val hostelComplaint = Complaint(
            id = "C02",
            category = ComplaintCategories.HOSTEL,
            description = "Water supply issue in hostel.",
            submittedAt = now - (8 * oneHour),
            status = ComplaintStatus.IN_PROGRESS
        )
        assertEquals(2, hostelComplaint.calculateUrgencyScore(now))

        // Classroom complaint, 72 hours old: Score = 1 + floor(72/24) = 4
        val classroomComplaint = Complaint(
            id = "C03",
            category = ComplaintCategories.CLASSROOM,
            description = "Classroom projector not working.",
            submittedAt = now - (72 * oneHour),
            status = ComplaintStatus.RAISED
        )
        assertEquals(4, classroomComplaint.calculateUrgencyScore(now))

        // Laboratory complaint, 24 hours old: Score = 3 + floor(24/24) = 4
        val labComplaint = Complaint(
            id = "C04",
            category = ComplaintCategories.LABORATORY,
            description = "Laboratory equipment issue.",
            submittedAt = now - (24 * oneHour),
            status = ComplaintStatus.RESOLVED
        )
        assertEquals(4, labComplaint.calculateUrgencyScore(now))
    }

    @Test
    fun topPrioritySorting_resolvesTiesCorrectly_andExcludesResolved() {
        val now = 1000000000000L
        val oneHour = 3600 * 1000L

        val c01 = Complaint("C01", ComplaintCategories.WIFI, "Wi-Fi issue", now - (48 * oneHour), ComplaintStatus.RAISED) // score = 4, age = 48
        val c02 = Complaint("C02", ComplaintCategories.HOSTEL, "Water issue", now - (8 * oneHour), ComplaintStatus.IN_PROGRESS) // score = 2, age = 8
        val c03 = Complaint("C03", ComplaintCategories.CLASSROOM, "Projector issue", now - (72 * oneHour), ComplaintStatus.RAISED) // score = 4, age = 72
        val c04 = Complaint("C04", ComplaintCategories.LABORATORY, "Lab issue", now - (24 * oneHour), ComplaintStatus.RESOLVED) // resolved, must be excluded!

        val list = listOf(c01, c02, c03, c04)

        val topPriority = list.filter { it.status != ComplaintStatus.RESOLVED }
            .sortedWith(
                compareByDescending<Complaint> { it.calculateUrgencyScore(now) }
                    .thenBy { it.submittedAt } // smaller timestamp = older complaint
                    .thenBy { it.id }
            )
            .take(3)

        assertEquals(3, topPriority.size)
        // C03: score 4, 72h old (older than C01 which is 48h old) -> Rank #1
        assertEquals("C03", topPriority[0].id)
        // C01: score 4, 48h old -> Rank #2
        assertEquals("C01", topPriority[1].id)
        // C02: score 2, 8h old -> Rank #3
        assertEquals("C02", topPriority[2].id)
        // C04 was resolved and must not appear
        assertFalse(topPriority.any { it.id == "C04" })
    }

    @Test
    fun categoryWeights_conformToSpecification() {
        assertEquals(2, ComplaintCategories.getWeight(ComplaintCategories.WIFI))
        assertEquals(2, ComplaintCategories.getWeight(ComplaintCategories.HOSTEL))
        assertEquals(1, ComplaintCategories.getWeight(ComplaintCategories.CLASSROOM))
        assertEquals(3, ComplaintCategories.getWeight(ComplaintCategories.LABORATORY))
    }
}
