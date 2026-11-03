package com.example.data

import android.content.Context
import android.content.SharedPreferences
import com.example.model.Complaint
import com.example.model.ComplaintCategories
import com.example.model.ComplaintStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.json.JSONArray
import org.json.JSONObject

class ComplaintStorage(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    private val _complaints = MutableStateFlow<List<Complaint>>(emptyList())
    val complaints: StateFlow<List<Complaint>> = _complaints.asStateFlow()

    init {
        loadOrInitializeComplaints()
    }

    private fun loadOrInitializeComplaints() {
        val hasInitialized = prefs.getBoolean(KEY_INITIALIZED, false)
        val rawJson = prefs.getString(KEY_COMPLAINTS, null)

        if (!hasInitialized || rawJson == null) {
            // First time launch: Initialize with the required 4 sample records
            val sampleData = createInitialSampleData()
            saveComplaintsInternal(sampleData)
            prefs.edit().putBoolean(KEY_INITIALIZED, true).apply()
            _complaints.value = sampleData
        } else {
            val list = parseComplaints(rawJson)
            _complaints.value = list
        }
    }

    fun addComplaint(category: String, description: String): Complaint {
        val currentList = _complaints.value.toMutableList()
        val nextId = generateNextUniqueId(currentList)
        val now = System.currentTimeMillis()
        val newComplaint = Complaint(
            id = nextId,
            category = category,
            description = description.trim(),
            submittedAt = now,
            status = ComplaintStatus.RAISED
        )
        // Add newest at the beginning of the list
        currentList.add(0, newComplaint)
        saveComplaintsInternal(currentList)
        _complaints.value = currentList
        return newComplaint
    }

    fun updateStatus(complaintId: String, newStatus: ComplaintStatus) {
        val currentList = _complaints.value.map {
            if (it.id == complaintId) {
                it.copy(status = newStatus)
            } else {
                it
            }
        }
        saveComplaintsInternal(currentList)
        _complaints.value = currentList
    }

    fun deleteComplaint(complaintId: String) {
        val currentList = _complaints.value.filter { it.id != complaintId }
        saveComplaintsInternal(currentList)
        _complaints.value = currentList
    }

    fun resetToSampleData() {
        val sampleData = createInitialSampleData()
        saveComplaintsInternal(sampleData)
        prefs.edit().putBoolean(KEY_INITIALIZED, true).apply()
        _complaints.value = sampleData
    }

    private fun saveComplaintsInternal(list: List<Complaint>) {
        val jsonArray = JSONArray()
        list.forEach { complaint ->
            val obj = JSONObject().apply {
                put("id", complaint.id)
                put("category", complaint.category)
                put("description", complaint.description)
                put("submittedAt", complaint.submittedAt)
                put("status", complaint.status.name)
            }
            jsonArray.put(obj)
        }
        prefs.edit().putString(KEY_COMPLAINTS, jsonArray.toString()).apply()
    }

    private fun parseComplaints(jsonString: String): List<Complaint> {
        val result = mutableListOf<Complaint>()
        try {
            val jsonArray = JSONArray(jsonString)
            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.getJSONObject(i)
                val statusStr = obj.optString("status", ComplaintStatus.RAISED.name)
                val status = try {
                    ComplaintStatus.valueOf(statusStr)
                } catch (e: Exception) {
                    ComplaintStatus.RAISED
                }
                result.add(
                    Complaint(
                        id = obj.getString("id"),
                        category = obj.getString("category"),
                        description = obj.getString("description"),
                        submittedAt = obj.getLong("submittedAt"),
                        status = status
                    )
                )
            }
        } catch (e: Exception) {
            // In case of corrupt data, fallback gracefully
            return createInitialSampleData()
        }
        return result
    }

    private fun generateNextUniqueId(existingList: List<Complaint>): String {
        var highestNumber = prefs.getInt(KEY_HIGHEST_ID_COUNTER, 4)
        for (c in existingList) {
            val match = REGEX_ID.find(c.id)
            if (match != null) {
                val num = match.groupValues[1].toIntOrNull() ?: 0
                if (num > highestNumber) {
                    highestNumber = num
                }
            }
        }
        val nextNumber = highestNumber + 1
        prefs.edit().putInt(KEY_HIGHEST_ID_COUNTER, nextNumber).apply()
        return "C%02d".format(nextNumber)
    }

    private fun createInitialSampleData(): List<Complaint> {
        val now = System.currentTimeMillis()
        val oneHourMillis = 3600 * 1000L

        return listOf(
            Complaint(
                id = "C01",
                category = ComplaintCategories.WIFI,
                description = "Wi-Fi connectivity issue on campus.",
                submittedAt = now - (48 * oneHourMillis),
                status = ComplaintStatus.RAISED
            ),
            Complaint(
                id = "C02",
                category = ComplaintCategories.HOSTEL,
                description = "Water supply issue in hostel.",
                submittedAt = now - (8 * oneHourMillis),
                status = ComplaintStatus.IN_PROGRESS
            ),
            Complaint(
                id = "C03",
                category = ComplaintCategories.CLASSROOM,
                description = "Classroom projector not working.",
                submittedAt = now - (72 * oneHourMillis),
                status = ComplaintStatus.RAISED
            ),
            Complaint(
                id = "C04",
                category = ComplaintCategories.LABORATORY,
                description = "Laboratory equipment issue.",
                submittedAt = now - (24 * oneHourMillis),
                status = ComplaintStatus.RESOLVED
            )
        )
    }

    companion object {
        const val PREFS_NAME = "campuscare_prefs"
        const val KEY_COMPLAINTS = "campuscare_complaints"
        const val KEY_INITIALIZED = "campuscare_initialized"
        const val KEY_HIGHEST_ID_COUNTER = "campuscare_highest_id"
        private val REGEX_ID = Regex("""[Cc](\d+)""")
    }
}
