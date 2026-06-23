package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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

@Composable
fun FoundationsScreen(viewModel: AcademyViewModel, modifier: Modifier = Modifier) {
    val isHindi by viewModel.isHindi.collectAsStateWithLifecycle()
    var selectedTab by remember { mutableStateOf("Hindi") } // Hindi, English, Math

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Core Tab Selectors
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf("Hindi", "English", "Mathematics").forEach { tab ->
                Button(
                    onClick = { selectedTab = tab },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selectedTab == tab) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.surfaceVariant,
                        contentColor = if (selectedTab == tab) MaterialTheme.colorScheme.onPrimary
                        else MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("tab_foundation_$tab"),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = if (isHindi) {
                            when (tab) {
                                "Hindi" -> "हिंदी फाउंडेशन"
                                "English" -> "अंग्रेजी सीखें"
                                else -> "गणित आधार"
                            }
                        } else {
                            tab
                        },
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // Sub-panels
        when (selectedTab) {
            "Hindi" -> HindiFoundationPanel(viewModel)
            "English" -> EnglishFoundationPanel(viewModel)
            "Mathematics" -> MathsFoundationPanel(viewModel)
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun HindiFoundationPanel(viewModel: AcademyViewModel) {
    val isHindi by viewModel.isHindi.collectAsStateWithLifecycle()
    val swars = listOf("अ", "आ", "इ", "ई", "उ", "ऊ", "ऋ", "ए", "ऐ", "ओ", "औ", "अं", "अः")
    val vyanjans = listOf("क", "ख", "ग", "घ", "ङ", "च", "छ", "ज", "झ", "ञ", "ट", "ठ", "ड", "ढ", "ण", "त", "थ", "द", "ध", "न", "प", "फ", "ब", "भ", "म", "य", "र", "ल", "व")

    LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp), modifier = Modifier.fillMaxSize()) {
        // Header
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(imageVector = Icons.Default.Star, contentDescription = "", tint = MaterialTheme.colorScheme.tertiary, modifier = Modifier.size(32.dp))
                    Column {
                        Text(
                            text = if (isHindi) "हिंदी वर्णमाला (शून्य स्तर से)" else "Hindi Foundation (Zero Level)",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = if (isHindi) "बच्चों को स्वर, व्यंजन और मात्राओं को बुनियादी रूप से पहचानना सिखाएं।" else "Guide beginners systematically with vowels, consonants, and word formations.",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }

        // Swar Vowels Grid
        item {
            Text(text = if (isHindi) "स्वर (Swar Vowels):" else "Swar (Vowels):", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        }
        item {
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                swars.forEach { swar ->
                    Card(
                        modifier = Modifier
                            .size(54.dp)
                            .clickable {},
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                            Text(text = swar, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                        }
                    }
                }
            }
        }

        // Vyanjan Consonants Grid
        item {
            Text(text = if (isHindi) "व्यंजन (Vyanjan Consonants):" else "Vyanjan (Consonants):", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        }
        item {
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                vyanjans.forEach { vyanjan ->
                    Card(
                        modifier = Modifier
                            .size(48.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                    ) {
                        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                            Text(text = vyanjan, fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            }
        }

        // Static Prepopulated Lesson modules (From repo via ViewModel)
        item {
            Text(text = if (isHindi) "आधार पाठ / Foundation Topics" else "Hindi Core Guidance Modules", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        }
        items(viewModel.hindiLessons.size) { i ->
            val topic = viewModel.hindiLessons[i]
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = if (isHindi) topic.titleHindi else topic.titleEnglish,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.secondary
                    )
                    Text(
                        text = if (isHindi) topic.descriptionHindi else topic.descriptionEnglish,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    HorizontalDivider()
                    Text(
                        text = if (isHindi) topic.contentHindi else topic.contentEnglish,
                        style = MaterialTheme.typography.bodyMedium,
                        lineHeight = 22.sp
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun EnglishFoundationPanel(viewModel: AcademyViewModel) {
    val isHindi by viewModel.isHindi.collectAsStateWithLifecycle()
    val alphabets = ('A'..'Z').toList()

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
                            text = if (isHindi) "अंग्रेजी सीखें (शुरुआती स्तर से)" else "English Foundation (Beginner-Friendly)",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = if (isHindi) "ध्वनि उच्चारण (Phonics) और साइट वर्ड्स के द्वारा आत्मविश्वास बढ़ाएं।" else "Develop early reading, high-frequency sight words, and nouns/verbs exercises.",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }

        // Alphabet tiles
        item {
            Text(text = "Alphabet Letters (A-Z):", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        }
        item {
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                alphabets.forEach { letter ->
                    Card(
                        modifier = Modifier.size(46.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                        shape = CircleShape
                    ) {
                        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                            Text(text = "$letter${letter.lowercase()}", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                        }
                    }
                }
            }
        }

        // Static Core syllabus lessons
        item {
            Text(text = if (isHindi) "विषय निर्देशिका / English Modules" else "English Mastery Topics", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        }
        items(viewModel.englishLessons.size) { i ->
            val topic = viewModel.englishLessons[i]
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = if (isHindi) topic.titleHindi else topic.titleEnglish,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.secondary
                    )
                    Text(
                        text = if (isHindi) topic.descriptionHindi else topic.descriptionEnglish,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    HorizontalDivider()
                    Text(
                        text = if (isHindi) topic.contentHindi else topic.contentEnglish,
                        style = MaterialTheme.typography.bodyMedium,
                        lineHeight = 22.sp
                    )
                }
            }
        }
    }
}

@Composable
fun MathsFoundationPanel(viewModel: AcademyViewModel) {
    val isHindi by viewModel.isHindi.collectAsStateWithLifecycle()
    
    // Vedic multiplier state solver
    var valueA by remember { mutableStateOf("12") }
    var valueB by remember { mutableStateOf("13") }
    var vedicResultText by remember { mutableStateOf("") }

    LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp), modifier = Modifier.fillMaxSize()) {
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "", tint = MaterialTheme.colorScheme.secondary, modifier = Modifier.size(32.dp))
                    Column {
                        Text(
                            text = if (isHindi) "गणित आधार केंद्र" else "Mathematics Foundation",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = if (isHindi) "संख्या पहचान से शुरू कर वैदिक शॉर्टकट्स तक बच्चों और शिक्षकों के लिए अभ्यास।" else "Start from Basic numbers, ascending patterns, up to Vedic Math tricks.",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }

        // Vedic maths solver interactive module
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = if (isHindi) "वैदिक गुणन कैलकुलेटर (ऊर्ध्वतिर्यग्भ्याम्)" else "Vedic Multiplier Solver (Vertical & Crosswise)",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = valueA,
                            onValueChange = { valueA = it },
                            label = { Text("Num 1") },
                            modifier = Modifier.weight(1f).testTag("valA"),
                            maxLines = 1
                        )
                        OutlinedTextField(
                            value = valueB,
                            onValueChange = { valueB = it },
                            label = { Text("Num 2") },
                            modifier = Modifier.weight(1f).testTag("valB"),
                            maxLines = 1
                        )
                    }

                    Button(
                        onClick = {
                            val a = valueA.toIntOrNull()
                            val b = valueB.toIntOrNull()
                            if (a != null && b != null && valueA.length == 2 && valueB.length == 2) {
                                val uA = a % 10
                                val tA = a / 10
                                val uB = b % 10
                                val tB = b / 10
                                
                                val step1 = uA * uB
                                val step2 = (tA * uB) + (uA * tB)
                                val step3 = tA * tB

                                val total = a * b

                                vedicResultText = if (isHindi) {
                                    "1. इकाई अंक गुणा (${uA}x${uB}) = $step1\n" +
                                    "2. तिरछा गुणा जोड़ (${tA}x${uB} + ${uA}x${tB}) = $step2\n" +
                                    "3. दहाई अंक गुणा (${tA}x${tB}) = $step3\n" +
                                    "योगफल उत्तर = $total"
                                } else {
                                    "1. Straight units (${uA}x${uB}) = $step1\n" +
                                    "2. Cross sum (${tA}x${uB} + ${uA}x${tB}) = $step2\n" +
                                    "3. Straight tens (${tA}x${tB}) = $step3\n" +
                                    "Vedic Multiplied Answer: $total"
                                }
                            } else {
                                vedicResultText = "Please enter two digit integers (e.g. 12 & 14) / कृपया दो अंकों की संख्याएं डालें।"
                            }
                        },
                        modifier = Modifier.fillMaxWidth().testTag("solve_vedic_btn")
                    ) {
                        Text(if (isHindi) "वैदिक रीति से गुणा करें" else "Solve Vedic multiplication")
                    }

                    if (vedicResultText.isNotEmpty()) {
                        Text(
                            text = vedicResultText,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(8.dp))
                                .padding(12.dp)
                        )
                    }
                }
            }
        }

        // Static core Math cards
        item {
            Text(text = if (isHindi) "विषय मार्गदर्शिका / Math Lessons" else "Mathematics Guidelines", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        }
        items(viewModel.mathLessons.size) { i ->
            val topic = viewModel.mathLessons[i]
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = if (isHindi) topic.titleHindi else topic.titleEnglish,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.secondary
                    )
                    Text(
                        text = if (isHindi) topic.descriptionHindi else topic.descriptionEnglish,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    HorizontalDivider()
                    Text(
                        text = if (isHindi) topic.contentHindi else topic.contentEnglish,
                        style = MaterialTheme.typography.bodyMedium,
                        lineHeight = 22.sp
                    )
                }
            }
        }
    }
}
