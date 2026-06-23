package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectDragGestures
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.viewmodel.AcademyViewModel
import com.example.ui.viewmodel.Line

@Composable
fun AddonsScreen(viewModel: AcademyViewModel, modifier: Modifier = Modifier) {
    val isHindi by viewModel.isHindi.collectAsStateWithLifecycle()
    var selectedCategory by remember { mutableStateOf("Stories") } // Stories, Canvas, Reasoning, GK, Booster

    val categories = listOf("Stories", "Canvas", "Reasoning", "GK", "Booster")

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Toggle Category Button Header
        Row(
            modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            categories.forEach { cat ->
                Button(
                    onClick = { selectedCategory = cat },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selectedCategory == cat) MaterialTheme.colorScheme.secondary
                        else MaterialTheme.colorScheme.surfaceVariant,
                        contentColor = if (selectedCategory == cat) MaterialTheme.colorScheme.onSecondary
                        else MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    modifier = Modifier.testTag("addon_tab_$cat"),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = if (isHindi) {
                            when (cat) {
                                "Stories" -> "नैतिक कहानियां"
                                "Canvas" -> "ड्रॉइंग बोर्ड"
                                "Reasoning" -> "तार्किक क्षमता"
                                "GK" -> "सामान्य ज्ञान"
                                else -> "शिक्षक बूस्टर"
                            }
                        } else {
                            when (cat) {
                                "Stories" -> "Moral Stories"
                                "Canvas" -> "Sketch Slates"
                                "Reasoning" -> "Logical Reasoning"
                                "GK" -> "General Knowledge"
                                else -> "Teacher Booster"
                            }
                        },
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // Selected module Panel
        when (selectedCategory) {
            "Stories" -> StoryLibraryPanel(viewModel)
            "Canvas" -> DrawingBoardPanel(viewModel)
            "Reasoning" -> ReasoningPanel(viewModel)
            "GK" -> GkPanel(viewModel)
            "Booster" -> BoosterPanel(viewModel)
        }
    }
}

@Composable
fun StoryLibraryPanel(viewModel: AcademyViewModel) {
    val isHindi by viewModel.isHindi.collectAsStateWithLifecycle()
    val stories = viewModel.moralStories
    var activeStoryIndex by remember { mutableStateOf<Int?>(null) }

    LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp), modifier = Modifier.fillMaxSize()) {
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(imageVector = Icons.Default.Info, contentDescription = "", tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(32.dp))
                    Column {
                        Text(
                            text = if (isHindi) "नैतिक कहानी पुस्तकालय" else "Moral Story Library",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = if (isHindi) "मूल्यपरक शिक्षा और कहानियों के ज़रिए बोलना, सुनना और नैतिक गुण सिखाएं।" else "Develop listening skills and moral wisdom with classic folk tales.",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }

        items(stories.size) { i ->
            val story = stories[i]
            val isOpen = activeStoryIndex == i
            val title = if (isHindi) story.titleHindi else story.titleEnglish
            val teaser = if (isHindi) story.descriptionHindi else story.descriptionEnglish
            val body = if (isHindi) story.contentHindi else story.contentEnglish

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { activeStoryIndex = if (isOpen) null else i }
                    .testTag("story_card_$i"),
                colors = CardDefaults.cardColors(
                    containerColor = if (isOpen) MaterialTheme.colorScheme.surfaceVariant else MaterialTheme.colorScheme.surface
                ),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = title, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                        Icon(imageVector = Icons.Default.Info, contentDescription = "")
                    }
                    Text(text = teaser, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    
                    AnimatedVisibility(
                        visible = isOpen,
                        enter = fadeIn() + expandVertically(),
                        exit = fadeOut() + shrinkVertically()
                    ) {
                        Column {
                            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                            Text(text = body, style = MaterialTheme.typography.bodyMedium, lineHeight = 22.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DrawingBoardPanel(viewModel: AcademyViewModel) {
    val isHindi by viewModel.isHindi.collectAsStateWithLifecycle()
    val lines by viewModel.canvasLines.collectAsStateWithLifecycle()

    var activeColor by remember { mutableStateOf("#FF6B6B") }

    Column(verticalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxSize()) {
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = if (isHindi) "सक्रिय शिक्षण ड्राइंग स्लेट" else "Interactive Teaching Sketch Board",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleSmall
                )
                Text(
                    text = if (isHindi) "वर्णमाला आकृति या अंकों को उंगली से स्लेट पर बनाकर छात्रों को प्रत्यक्ष रूप से समझाएं।" else "Illustrate word shapes, counting patterns, or geometry models on the canvas.",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        // Color and Action Controls
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("#FF6B6B", "#4D96FF", "#6BCB77", "#FFD93D", "#FFFFFF").forEach { hex ->
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .background(Color(android.graphics.Color.parseColor(hex)), shape = RoundedCornerShape(4.dp))
                            .border(
                                width = if (activeColor == hex) 3.dp else 1.dp,
                                color = if (activeColor == hex) MaterialTheme.colorScheme.primary else Color.LightGray,
                                shape = RoundedCornerShape(4.dp)
                            )
                            .clickable { activeColor = hex }
                            .testTag("color_$hex")
                    )
                }
            }

            Button(
                onClick = { viewModel.clearCanvas() },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                modifier = Modifier.testTag("clear_board_btn")
            ) {
                Icon(imageVector = Icons.Default.Clear, contentDescription = "")
                Spacer(modifier = Modifier.width(4.dp))
                Text(if (isHindi) "पूरी तरह साफ़ करें" else "Clear Slate")
            }
        }

        // Interactive Canvas Board
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(Color(0xFF2B2D42), shape = RoundedCornerShape(24.dp))
                .border(2.dp, MaterialTheme.colorScheme.secondary, shape = RoundedCornerShape(24.dp))
                .pointerInput(Unit) {
                    detectDragGestures { change, dragAmount ->
                        change.consume()
                        val previousPosition = change.previousPosition
                        val currentPosition = change.position
                        
                        // Append new coordinate line to stream
                        val newLine = Line(
                            startX = previousPosition.x,
                            startY = previousPosition.y,
                            endX = currentPosition.x,
                            endY = currentPosition.y,
                            colorHex = activeColor
                        )
                        viewModel.canvasLines.value = viewModel.canvasLines.value + newLine
                    }
                }
                .testTag("drawing_slate_board")
        ) {
            androidx.compose.foundation.Canvas(modifier = Modifier.fillMaxSize()) {
                lines.forEach { line ->
                    try {
                        colorPath(line)
                    } catch (e: Exception) {
                        // ignore color parse failure
                    }
                }
            }

            if (lines.isEmpty()) {
                Text(
                    text = if (isHindi) "यहाँ ड्रा करने के लिए उंगली घुमाएँ..." else "Drag finger here to draw models...",
                    color = Color.LightGray.copy(alpha = 0.5f),
                    modifier = Modifier.align(Alignment.Center),
                    fontSize = 14.sp
                )
            }
        }
    }
}

// Draw line helper
private fun androidx.compose.ui.graphics.drawscope.DrawScope.colorPath(line: Line) {
    drawLine(
        color = Color(android.graphics.Color.parseColor(line.colorHex)),
        start = androidx.compose.ui.geometry.Offset(line.startX, line.startY),
        end = androidx.compose.ui.geometry.Offset(line.endX, line.endY),
        strokeWidth = 8f,
        cap = androidx.compose.ui.graphics.StrokeCap.Round
    )
}

@Composable
fun ReasoningPanel(viewModel: AcademyViewModel) {
    val isHindi by viewModel.isHindi.collectAsStateWithLifecycle()
    val reasoning = viewModel.reasoningLessons
    var currentScore by remember { mutableStateOf(0) }
    var clickedOddOneOut by remember { mutableStateOf<String?>(null) }

    LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp), modifier = Modifier.fillMaxSize()) {
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = if (isHindi) "कक्षा तार्किक क्षमता / Reasoning Zone" else "Logical Reasoning Zone",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = if (isHindi) "आकृतियों, अनुक्रमों व वर्गीकरण आधारित गेम्स से बच्चों की सोचने की क्षमता बढ़ाएं।" else "Boost cognitive agility with odd-one-out categorization and pattern matching games.",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }

        // Game 1: Odd One Out
        item {
            Card(
                modifier = Modifier.fillMaxWidth().testTag("reasoning_game_1"),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        text = if (isHindi) "खेल 1: असंगत चुनें (Odd One Out)" else "Game 1: Select the Odd One Out",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = if (isHindi) "नीचे दिए गए विकल्पों में सबसे अलग वस्तु का चयन करें:" else "Which of the following is completely distinct from the others?",
                        style = MaterialTheme.typography.bodyMedium
                    )

                    val options = listOf("Apple / सेब", "Banana / केला", "Orange / संतरा", "Potato / आलू")
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        options.forEach { opt ->
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .background(
                                        color = if (clickedOddOneOut == opt) {
                                            if (opt.contains("Potato")) Color(0xFFC0EDA6) else Color(0xFFFFB3B3)
                                        } else MaterialTheme.colorScheme.surfaceVariant,
                                        shape = RoundedCornerShape(8.dp)
                                    )
                                    .clickable {
                                        clickedOddOneOut = opt
                                        if (opt.contains("Potato")) {
                                            currentScore += 10
                                            viewModel.saveQuizScore("OddOneOut Game", 10, 10)
                                        }
                                    }
                                    .padding(8.dp)
                                    .testTag("odd_$opt"),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = opt, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                            }
                        }
                    }

                    if (clickedOddOneOut != null) {
                        Text(
                            text = if (clickedOddOneOut!!.contains("Potato")) {
                                if (isHindi) "बिल्कुल सही! आलू एक सब्जी है, बचे हुए सब फल हैं। (+10 अंक)" else "Excellent! Potato is a vegetable, others are fruits. (+10 Score)"
                            } else {
                                if (isHindi) "गलत! दोबारा कोशिश करें।" else "Incorrect! Try again."
                            },
                            fontWeight = FontWeight.Bold,
                            color = if (clickedOddOneOut!!.contains("Potato")) Color(0xFF387F39) else Color.Red,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }

        // Display general reasoning material
        items(reasoning.size) { i ->
            val topic = reasoning[i]
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = if (isHindi) topic.titleHindi else topic.titleEnglish,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleSmall
                    )
                    Text(
                        text = if (isHindi) topic.contentHindi else topic.contentEnglish,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}

@Composable
fun GkPanel(viewModel: AcademyViewModel) {
    val isHindi by viewModel.isHindi.collectAsStateWithLifecycle()
    val facts = viewModel.gkLessons

    LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp), modifier = Modifier.fillMaxSize()) {
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = if (isHindi) "सामान्य ज्ञान पुस्तकालय" else "General Knowledge Zone",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = if (isHindi) "भारत के राज्य, राजधानियां, विज्ञान और पशु जगत से जुड़े रोचक विजुअल तथ्य।" else "Explore interactive flashcards covering birds, human body, capitals & sports.",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }

        items(facts.size) { i ->
            val fact = facts[i]
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = if (isHindi) fact.titleHindi else fact.titleEnglish,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = if (isHindi) fact.descriptionHindi else fact.descriptionEnglish,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    HorizontalDivider()
                    Text(
                        text = if (isHindi) fact.contentHindi else fact.contentEnglish,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}

@Composable
fun BoosterPanel(viewModel: AcademyViewModel) {
    val isHindi by viewModel.isHindi.collectAsStateWithLifecycle()

    val boosts = listOf(
        Pair("Basic English Fluency", "Ensure correct subject-verb agreements (e.g. He does vs They do). Teach short visual dialogues Daily."),
        Pair("Hindi Grammar Nuances", "Focus heavily on swar sounds contrast (e.g. ि vs ी). Practice writing dictation once weekly."),
        Pair("Internet Skillsets", "Use bookmarks to organize lesson resources. Simulate saving offline materials to prevent signal loss."),
        Pair("Public Speaking & Interview", "Maintain comfortable eye-contact, project voice loudly, use hand gestures to explain shape charts.")
    )

    LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxSize()) {
        item {
            Text(
                text = if (isHindi) "शिक्षक ज्ञान वर्धक बूस्टर" else "Teacher Knowledge Booster",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }

        items(boosts) { item ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(imageVector = Icons.Default.Star, contentDescription = "", tint = MaterialTheme.colorScheme.secondary, modifier = Modifier.size(36.dp))
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(text = item.first, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                        Text(text = item.second, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        }
    }
}
