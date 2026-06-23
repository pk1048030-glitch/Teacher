package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.viewmodel.AcademyViewModel

@Composable
fun TeacherTrainingScreen(viewModel: AcademyViewModel, modifier: Modifier = Modifier) {
    val isHindi by viewModel.isHindi.collectAsStateWithLifecycle()
    val allNotes by viewModel.allNotes.collectAsStateWithLifecycle()
    val modules = viewModel.trainingModules

    var expandedModuleId by remember { mutableStateOf<Int?>(null) }
    var selectedNoteCategory by remember { mutableStateOf("Psychology") }
    
    // Quick notes state variables
    var noteTitle by remember { mutableStateOf("") }
    var noteContent by remember { mutableStateOf("") }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Banner Hero Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("training_hero"),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                shape = RoundedCornerShape(24.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.linearGradient(
                                colors = listOf(
                                    MaterialTheme.colorScheme.primaryContainer,
                                    MaterialTheme.colorScheme.secondaryContainer
                                )
                            )
                        )
                        .padding(24.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.List,
                                contentDescription = "School Header Icon",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(36.dp)
                            )
                            Text(
                                text = if (isHindi) "शिक्षक प्रशिक्षण केंद्र" else "Teacher Training Center",
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }

                        Text(
                            text = if (isHindi) "एक कुशल, आत्मविश्वासी और संवेदनशील शिक्षक बनने के लिए बुनियादी फाउंडेशन कोर्स पूरा करें।"
                            else "Master Child Psychology, Active Methodologies, Classroom Discipline, and Professional Lesson plans.",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        // Training Modules List
        item {
            Text(
                text = if (isHindi) "पाठ्यक्रम अध्याय / Course Chapters" else "Training Modules",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        items(modules.size) { index ->
            val module = modules[index]
            val isExpanded = expandedModuleId == index
            
            val title = if (isHindi) module.titleHindi else module.titleEnglish
            val desc = if (isHindi) module.descriptionHindi else module.descriptionEnglish
            val detail = if (isHindi) module.contentHindi else module.contentEnglish

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("training_module_card_$index")
                    .clickable { expandedModuleId = if (isExpanded) null else index },
                colors = CardDefaults.cardColors(
                    containerColor = if (isExpanded) MaterialTheme.colorScheme.surfaceVariant
                    else MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                shape = RoundedCornerShape(16.dp)
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
                                imageVector = when(index) {
                                    0 -> Icons.Default.Face
                                    1 -> Icons.Default.Share
                                    2 -> Icons.Default.Home
                                    3 -> Icons.Default.PlayArrow
                                    else -> Icons.Default.List
                                },
                                contentDescription = "Module Icon",
                                tint = MaterialTheme.colorScheme.secondary,
                            )
                            Text(
                                text = title,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Icon(
                            imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                            contentDescription = "Expand info"
                        )
                    }

                    Text(
                        text = desc,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    AnimatedVisibility(
                        visible = isExpanded,
                        enter = fadeIn() + expandVertically(),
                        exit = fadeOut() + shrinkVertically()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp, bottom = 8.dp)
                                .background(
                                    MaterialTheme.colorScheme.surfaceVariant,
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = if (isHindi) "विस्तृत पाठ / Course Guidelines:" else "Guidelines & Application Plan:",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = detail,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
        }

        // Live Training Diary (Personal Notes)
        item {
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Create,
                    contentDescription = "Diary Icon",
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = if (isHindi) "शिक्षक प्रशिक्षण डायरी" else "Teacher Training Diary",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Create Note Editor
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("note_editor_card"),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = if (isHindi) "डायरी में नया नोट जोड़ें" else "Record Offline Lesson Insight",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )

                    TextField(
                        value = noteTitle,
                        onValueChange = { noteTitle = it },
                        placeholder = { Text(if (isHindi) "नोट का शीर्षक (उदा: बाल मनोविज्ञान सूत्र)" else "Note Title") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("note_title_input"),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            disabledContainerColor = Color.Transparent
                        )
                    )

                    TextField(
                        value = noteContent,
                        onValueChange = { noteContent = it },
                        placeholder = { Text(if (isHindi) "नोट विवरण लिखें..." else "Write notes or teaching ideas...") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("note_content_input"),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            disabledContainerColor = Color.Transparent
                        ),
                        maxLines = 4
                    )

                    // Note Category selector
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Category:", style = MaterialTheme.typography.bodySmall)
                        listOf("Psychology", "Methodology", "Management", "General").forEach { cat ->
                            FilterChip(
                                selected = selectedNoteCategory == cat,
                                onClick = { selectedNoteCategory = cat },
                                label = { Text(cat, fontSize = 11.sp) },
                                modifier = Modifier.testTag("chip_$cat")
                            )
                        }
                    }

                    Button(
                        onClick = {
                            if (noteTitle.isNotBlank()) {
                                viewModel.addNote(noteTitle, noteContent, selectedNoteCategory)
                                noteTitle = ""
                                noteContent = ""
                            }
                        },
                        modifier = Modifier
                            .align(Alignment.End)
                            .testTag("save_note_button"),
                        enabled = noteTitle.isNotBlank()
                    ) {
                        Icon(imageVector = Icons.Default.Check, contentDescription = "Save Action")
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(if (isHindi) "डायरी में सहेजें" else "Save Note")
                    }
                }
            }
        }

        // Saved Notes List
        if (allNotes.isNotEmpty()) {
            item {
                Text(
                    text = if (isHindi) "सहेजे गए नोट्स / Saved Insights" else "My Saved Notes",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            items(allNotes) { note ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("note_item_${note.id}"),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    text = note.title,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Badge { Text(note.category) }
                            }
                            Text(
                                text = note.content,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        IconButton(
                            onClick = { viewModel.deleteNote(note.id) },
                            modifier = Modifier.testTag("delete_note_${note.id}")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete note",
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }
            }
        }
    }
}
