package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.viewmodel.AcademyViewModel

@Composable
fun DashboardScreen(viewModel: AcademyViewModel, modifier: Modifier = Modifier) {
    val isHindi by viewModel.isHindi.collectAsStateWithLifecycle()
    val todayProgress by viewModel.todayProgress.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val searchResults by viewModel.filteredSearchResults.collectAsStateWithLifecycle()

    var showAdminState by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Global Search Bar (Section 15)
        item {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { viewModel.searchQuery.value = it },
                placeholder = { Text(if (isHindi) "विषय, पाठ योजना, या प्रश्न खोजें..." else "Search topics, lesson plans, story books...") },
                leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = "") },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { viewModel.searchQuery.value = "" }) {
                            Icon(imageVector = Icons.Default.Close, contentDescription = "")
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("global_search_input"),
                shape = RoundedCornerShape(28.dp)
            )
        }

        // Search Results Panel
        if (searchQuery.trim().isNotEmpty()) {
            item {
                Text(
                    text = if (isHindi) "खोज परिणाम / Search Matches" else "Matches found across sections:",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            if (searchResults.isEmpty()) {
                item {
                    Text(
                        text = if (isHindi) "कोई परिणाम नहीं मिला। कृपया दूसरा शब्द खोजें।" else "No matches found. Try search keys like 'Vedic', 'Math', 'Language', 'Psychology'.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            } else {
                searchResults.keys.forEach { sectionName ->
                    item {
                        Text(text = sectionName, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleSmall)
                    }

                    items(searchResults[sectionName] ?: emptyList()) { topic ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                        ) {
                            Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Text(
                                    text = if (isHindi) topic.titleHindi else topic.titleEnglish,
                                    fontWeight = FontWeight.Bold,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.secondary
                                )
                                Text(
                                    text = if (isHindi) topic.contentHindi else topic.contentEnglish,
                                    style = MaterialTheme.typography.bodySmall,
                                    maxLines = 4
                                )
                            }
                        }
                    }
                }
            }

            item {
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
            }
        }

        // Admin Toggler / System Hub
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (isHindi) "दैनिक प्रगति डैशबोर्ड" else "Daily Learning Dashboard",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                FilterChip(
                    selected = showAdminState,
                    onClick = { showAdminState = !showAdminState },
                    label = { Text(if (isHindi) "व्यवस्थापक पैनल / Admin" else "Admin Station") },
                    modifier = Modifier.testTag("admin_panel_toggle_btn"),
                    leadingIcon = { Icon(imageVector = Icons.Default.Settings, contentDescription = "", modifier = Modifier.size(14.dp)) }
                )
            }
        }

        if (showAdminState) {
            // Admin Panel CMS (Section 17)
            item {
                AdminPanelComponent(viewModel)
            }
        } else {
            // Roadmap tracker (Section 14)
            item {
                RoadmapComponent(viewModel, todayProgress)
            }

            // Student General Performance summary dashboard (Section 9)
            item {
                StatsAnalyticsComponent(viewModel)
            }
        }
    }
}

@Composable
fun RoadmapComponent(viewModel: AcademyViewModel, progress: com.example.data.database.RoadmapProgress?) {
    val isHindi by viewModel.isHindi.collectAsStateWithLifecycle()
    val actual = progress ?: com.example.data.database.RoadmapProgress("")

    Card(
        modifier = Modifier.fillMaxWidth().testTag("roadmap_card"),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.4f)),
        shape = RoundedCornerShape(24.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Icon(imageVector = Icons.Default.List, contentDescription = "", tint = MaterialTheme.colorScheme.primary)
                    Text(
                        text = if (isHindi) "शिक्षक अध्ययन रोडमैप" else "Teacher Training Roadmap Tracker",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
                Badge { Text(text = "Daily Goals") }
            }

            Text(
                text = if (isHindi) "स्वयं की योग्यता बढ़ाने के लिए प्रतिदिन अध्ययन पूर्ण करें (समय सिमुलेटर द्वारा):"
                else "Upgrade teaching competencies by marking off daily study modules:",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            // Subject Checklist rows
            val subjectsTasks = listOf(
                Triple("Hindi Foundation", "30 mins limit", "Hindi"),
                Triple("English Foundation", "30 mins limit", "English"),
                Triple("Mathematics basis", "30 mins limit", "Mathematics"),
                Triple("General Knowledge facts", "15 mins limit", "GK"),
                Triple("Reasoning Games", "15 mins limit", "Reasoning")
            )

            subjectsTasks.forEach { task ->
                val minutesDone = when (task.third) {
                    "Hindi" -> actual.hindiMinutes
                    "English" -> actual.englishMinutes
                    "Mathematics" -> actual.mathMinutes
                    "GK" -> actual.gkMinutes
                    else -> actual.reasoningMinutes
                }
                val maxMinutes = if (task.third == "GK" || task.third == "Reasoning") 15 else 30
                val progressRatio = minutesDone.toFloat() / maxMinutes

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(12.dp))
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(text = task.first, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyMedium)
                        LinearProgressIndicator(
                            progress = { progressRatio },
                            modifier = Modifier.fillMaxWidth().height(6.dp),
                            color = MaterialTheme.colorScheme.primary,
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Button(
                        onClick = { viewModel.updateProgress(task.third, 15) },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                        modifier = Modifier.testTag("add_15_${task.third}"),
                        enabled = minutesDone < maxMinutes
                    ) {
                        Text(text = "+15m", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun StatsAnalyticsComponent(viewModel: AcademyViewModel) {
    val isHindi by viewModel.isHindi.collectAsStateWithLifecycle()
    val reports by viewModel.allStudentReports.collectAsStateWithLifecycle()

    var studentName by remember { mutableStateOf("") }
    var selectedReportClass by remember { mutableStateOf("Class 1") }
    var reportStrengths by remember { mutableStateOf("") }
    var reportWeaknesses by remember { mutableStateOf("") }
    var feedbackComments by remember { mutableStateOf("") }

    val reportCardsList = reports

    Column(verticalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(imageVector = Icons.Default.Info, contentDescription = "", tint = MaterialTheme.colorScheme.primary)
            Text(
                text = if (isHindi) "छातर आकलन एवं रिपोर्ट" else "Student Assessments & Analytics",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }

        // Simple Form to create progress card
        Card(
            modifier = Modifier.fillMaxWidth().testTag("assessment_form_card"),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(text = "Create Progress Report", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleSmall)
                
                OutlinedTextField(
                    value = studentName,
                    onValueChange = { studentName = it },
                    label = { Text("Student Name") },
                    modifier = Modifier.fillMaxWidth().testTag("student_name_input"),
                    maxLines = 1
                )

                OutlinedTextField(
                    value = reportStrengths,
                    onValueChange = { reportStrengths = it },
                    label = { Text("Key Strengths (e.g. Good reading / Multiplication)") },
                    modifier = Modifier.fillMaxWidth().testTag("student_strengths_input"),
                    maxLines = 1
                )

                OutlinedTextField(
                    value = reportWeaknesses,
                    onValueChange = { reportWeaknesses = it },
                    label = { Text("Detect Weakness (e.g. Needs handwriting practice)") },
                    modifier = Modifier.fillMaxWidth().testTag("student_weaknesses_input"),
                    maxLines = 1
                )

                OutlinedTextField(
                    value = feedbackComments,
                    onValueChange = { feedbackComments = it },
                    label = { Text("Personalized parent feedback comments...") },
                    modifier = Modifier.fillMaxWidth().testTag("student_comments_input"),
                    maxLines = 2
                )

                Button(
                    onClick = {
                        if (studentName.isNotBlank()) {
                            viewModel.addStudentReport(
                                name = studentName,
                                className = selectedReportClass,
                                strengths = reportStrengths,
                                weaknesses = reportWeaknesses,
                                academicGrade = "A",
                                behaviourGrade = "A",
                                feedback = feedbackComments
                            )
                            studentName = ""
                            reportStrengths = ""
                            reportWeaknesses = ""
                            feedbackComments = ""
                        }
                    },
                    modifier = Modifier.align(Alignment.End).testTag("save_student_report_btn"),
                    enabled = studentName.isNotBlank()
                ) {
                    Icon(imageVector = Icons.Default.Check, contentDescription = "")
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Save Student Report")
                }
            }
        }

        // Student List Reports
        if (reportCardsList.isNotEmpty()) {
            Text(text = "Saved Gradebooks & Weakness analytics:", fontWeight = FontWeight.SemiBold, style = MaterialTheme.typography.titleSmall)
            reportCardsList.forEach { card ->
                Card(
                    modifier = Modifier.fillMaxWidth().testTag("report_card_${card.id}"),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                                Text(text = card.studentName, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                                Badge { Text("Class 1") }
                            }
                            Text(text = "Strengths: ${card.strengths}", style = MaterialTheme.typography.bodySmall, color = Color(0xFF387F39))
                            Text(text = "Weaknesses: ${card.weaknesses}", style = MaterialTheme.typography.bodySmall, color = Color.Red)
                            Text(text = "Feedback comments: ${card.personalizedFeedback}", style = MaterialTheme.typography.bodyMedium)
                        }
                        IconButton(onClick = { viewModel.deleteStudentReport(card.id) }) {
                            Icon(imageVector = Icons.Default.Delete, contentDescription = "", tint = MaterialTheme.colorScheme.error)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AdminPanelComponent(viewModel: AcademyViewModel) {
    val isHindi by viewModel.isHindi.collectAsStateWithLifecycle()
    val context = LocalContext.current

    var sheetTitle by remember { mutableStateOf("") }
    var sheetGrade by remember { mutableStateOf("Class 1") }
    var sheetSubj by remember { mutableStateOf("Mathematics") }
    var sheetCont by remember { mutableStateOf("") }

    var mockFilePath by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f), shape = RoundedCornerShape(20.dp))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Icon(imageVector = Icons.Default.Settings, contentDescription = "", tint = MaterialTheme.colorScheme.secondary)
            Text(text = "Content Management System (CMS)", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
        }

        // Subfeature 1: Custom Worksheet Builder
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier.fillMaxWidth().testTag("admin_custom_worksheet_builder")
        ) {
            Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(text = "Topic Editor / Worksheet Creator", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleSmall)
                OutlinedTextField(
                    value = sheetTitle,
                    onValueChange = { sheetTitle = it },
                    label = { Text("Worksheet Title") },
                    modifier = Modifier.fillMaxWidth().testTag("sheet_title"),
                    maxLines = 1
                )
                OutlinedTextField(
                    value = sheetCont,
                    onValueChange = { sheetCont = it },
                    label = { Text("Type custom lesson text / worksheet questions...") },
                    modifier = Modifier.fillMaxWidth().testTag("sheet_content"),
                    maxLines = 4
                )
                Button(
                    onClick = {
                        if (sheetTitle.isNotBlank()) {
                            viewModel.addWorksheet(sheetTitle, sheetCont, sheetGrade, sheetSubj, isAi = false)
                            sheetTitle = ""
                            sheetCont = ""
                            Toast.makeText(context, "Lesson Worksheet saved successfully!", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier.align(Alignment.End).testTag("save_worksheet_cms_btn"),
                    enabled = sheetTitle.isNotBlank()
                ) {
                    Text("Save to Library")
                }
            }
        }

        // Subfeature 2: Interactive Mock File Attachment uploader (PDF simulation uploader)
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier.fillMaxWidth().testTag("mock_uploader_card")
        ) {
            Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(text = "Simulated PDF / Printable Chart Attachment Uploader", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleSmall)
                
                OutlinedTextField(
                    value = mockFilePath,
                    onValueChange = { mockFilePath = it },
                    placeholder = { Text("e.g. math_addition_chart.pdf") },
                    label = { Text("Document / Chart Filename") },
                    modifier = Modifier.fillMaxWidth().testTag("pdf_uploader_input"),
                    maxLines = 1
                )

                Button(
                    onClick = {
                        if (mockFilePath.isNotBlank()) {
                            viewModel.addWorksheet(
                                title = "Printable Attachment: $mockFilePath",
                                content = "Simulated offline document attachment path: assets/books/$mockFilePath.\nOpen and print chart via the Classroom material menu.",
                                grade = "General",
                                subject = "Chart Library",
                                isAi = false
                            )
                            mockFilePath = ""
                            Toast.makeText(context, "Mock PDF attached successfully!", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier.fillMaxWidth().testTag("pdf_uploader_upload_btn"),
                    enabled = mockFilePath.isNotBlank()
                ) {
                    Icon(imageVector = Icons.Default.Share, contentDescription = "")
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Attach Document Reference")
                }
            }
        }

        // Subfeature 3: Statistics Dashboard Charts simulation
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(text = "Student performance stats", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleSmall)
                Text(text = "Math passing grade: 92% | English Speech: 88% | Behaviour: 94%", style = MaterialTheme.typography.bodySmall)
                Row(
                    modifier = Modifier.fillMaxWidth().height(24.dp)
                ) {
                    Box(modifier = Modifier.weight(92f).fillMaxHeight().background(Color(0xFF6BCB77), shape = RoundedCornerShape(4.dp)))
                    Spacer(modifier = Modifier.width(4.dp))
                    Box(modifier = Modifier.weight(8f).fillMaxHeight().background(Color(0xFFFF6B6B), shape = RoundedCornerShape(4.dp)))
                }
                Text(text = "Green: Active passing, Red: Intervention needed", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
            }
        }
    }
}
