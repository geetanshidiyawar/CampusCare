package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddComment
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.example.ui.components.CategoryChip
import com.example.ui.components.StatusBadge
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
import com.example.ui.theme.StatusResolvedGreen
import com.example.ui.theme.StatusResolvedGreenBg
import com.example.viewmodel.ComplaintViewModel
import com.example.viewmodel.SubmissionState

@Composable
fun SubmitComplaintScreen(
    viewModel: ComplaintViewModel,
    onNavigateToAllComplaints: () -> Unit,
    modifier: Modifier = Modifier
) {
    val selectedCategory by viewModel.formCategory.collectAsState()
    val description by viewModel.formDescription.collectAsState()
    val submissionState by viewModel.submissionState.collectAsState()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("submit_complaint_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Form Title & Introduction
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = androidx.compose.foundation.BorderStroke(1.dp, Slate200),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(18.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(BlueContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AddComment,
                            contentDescription = null,
                            tint = BluePrimary,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Column {
                        Text(
                            text = "Submit Campus Complaint",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Slate900
                        )
                        Text(
                            text = "Log an issue to campus facilities & administrators.",
                            fontSize = 12.sp,
                            color = Slate500
                        )
                    }
                }
            }
        }

        // Success Confirmation Banner (Section 5.9)
        if (submissionState is SubmissionState.Success) {
            val success = submissionState as SubmissionState.Success
            item {
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = StatusResolvedGreenBg),
                    border = androidx.compose.foundation.BorderStroke(1.5.dp, StatusResolvedGreen),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("submission_success_banner")
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Success",
                                tint = StatusResolvedGreen,
                                modifier = Modifier.size(22.dp)
                            )
                            Text(
                                text = "Complaint Submitted Successfully!",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF065F46)
                            )
                        }

                        Text(
                            text = "Assigned Complaint ID: ${success.complaint.id}\nInitial Status: Raised • Category: ${success.complaint.category}",
                            fontSize = 13.sp,
                            color = Color(0xFF064E3B),
                            fontWeight = FontWeight.Medium
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = onNavigateToAllComplaints,
                                colors = ButtonDefaults.buttonColors(containerColor = BluePrimary),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.weight(1f).testTag("view_in_all_complaints_button")
                            ) {
                                Text("View in All Complaints", fontSize = 12.sp)
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(
                                    imageVector = Icons.AutoMirroredFilledArrow,
                                    contentDescription = null,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                            OutlinedButton(
                                onClick = { viewModel.resetSubmissionState() },
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.testTag("submit_another_button")
                            ) {
                                Text("Submit Another", fontSize = 12.sp, color = BluePrimary)
                            }
                        }
                    }
                }
            }
        }

        // Submission Form Container
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = androidx.compose.foundation.BorderStroke(1.dp, Slate200),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Category Selection (Section 5)
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = "Category",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Slate900
                            )
                            Text(
                                text = "*",
                                color = PriorityRed,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }

                        Text(
                            text = "Select the facility area. Each category has an urgency weight.",
                            fontSize = 12.sp,
                            color = Slate500
                        )

                        // Category Selector Tiles
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            ComplaintCategories.ALL.forEach { cat ->
                                val isSelected = selectedCategory == cat
                                val icon: ImageVector = when (cat) {
                                    ComplaintCategories.WIFI -> Icons.Default.Wifi
                                    ComplaintCategories.HOSTEL -> Icons.Default.Home
                                    ComplaintCategories.CLASSROOM -> Icons.Default.School
                                    ComplaintCategories.LABORATORY -> Icons.Default.Science
                                    else -> Icons.Default.Info
                                }
                                val weight = ComplaintCategories.getWeight(cat)

                                Surface(
                                    shape = RoundedCornerShape(10.dp),
                                    color = if (isSelected) BlueContainer else Slate100,
                                    border = androidx.compose.foundation.BorderStroke(
                                        width = if (isSelected) 1.5.dp else 1.dp,
                                        color = if (isSelected) BluePrimary else Slate200
                                    ),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { viewModel.setFormCategory(cat) }
                                        .testTag("select_category_$cat")
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                                        ) {
                                            Icon(
                                                imageVector = icon,
                                                contentDescription = cat,
                                                tint = if (isSelected) BluePrimary else Slate700,
                                                modifier = Modifier.size(18.dp)
                                            )
                                            Text(
                                                text = cat,
                                                fontSize = 14.sp,
                                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                                color = if (isSelected) BluePrimary else Slate900
                                            )
                                        }

                                        Surface(
                                            shape = RoundedCornerShape(6.dp),
                                            color = if (isSelected) Color(0xFFBFDBFE) else Color(0xFFE2E8F0)
                                        ) {
                                            Text(
                                                text = "Weight: $weight",
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.SemiBold,
                                                color = if (isSelected) BluePrimary else Slate700,
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // Description Multiline Input (Section 5)
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = "Description",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Slate900
                            )
                            Text(
                                text = "*",
                                color = PriorityRed,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }

                        OutlinedTextField(
                            value = description,
                            onValueChange = { viewModel.setFormDescription(it) },
                            placeholder = {
                                Text(
                                    "Describe the issue clearly (e.g. location, specific problem, equipment affected)...",
                                    fontSize = 13.sp,
                                    color = Slate500
                                )
                            },
                            minLines = 4,
                            maxLines = 6,
                            shape = RoundedCornerShape(10.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = BluePrimary,
                                unfocusedBorderColor = Slate200,
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("complaint_description_input")
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Initial status will automatically be set to 'Raised'.",
                                fontSize = 11.sp,
                                color = Slate500
                            )
                            Text(
                                text = "${description.length} chars",
                                fontSize = 11.sp,
                                color = Slate500
                            )
                        }
                    }

                    // Validation Error Message (Section 5)
                    AnimatedVisibility(visible = submissionState is SubmissionState.Error) {
                        if (submissionState is SubmissionState.Error) {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = PriorityRedBg,
                                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFCA5A5)),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("form_validation_error")
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.ErrorOutline,
                                        contentDescription = null,
                                        tint = PriorityRed,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Text(
                                        text = (submissionState as SubmissionState.Error).message,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = PriorityRed
                                    )
                                }
                            }
                        }
                    }

                    // Submit Button (Section 5)
                    Button(
                        onClick = { viewModel.submitComplaint() },
                        colors = ButtonDefaults.buttonColors(containerColor = BluePrimary),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("submit_complaint_form_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Send,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Submit Complaint",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                    }
                }
            }
        }
    }
}

val Icons.AutoMirroredFilledArrow: ImageVector
    get() = Icons.Default.ArrowForward
