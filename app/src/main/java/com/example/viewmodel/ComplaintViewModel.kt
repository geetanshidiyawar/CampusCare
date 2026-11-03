package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.ComplaintStorage
import com.example.model.Complaint
import com.example.model.ComplaintCategories
import com.example.model.ComplaintStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

sealed class SubmissionState {
    data object Idle : SubmissionState()
    data class Error(val message: String) : SubmissionState()
    data class Success(val complaint: Complaint) : SubmissionState()
}

data class AnalyticsData(
    val categoryCounts: Map<String, Int>,
    val statusCounts: Map<ComplaintStatus, Int>,
    val totalCount: Int
)

class ComplaintViewModel(application: Application) : AndroidViewModel(application) {

    private val storage = ComplaintStorage(application)

    val complaints: StateFlow<List<Complaint>> = storage.complaints

    // Filter states
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedCategory = MutableStateFlow("All Categories")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    private val _selectedStatus = MutableStateFlow("All Statuses")
    val selectedStatus: StateFlow<String> = _selectedStatus.asStateFlow()

    // Form submission states
    private val _formCategory = MutableStateFlow("")
    val formCategory: StateFlow<String> = _formCategory.asStateFlow()

    private val _formDescription = MutableStateFlow("")
    val formDescription: StateFlow<String> = _formDescription.asStateFlow()

    private val _submissionState = MutableStateFlow<SubmissionState>(SubmissionState.Idle)
    val submissionState: StateFlow<SubmissionState> = _submissionState.asStateFlow()

    // Top 3 Priority Complaints:
    // 1. Unresolved complaints only (Raised or In Progress; exclude Resolved)
    // 2. Higher urgency score first
    // 3. If scores are equal, older complaint first (smaller submittedAt)
    // 4. If both score and age are equal, lower complaint ID first
    val topPriorityComplaints: StateFlow<List<Complaint>> = complaints.combine(_searchQuery) { list, _ ->
        val now = System.currentTimeMillis()
        list.filter { it.status != ComplaintStatus.RESOLVED }
            .sortedWith(
                compareByDescending<Complaint> { it.calculateUrgencyScore(now) }
                    .thenBy { it.submittedAt }
                    .thenBy { it.id }
            )
            .take(3)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Filtered Complaints for All Complaints screen
    val filteredComplaints: StateFlow<List<Complaint>> = combine(
        complaints,
        _searchQuery,
        _selectedCategory,
        _selectedStatus
    ) { list, query, category, status ->
        val cleanQuery = query.trim().lowercase()

        list.filter { complaint ->
            val matchesCategory = (category == "All Categories") || (complaint.category == category)
            val matchesStatus = (status == "All Statuses") || (complaint.status.displayName == status)
            val matchesQuery = cleanQuery.isEmpty() ||
                    complaint.id.lowercase().contains(cleanQuery) ||
                    complaint.description.lowercase().contains(cleanQuery) ||
                    complaint.category.lowercase().contains(cleanQuery)

            matchesCategory && matchesStatus && matchesQuery
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Summary counts
    val summaryCounts: StateFlow<Triple<Int, Int, Int>> = complaints.combine(_searchQuery) { list, _ ->
        val total = list.size
        val pending = list.count { it.status != ComplaintStatus.RESOLVED }
        val resolved = list.count { it.status == ComplaintStatus.RESOLVED }
        Triple(total, pending, resolved)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), Triple(0, 0, 0))

    // Analytics data
    val analyticsData: StateFlow<AnalyticsData> = complaints.combine(_searchQuery) { list, _ ->
        val catMap = ComplaintCategories.ALL.associateWith { cat ->
            list.count { it.category == cat }
        }
        val statusMap = ComplaintStatus.entries.associateWith { st ->
            list.count { it.status == st }
        }
        AnalyticsData(
            categoryCounts = catMap,
            statusCounts = statusMap,
            totalCount = list.size
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        AnalyticsData(emptyMap(), emptyMap(), 0)
    )

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setSelectedCategory(category: String) {
        _selectedCategory.value = category
    }

    fun setSelectedStatus(status: String) {
        _selectedStatus.value = status
    }

    fun clearFilters() {
        _searchQuery.value = ""
        _selectedCategory.value = "All Categories"
        _selectedStatus.value = "All Statuses"
    }

    fun setFormCategory(cat: String) {
        _formCategory.value = cat
        if (_submissionState.value is SubmissionState.Error) {
            _submissionState.value = SubmissionState.Idle
        }
    }

    fun setFormDescription(desc: String) {
        _formDescription.value = desc
        if (_submissionState.value is SubmissionState.Error) {
            _submissionState.value = SubmissionState.Idle
        }
    }

    fun resetSubmissionState() {
        _submissionState.value = SubmissionState.Idle
    }

    fun submitComplaint(): Boolean {
        val category = _formCategory.value.trim()
        val description = _formDescription.value.trim()

        if (category.isEmpty()) {
            _submissionState.value = SubmissionState.Error("Please select a complaint category.")
            return false
        }
        if (description.isEmpty()) {
            _submissionState.value = SubmissionState.Error("Please provide a description of the issue.")
            return false
        }
        if (description.length < 5) {
            _submissionState.value = SubmissionState.Error("Description must be at least 5 characters.")
            return false
        }

        val created = storage.addComplaint(category, description)
        _formCategory.value = ""
        _formDescription.value = ""
        _submissionState.value = SubmissionState.Success(created)
        return true
    }

    fun updateStatus(complaintId: String, newStatus: ComplaintStatus) {
        storage.updateStatus(complaintId, newStatus)
    }

    fun deleteComplaint(complaintId: String) {
        storage.deleteComplaint(complaintId)
    }

    fun resetToSampleData() {
        storage.resetToSampleData()
        clearFilters()
    }
}
