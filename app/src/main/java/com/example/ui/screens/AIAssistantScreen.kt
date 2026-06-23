package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.viewmodel.AcademyViewModel

@Composable
fun AIAssistantScreen(viewModel: AcademyViewModel, modifier: Modifier = Modifier) {
    val isHindi by viewModel.isHindi.collectAsStateWithLifecycle()
    val aiResponse by viewModel.aiResponseText.collectAsStateWithLifecycle()
    val isGenerating by viewModel.isAiGenerating.collectAsStateWithLifecycle()
    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current

    var selectedPromptType by remember { mutableStateOf("Lesson Plan") } // Lesson Plan, Worksheet, Quizzes, Activities, Feedback
    var gradeLevel by remember { mutableStateOf("Class 1") }
    var subjectCategory by remember { mutableStateOf("English") }
    var topicDescription by remember { mutableStateOf("Basic Addition") }

    val promptTypes = listOf("Lesson Plan", "Worksheet", "Quizzes", "Activities", "Feedback")
    val grades = listOf("Nursery", "LKG", "UKG", "Class 1", "Class 2", "Class 3", "Class 4", "Class 5")
    val subjects = listOf("English", "Hindi", "Mathematics", "EVS", "Science", "Computer")

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Hero Intro Banner
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                shape = RoundedCornerShape(20.dp)
            ) {
                Box(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Icon(imageVector = Icons.Default.Settings, contentDescription = "", tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(32.dp))
                            Text(
                                text = if (isHindi) "एआई शिक्षण सहायक / AI Assistant" else "AI Teaching Assistant",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Text(
                            text = if (isHindi) "एक क्लिक में पाठ योजनाएं, मजेदार व्याकरण वर्कशीट, वैदिक गणित मूक टेस्ट और होमवर्क कमेंट्स तैयार करें।"
                            else "Generate structured dynamic worksheets, lesson outlines, play-way homework lists, or report feedbacks using Gemini API.",
                            style = MaterialTheme.typography.bodySmall,
                            lineHeight = 18.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        // Configuration Form
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Type filter Row
                    Column {
                        Text(text = "1. Generate Type:", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            promptTypes.forEach { type ->
                                FilterChip(
                                    selected = selectedPromptType == type,
                                    onClick = { selectedPromptType = type },
                                    label = { Text(type, fontSize = 11.sp) },
                                    modifier = Modifier.testTag("prompt_chip_$type")
                                )
                            }
                        }
                    }

                    // Class Level Selection
                    Column {
                        Text(text = "2. Select Target Grade:", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            grades.forEach { g ->
                                FilterChip(
                                    selected = gradeLevel == g,
                                    onClick = { gradeLevel = g },
                                    label = { Text(g, fontSize = 11.sp) },
                                    modifier = Modifier.testTag("grade_chip_$g")
                                )
                            }
                        }
                    }

                    // Subjects Selector
                    Column {
                        Text(text = "3. Select Subject:", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            subjects.forEach { sub ->
                                FilterChip(
                                    selected = subjectCategory == sub,
                                    onClick = { subjectCategory = sub },
                                    label = { Text(sub, fontSize = 11.sp) },
                                    modifier = Modifier.testTag("sub_chip_$sub")
                                )
                            }
                        }
                    }

                    // Topic prompt input
                    OutlinedTextField(
                        value = topicDescription,
                        onValueChange = { topicDescription = it },
                        label = { Text("Describe Topic or Specific Goal (e.g. Alphabets blending / Phonics rules)") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("ai_topic_desc"),
                        maxLines = 2
                    )

                    // Execute Button
                    Button(
                        onClick = {
                            if (topicDescription.isNotBlank()) {
                                viewModel.generateAiContent(selectedPromptType, gradeLevel, subjectCategory, topicDescription)
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("generate_ai_call_btn"),
                        enabled = !isGenerating && topicDescription.isNotBlank()
                    ) {
                        if (isGenerating) {
                            CircularProgressIndicator(modifier = Modifier.size(18.dp), color = Color.White, strokeWidth = 2.dp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Generating Materials...")
                        } else {
                            Icon(imageVector = Icons.Default.Settings, contentDescription = "")
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(if (isHindi) "एआई सामग्री उत्पन्न करें" else "Generate Creative Materials")
                        }
                    }
                }
            }
        }

        // Output Result Card
        if (aiResponse.isNotEmpty()) {
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.secondary),
                    modifier = Modifier.testTag("ai_output_holder")
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
                            Text(
                                text = "AI Result Layout:",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )

                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                IconButton(
                                    onClick = {
                                        clipboardManager.setText(AnnotatedString(aiResponse))
                                        Toast.makeText(context, "Copied AI output to paste", Toast.LENGTH_SHORT).show()
                                    },
                                    modifier = Modifier.testTag("copy_ai_output")
                                ) {
                                    Icon(imageVector = Icons.Default.Edit, contentDescription = "Copy")
                                }

                                if (!isGenerating && !aiResponse.startsWith("Error") && !aiResponse.contains("crafting")) {
                                    Button(
                                        onClick = {
                                            viewModel.saveGeneratedAsWorksheet(
                                                title = "$selectedPromptType: $topicDescription ($gradeLevel)",
                                                grade = gradeLevel,
                                                subject = subjectCategory
                                            )
                                            Toast.makeText(context, "Saved successfully into Material Library!", Toast.LENGTH_SHORT).show()
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF387F39)),
                                        modifier = Modifier.testTag("save_ai_to_db")
                                    ) {
                                        Icon(imageVector = Icons.Default.Check, contentDescription = "")
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(if (isHindi) "पुस्तकालय में सहेजें" else "Save to Library")
                                    }
                                }
                            }
                        }

                        Text(
                            text = aiResponse,
                            style = MaterialTheme.typography.bodyMedium,
                            lineHeight = 22.sp,
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .padding(16.dp)
                        )

                        // Caution notice for developer keys as requested in Secret Management skill!
                        Text(
                            text = "Security Alert: This educational assistant key is configured for active simulations. Avoid public redeployments.",
                            fontSize = 10.sp,
                            color = Color.Gray,
                            fontWeight = FontWeight.Light,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
            }
        }
    }
}
