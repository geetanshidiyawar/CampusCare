package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.FiberManualRecord
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.PendingActions
import androidx.compose.material.icons.filled.PriorityHigh
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Complaint
import com.example.model.ComplaintCategories
import com.example.model.ComplaintStatus
import com.example.ui.components.CategoryChip
import com.example.ui.components.EmptyStateCard
import com.example.ui.components.StatusBadge
import com.example.ui.components.StatusDropdownSelector
import com.example.ui.components.UrgencyScoreBadge
import com.example.ui.theme.BlueContainer
import com.example.ui.theme.BluePrimary
import com.example.ui.theme.BlueSecondary
import com.example.ui.theme.PriorityRed
import com.example.ui.theme.PriorityRedBg
import com.example.ui.theme.Slate100
import com.example.ui.theme.Slate200
import com.example.ui.theme.Slate500
import com.example.ui.theme.Slate700
import com.example.ui.theme.Slate900
import com.example.ui.theme.StatusInProgressBlue
import com.example.ui.theme.StatusRaisedAmber
import com.example.ui.theme.StatusRaisedAmberBg
import com.example.ui.theme.StatusResolvedGreen
import com.example.ui.theme.StatusResolvedGreenBg
import com.example.viewmodel.ComplaintViewModel

@Composable
fun DashboardScreen(
    viewModel: ComplaintViewModel,
    onNavigateToSubmit: () -> Unit,
    onNavigateToAllComplaints: () -> Unit,
    modifier: Modifier = Modifier
) {
    val summaryCounts by viewModel.summaryCounts.collectAsState()
    val topPriority by viewModel.topPriorityComplaints.collectAsState()
    val analytics by viewModel.analyticsData.collectAsState()
    var showFormulaInfo by remember { mutableStateOf(false) }

    val (totalCount, pendingCount, resolvedCount) = summaryCounts

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("dashboard_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // Dashboard Header (Section 4.B)
        item {
            DashboardHeader(
                onNavigateToSubmit = onNavigateToSubmit
            )
        }

        // Summary Cards (Section 4.C)
        item {
            SummaryCardsRow(
                total = totalCount,
                pending = pendingCount,
                resolved = resolvedCount
            )
        }

        // Top 3 Priority Complaints Header & Formula Explanation (Section 11 & 12)
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(PriorityRedBg),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Speed,
                                contentDescription = null,
                                tint = PriorityRed,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Column {
                            Text(
                                text = "Top 3 Priority Complaints",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Slate900
                            )
                            Text(
                                text = "Unresolved issues ranked by urgency algorithm",
                                fontSize = 12.sp,
                                color = Slate500
                            )
                        }
                    }

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = if (showFormulaInfo) BlueContainer else Slate100,
                        modifier = Modifier
                            .clickable { showFormulaInfo = !showFormulaInfo }
                            .testTag("formula_info_button")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = "Algorithm Info",
                                tint = if (showFormulaInfo) BluePrimary else Slate700,
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                text = "Formula",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = if (showFormulaInfo) BluePrimary else Slate700
                            )
                        }
                    }
                }

                // Interactive Formula Dropdown Explanation
                AnimatedVisibility(visible = showFormulaInfo) {
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = BlueContainer),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFBFDBFE)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(14.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = "Urgency Scoring Algorithm:",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = BluePrimary
                            )
                            Text(
                                text = "Urgency Score = Category Weight + floor(Hours Pending / 24)",
                                fontWeight = FontWeight.Medium,
                                fontSize = 12.sp,
                                color = Slate900
                            )
                            Text(
                                text = "Category Weights: Laboratory (3) • Wi-Fi (2) • Hostel (2) • Classroom (1)",
                                fontSize = 11.sp,
                                color = Slate700
                            )
                            Text(
                                text = "• Tie-breaker rules: Higher score > Older complaint > Lower ID\n• Resolved complaints are immediately excluded from priority queue.",
                                fontSize = 11.sp,
                                color = Slate500
                            )
                        }
                    }
                }
            }
        }

        // Top 3 Priority Complaints List / Empty State
        if (topPriority.isEmpty()) {
            item {
                EmptyStateCard(
                    title = "All complaints have been resolved. Great work!",
                    subtitle = "There are no unresolved complaints currently pending on campus.",
                    icon = Icons.Default.CheckCircle,
                    modifier = Modifier.fillMaxWidth().testTag("priority_empty_state")
                )
            }
        } else {
            itemsIndexed(topPriority) { index, complaint ->
                val isHighest = index == 0
                PriorityComplaintCard(
                    complaint = complaint,
                    rank = index + 1,
                    isHighestPriority = isHighest,
                    onStatusChange = { newStatus ->
                        viewModel.updateStatus(complaint.id, newStatus)
                    }
                )
            }
        }

        // Analytics Section (Section 15)
        item {
            DashboardAnalyticsSection(
                analytics = analytics,
                onViewAll = onNavigateToAllComplaints
            )
        }
    }
}

@Composable
fun DashboardHeader(
    onNavigateToSubmit: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.dp, Slate200),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Campus Complaint Dashboard",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Slate900
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Monitor, manage, and prioritize campus complaints.",
                        fontSize = 13.sp,
                        color = Slate500
                    )
                }
            }

            Button(
                onClick = onNavigateToSubmit,
                colors = ButtonDefaults.buttonColors(containerColor = BluePrimary),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("submit_new_complaint_header_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Submit New Complaint",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
fun SummaryCardsRow(
    total: Int,
    pending: Int,
    resolved: Int,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            SummaryCard(
                title = "Total Complaints",
                count = total,
                icon = Icons.Default.Assignment,
                iconTint = BluePrimary,
                iconBg = BlueContainer,
                modifier = Modifier.weight(1f).testTag("summary_card_total")
            )
            SummaryCard(
                title = "Pending",
                count = pending,
                icon = Icons.Default.PendingActions,
                iconTint = StatusRaisedAmber,
                iconBg = StatusRaisedAmberBg,
                modifier = Modifier.weight(1f).testTag("summary_card_pending")
            )
            SummaryCard(
                title = "Resolved",
                count = resolved,
                icon = Icons.Default.CheckCircle,
                iconTint = StatusResolvedGreen,
                iconBg = StatusResolvedGreenBg,
                modifier = Modifier.weight(1f).testTag("summary_card_resolved")
            )
        }
    }
}

@Composable
fun SummaryCard(
    title: String,
    count: Int,
    icon: ImageVector,
    iconTint: Color,
    iconBg: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = Color.White,
        border = androidx.compose.foundation.BorderStroke(1.dp, Slate200),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(iconBg),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(18.dp)
                )
            }

            Text(
                text = count.toString(),
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Slate900
            )

            Text(
                text = title,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                color = Slate500,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun PriorityComplaintCard(
    complaint: Complaint,
    rank: Int,
    isHighestPriority: Boolean,
    onStatusChange: (ComplaintStatus) -> Unit,
    modifier: Modifier = Modifier
) {
    val urgencyScore = complaint.calculateUrgencyScore()
    val hoursPending = complaint.getHoursPending()

    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isHighestPriority) Color(0xFFFFFBEB) else Color.White
        ),
        border = androidx.compose.foundation.BorderStroke(
            width = if (isHighestPriority) 1.5.dp else 1.dp,
            color = if (isHighestPriority) StatusRaisedAmber else Slate200
        ),
        modifier = modifier
            .fillMaxWidth()
            .testTag("priority_card_${complaint.id}")
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Header Row: Rank badge, ID, Category, Urgency
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(if (isHighestPriority) StatusRaisedAmber else Slate700),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "#$rank",
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Text(
                        text = complaint.id,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Slate900
                    )

                    CategoryChip(category = complaint.category)
                }

                UrgencyScoreBadge(score = urgencyScore, isTopPriority = isHighestPriority)
            }

            if (isHighestPriority) {
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = PriorityRedBg,
                    modifier = Modifier.padding(vertical = 2.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.PriorityHigh,
                            contentDescription = null,
                            tint = PriorityRed,
                            modifier = Modifier.size(12.dp)
                        )
                        Text(
                            text = "HIGHEST URGENCY QUEUE ITEM",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = PriorityRed
                        )
                    }
                }
            }

            // Description
            Text(
                text = complaint.description,
                fontSize = 14.sp,
                color = Slate900,
                fontWeight = FontWeight.Normal,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )

            // Details and Action row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.HourglassTop,
                        contentDescription = null,
                        tint = Slate500,
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = "${hoursPending}h pending",
                        fontSize = 12.sp,
                        color = Slate500,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "• ${complaint.getFormattedAge()}",
                        fontSize = 12.sp,
                        color = Slate500
                    )
                }

                StatusDropdownSelector(
                    currentStatus = complaint.status,
                    onStatusSelected = onStatusChange,
                    testTag = "priority_status_selector_${complaint.id}"
                )
            }
        }
    }
}

@Composable
fun DashboardAnalyticsSection(
    analytics: com.example.viewmodel.AnalyticsData,
    onViewAll: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.dp, Slate200),
        modifier = modifier
            .fillMaxWidth()
            .testTag("dashboard_analytics_section")
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Assessment,
                        contentDescription = null,
                        tint = BluePrimary,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = "Campus Analytics Overview",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Slate900
                    )
                }

                Text(
                    text = "View All (${analytics.totalCount})",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = BluePrimary,
                    modifier = Modifier.clickable { onViewAll() }
                )
            }

            // Category breakdown
            Text(
                text = "Complaints by Category",
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = Slate700
            )

            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                ComplaintCategories.ALL.forEach { category ->
                    val count = analytics.categoryCounts[category] ?: 0
                    val fraction = if (analytics.totalCount > 0) count.toFloat() / analytics.totalCount else 0f
                    val percentage = (fraction * 100).toInt()

                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                CategoryChip(category = category, showWeight = true)
                            }
                            Text(
                                text = "$count ($percentage%)",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Slate700
                            )
                        }
                        LinearProgressIndicator(
                            progress = { fraction },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp)),
                            color = when (category) {
                                ComplaintCategories.LABORATORY -> Color(0xFF8B5CF6)
                                ComplaintCategories.WIFI -> BlueSecondary
                                ComplaintCategories.HOSTEL -> Color(0xFF0D9488)
                                else -> Color(0xFFF59E0B)
                            },
                            trackColor = Slate100
                        )
                    }
                }
            }

            // Status Distribution Row
            Text(
                text = "Status Distribution",
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = Slate700
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ComplaintStatus.entries.forEach { status ->
                    val count = analytics.statusCounts[status] ?: 0
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Slate100,
                        modifier = Modifier.weight(1f)
                    ) {
                        Column(
                            modifier = Modifier.padding(10.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            StatusBadge(status = status)
                            Text(
                                text = count.toString(),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Slate900
                            )
                        }
                    }
                }
            }
        }
    }
}
