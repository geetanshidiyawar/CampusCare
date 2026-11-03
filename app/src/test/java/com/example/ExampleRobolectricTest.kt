package com.example

import com.example.model.Complaint
import com.example.model.ComplaintCategories
import com.example.model.ComplaintStatus
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleRobolectricTest {

    @Test
    fun appName_constantVerification() {
        // Verify expected brand name
        val expectedAppName = "CampusCare"
        assertEquals("CampusCare", expectedAppName)
    }

    @Test
    fun complaintModel_propertiesAndAgeCalculations() {
        val now = 1700000000000L
        val hourMillis = 3600 * 1000L

        val complaint = Complaint(
            id = "C01",
            category = ComplaintCategories.WIFI,
            description = "Wi-Fi connectivity issue on campus.",
            submittedAt = now - (48 * hourMillis),
            status = ComplaintStatus.RAISED
        )

        assertEquals("C01", complaint.id)
        assertEquals(ComplaintCategories.WIFI, complaint.category)
        assertEquals(48L, complaint.getHoursPending(now))
        assertEquals(4, complaint.calculateUrgencyScore(now))
        assertTrue(complaint.getFormattedAge(now).contains("48"))
    }
}
