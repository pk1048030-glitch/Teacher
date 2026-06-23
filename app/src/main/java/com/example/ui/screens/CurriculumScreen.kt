package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.viewmodel.AcademyViewModel
import com.example.data.database.LessonPlan

@Composable
fun CurriculumScreen(viewModel: AcademyViewModel, modifier: Modifier = Modifier) {
    val isHindi by viewModel.isHindi.collectAsStateWithLifecycle()
    val activeClass by viewModel.activeClassLevel.collectAsStateWithLifecycle()
    val localLessonPlans by viewModel.allLessonPlans.collectAsStateWithLifecycle()

    val classOptions = listOf("Nursery", "LKG", "UKG", "Class 1", "Class 2", "Class 3", "Class 4", "Class 5")
    val selectedClassSyllabus = viewModel.curriculumSyllabus.firstOrNull { it.className == activeClass }

    var expandedSubject by remember { mutableStateOf<String?>(null) }
    var showAddPlanDialog by remember { mutableStateOf(false) }

    // Custom Lesson plan creator state
    var planSubject by remember { mutableStateOf("Mathematics") }
    var planTitle by remember { mutableStateOf("") }
    var planObjectives by remember { mutableStateOf("") }
    var planMethodology by remember { mutableStateOf("") }
    var planActivity by remember { mutableStateOf("") }
    var planAssessment by remember { mutableStateOf("") }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Selection Row
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = if (isHindi) "कक्षा का चयन करें / Select Grade Level" else "Select Grade Level",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(classOptions) { className ->
                        val isSelected = className == activeClass
                        FilterChip(
                            selected = isSelected,
                            onClick = {
                                viewModel.activeClassLevel.value = className
                                expandedSubject = null
                            },
                            label = { Text(className, fontWeight = FontWeight.Bold) },
                            modifier = Modifier.testTag("chip_class_$className"),
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.primary,
                                selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                            )
                        )
                    }
                }
            }
        }

        // Selected Class Syllabus details
        if (selectedClassSyllabus != null) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isHindi) "$activeClass पाठ्यक्रम" else "$activeClass Complete Syllabus",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Button(
                        onClick = {
                            planSubject = "Mathematics"
                            planTitle = ""
                            planObjectives = ""
                            planMethodology = ""
                            planActivity = ""
                            planAssessment = ""
                            showAddPlanDialog = true
                        },
                        modifier = Modifier.testTag("add_lesson_plan_btn")
                    ) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = "Add lesson plan")
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(if (isHindi) "पाठ योजना लिखें" else "Write Lesson Plan")
                    }
                }
            }

            // Core Curriculum Subjects List
            val subjects = selectedClassSyllabus.subjects
            if (subjects.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Box(modifier = Modifier.padding(16.dp), contentAlignment = Alignment.Center) {
                            Text(
                                text = if (isHindi) "इस विशेष कक्षा के लिए विषय विवरण नीचे दिए गए हैं।" else "Core foundation guidelines are active for this standard.",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            } else {
                items(subjects.keys.toList()) { subjectName ->
                    val isExpanded = expandedSubject == subjectName
                    val topicList = subjects[subjectName] ?: emptyList()

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("curriculum_subject_$subjectName")
                            .clickable { expandedSubject = if (isExpanded) null else subjectName },
                        colors = CardDefaults.cardColors(
                            containerColor = if (isExpanded) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                            else MaterialTheme.colorScheme.surface
                        ),
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    Icon(
                                        imageVector = when (subjectName) {
                                            "English" -> Icons.Default.Info
                                            "Hindi" -> Icons.Default.Star
                                            "Mathematics" -> Icons.Default.Add
                                            "Science / EVS" -> Icons.Default.Build
                                            "Science" -> Icons.Default.Build
                                            "Social Science" -> Icons.Default.Home
                                            else -> Icons.Default.Info
                                        },
                                        contentDescription = "Subject Icon",
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                    Text(
                                        text = if (isHindi) "$subjectName / विषय" else subjectName,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                Icon(
                                    imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                                    contentDescription = "Expand info"
                                )
                            }

                            AnimatedVisibility(
                                visible = isExpanded,
                                enter = expandVertically() + fadeIn(),
                                exit = shrinkVertically() + fadeOut()
                            ) {
                                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                    topicList.forEachIndexed { i, topic ->
                                        Card(
                                            modifier = Modifier.fillMaxWidth(),
                                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                            shape = RoundedCornerShape(12.dp)
                                        ) {
                                            Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                                Text(
                                                    text = "${i + 1}. " + if (isHindi) topic.titleHindi else topic.titleEnglish,
                                                    fontWeight = FontWeight.Bold,
                                                    style = MaterialTheme.typography.titleSmall,
                                                    color = MaterialTheme.colorScheme.secondary
                                                )
                                                Text(
                                                    text = if (isHindi) topic.descriptionHindi else topic.descriptionEnglish,
                                                    style = MaterialTheme.typography.bodySmall,
                                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                                )
                                                HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
                                                Text(
                                                    text = if (isHindi) topic.contentHindi else topic.contentEnglish,
                                                    style = MaterialTheme.typography.bodyMedium,
                                                    lineHeight = 20.sp
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Saved Custom Lesson Plans (Class Level filter)
        val classPlans = localLessonPlans.filter { it.classLevel == activeClass }
        if (classPlans.isNotEmpty()) {
            item {
                Text(
                    text = if (isHindi) "ककड़ी से सहेजी गई पाठ योजनाएँ" else "My Saved Lesson Plans ($activeClass)",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            items(classPlans) { plan ->
                Card(
                    modifier = Modifier.fillMaxWidth().testTag("lesson_plan_${plan.id}"),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Badge { Text(plan.subject) }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = plan.title,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            IconButton(onClick = { viewModel.deleteLessonPlan(plan) }) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Delete Plan",
                                    tint = MaterialTheme.colorScheme.error
                                )
                            }
                        }

                        Text(
                            text = "Objectives: ${plan.objectives}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "Play-Way Activity: ${plan.activity}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(imageVector = Icons.Default.Star, contentDescription = "", modifier = Modifier.size(14.dp))
                            Text(text = "Duration: ${plan.duration}", style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }
        }
    }

    // Modal dialog to Add Custom Lesson Plan
    if (showAddPlanDialog) {
        AlertDialog(
            onDismissRequest = { showAddPlanDialog = false },
            title = { Text(if (isHindi) "नई पाठ योजना तैयार करें" else "Create Lesson Plan") },
            text = {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    item {
                        Text("Class: $activeClass", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                    }
                    item {
                        OutlinedTextField(
                            value = planSubject,
                            onValueChange = { planSubject = it },
                            label = { Text("Subject / विषय") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    item {
                        OutlinedTextField(
                            value = planTitle,
                            onValueChange = { planTitle = it },
                            label = { Text("Topic Title / शीर्षक") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    item {
                        OutlinedTextField(
                            value = planObjectives,
                            onValueChange = { planObjectives = it },
                            label = { Text("Objectives / उद्देश्य") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    item {
                        OutlinedTextField(
                            value = planMethodology,
                            onValueChange = { planMethodology = it },
                            label = { Text("Methodology (Play-way, experiential)") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    item {
                        OutlinedTextField(
                            value = planActivity,
                            onValueChange = { planActivity = it },
                            label = { Text("Interactive Classroom Activity") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    item {
                        OutlinedTextField(
                            value = planAssessment,
                            onValueChange = { planAssessment = it },
                            label = { Text("Assessment / मौखिक प्रश्न") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (planTitle.isNotBlank()) {
                            viewModel.addLessonPlan(
                                classLevel = activeClass,
                                subject = planSubject,
                                title = planTitle,
                                objectives = planObjectives,
                                methodology = planMethodology,
                                activity = planActivity,
                                assessment = planAssessment
                            )
                            showAddPlanDialog = false
                        }
                    },
                    enabled = planTitle.isNotBlank()
                ) {
                    Text(if (isHindi) "सहेजें" else "Save Plan")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddPlanDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}
