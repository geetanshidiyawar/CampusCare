package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.HourglassBottom
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Pending
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ComplaintCategories
import com.example.model.ComplaintStatus
import com.example.ui.theme.BluePrimary
import com.example.ui.theme.PriorityRed
import com.example.ui.theme.PriorityRedBg
import com.example.ui.theme.Slate200
import com.example.ui.theme.Slate500
import com.example.ui.theme.Slate700
import com.example.ui.theme.Slate900
import com.example.ui.theme.StatusInProgressBlue
import com.example.ui.theme.StatusInProgressBlueBg
import com.example.ui.theme.StatusRaisedAmber
import com.example.ui.theme.StatusRaisedAmberBg
import com.example.ui.theme.StatusResolvedGreen
import com.example.ui.theme.StatusResolvedGreenBg

@Composable
fun StatusBadge(
    status: ComplaintStatus,
    modifier: Modifier = Modifier
) {
    val (bgColor, textColor, icon) = when (status) {
        ComplaintStatus.RAISED -> Triple(StatusRaisedAmberBg, StatusRaisedAmber, Icons.Default.Pending)
        ComplaintStatus.IN_PROGRESS -> Triple(StatusInProgressBlueBg, StatusInProgressBlue, Icons.Default.HourglassBottom)
        ComplaintStatus.RESOLVED -> Triple(StatusResolvedGreenBg, StatusResolvedGreen, Icons.Default.CheckCircle)
    }

    Row(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(bgColor)
            .padding(horizontal = 10.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = textColor,
            modifier = Modifier.size(14.dp)
        )
        Text(
            text = status.displayName,
            color = textColor,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
fun UrgencyScoreBadge(
    score: Int,
    modifier: Modifier = Modifier,
    isTopPriority: Boolean = false
) {
    val (bgColor, textColor) = when {
        score >= 4 -> Pair(PriorityRedBg, PriorityRed)
        score >= 2 -> Pair(StatusRaisedAmberBg, StatusRaisedAmber)
        else -> Pair(StatusInProgressBlueBg, StatusInProgressBlue)
    }

    Row(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(bgColor)
            .border(
                width = if (isTopPriority) 1.5.dp else 1.dp,
                color = if (isTopPriority) textColor else Color.Transparent,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Box(
            modifier = Modifier
                .size(7.dp)
                .clip(CircleShape)
                .background(textColor)
        )
        Text(
            text = "Urgency $score",
            color = textColor,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun CategoryChip(
    category: String,
    modifier: Modifier = Modifier,
    showWeight: Boolean = false
) {
    val icon: ImageVector = when (category) {
        ComplaintCategories.WIFI -> Icons.Default.Wifi
        ComplaintCategories.HOSTEL -> Icons.Default.Home
        ComplaintCategories.CLASSROOM -> Icons.Default.School
        ComplaintCategories.LABORATORY -> Icons.Default.Science
        else -> Icons.Default.Info
    }
    val weight = ComplaintCategories.getWeight(category)

    Row(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFFF1F5F9))
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = category,
            tint = Slate700,
            modifier = Modifier.size(14.dp)
        )
        Text(
            text = if (showWeight) "$category (W: $weight)" else category,
            color = Slate900,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun StatusDropdownSelector(
    currentStatus: ComplaintStatus,
    onStatusSelected: (ComplaintStatus) -> Unit,
    modifier: Modifier = Modifier,
    testTag: String = "status_selector"
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        Surface(
            shape = RoundedCornerShape(8.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, Slate200),
            color = Color.White,
            modifier = Modifier
                .clickable { expanded = true }
                .testTag(testTag)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                StatusBadge(status = currentStatus)
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Change Status",
                    tint = Slate500,
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(Color.White)
        ) {
            ComplaintStatus.entries.forEach { status ->
                DropdownMenuItem(
                    text = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            StatusBadge(status = status)
                            if (status == currentStatus) {
                                Text(
                                    text = "(Current)",
                                    fontSize = 11.sp,
                                    color = Slate500
                                )
                            }
                        }
                    },
                    onClick = {
                        expanded = false
                        if (status != currentStatus) {
                            onStatusSelected(status)
                        }
                    },
                    modifier = Modifier.testTag("status_option_${status.name}")
                )
            }
        }
    }
}

@Composable
fun EmptyStateCard(
    title: String,
    subtitle: String,
    icon: ImageVector = Icons.Default.Info,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = Color.White,
        border = androidx.compose.foundation.BorderStroke(1.dp, Slate200),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(54.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFF1F5F9)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = BluePrimary,
                    modifier = Modifier.size(28.dp)
                )
            }
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Slate900
            )
            Text(
                text = subtitle,
                fontSize = 13.sp,
                color = Slate500,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }
}
