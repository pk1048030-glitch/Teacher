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
fun MaterialLibraryScreen(viewModel: AcademyViewModel, modifier: Modifier = Modifier) {
    val isHindi by viewModel.isHindi.collectAsStateWithLifecycle()
    val worksheets by viewModel.allWorksheets.collectAsStateWithLifecycle()
    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current

    var activeFlashcardIndex by remember { mutableStateOf(0) }
    var isFrontOfCard by remember { mutableStateOf(true) }

    val flashcards = listOf(
        Pair("A says /æ/", "Apple (सेब)"),
        Pair("B says /b/", "Ball (गेंद)"),
        Pair("C says /k/", "Cat (बिल्ली)"),
        Pair("1 + 1 equals", "2 (दो)"),
        Pair("2 x 3 using Vedic", "6 (छह)"),
        Pair("Water boils at", "100°C (सौ डिग्री)")
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Section Hero Banner
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.List, contentDescription = "", tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(36.dp))
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = if (isHindi) "शिक्षण सामग्री पुस्तकालय" else "Teaching Material Library",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = if (isHindi) "फ्लैशकार्ड्स, प्रिंट योग्य वर्कशीट और क्लास टेस्ट का संकलन।" else "Bilingual ready-made flashcards, worksheets, and class tests.",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }

        // Feature 1: Flashcard Trainer
        item {
            Text(
                text = if (isHindi) "इंटरएक्टिव फ्लैशकार्ड्स / Flashcards" else "Interactive Classroom Flashcards",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }

        item {
            val card = flashcards[activeFlashcardIndex]
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clickable { isFrontOfCard = !isFrontOfCard }
                    .testTag("interactive_flashcard"),
                colors = CardDefaults.cardColors(
                    containerColor = if (isFrontOfCard) MaterialTheme.colorScheme.secondaryContainer
                    else MaterialTheme.colorScheme.tertiaryContainer
                ),
                shape = RoundedCornerShape(24.dp)
            ) {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize().padding(24.dp)) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = if (isFrontOfCard) "QUESTION / प्रश्न" else "ANSWER / उत्तर",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = if (isFrontOfCard) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
                        )

                        Text(
                            text = if (isFrontOfCard) card.first else card.second,
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Text(
                            text = if (isFrontOfCard) "(Click to flip over / उत्तर देखने के लिए क्लिक करें)"
                            else "(Click to view question / प्रश्न पर वापस जाएं)",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray
                        )
                    }
                }
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Card ${activeFlashcardIndex + 1} of ${flashcards.size}",
                    style = MaterialTheme.typography.bodySmall
                )

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(
                        onClick = {
                            if (activeFlashcardIndex > 0) activeFlashcardIndex--
                            isFrontOfCard = true
                        },
                        enabled = activeFlashcardIndex > 0,
                        modifier = Modifier.testTag("flashcard_prev_btn")
                    ) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Prev")
                        Text("Prev")
                    }

                    Button(
                        onClick = {
                            if (activeFlashcardIndex < flashcards.size - 1) activeFlashcardIndex++
                            isFrontOfCard = true
                        },
                        enabled = activeFlashcardIndex < flashcards.size - 1,
                        modifier = Modifier.testTag("flashcard_next_btn")
                    ) {
                        Text("Next")
                        Icon(imageVector = Icons.Default.ArrowForward, contentDescription = "Next")
                    }
                }
            }
        }

        // Printable worksheets list
        item {
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Icon(imageVector = Icons.Default.List, contentDescription = "", tint = MaterialTheme.colorScheme.primary)
                    Text(
                        text = if (isHindi) "मेरे वर्कशीट पुस्तकालय" else "My Saved Worksheets",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        if (worksheets.isEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Box(modifier = Modifier.padding(24.dp), contentAlignment = Alignment.Center) {
                        Text(
                            text = if (isHindi) "कोई कस्टम वर्कशीट नहीं सहेजी गई है। आप एआई सहायक में जाकर तुरंत नई वर्कशीट बना सकते हैं!"
                            else "No custom worksheets saved yet. Visit the AI Teaching Assistant tab to generate rich worksheets instantly!",
                            style = MaterialTheme.typography.bodyMedium,
                            lineHeight = 20.sp
                        )
                    }
                }
            }
        } else {
            items(worksheets) { sheet ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("worksheet_item_${sheet.id}"),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Top
                        ) {
                            Column {
                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                                    Badge { Text("Class ${sheet.grade}") }
                                    Badge(containerColor = MaterialTheme.colorScheme.secondaryContainer) { Text(sheet.subject) }
                                    if (sheet.isAiGenerated) {
                                        Badge(containerColor = MaterialTheme.colorScheme.tertiaryContainer) { Text("AI Active") }
                                    }
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = sheet.title,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Row {
                                IconButton(
                                    onClick = {
                                        clipboardManager.setText(AnnotatedString(sheet.content))
                                        Toast.makeText(context, "Worksheet copied to clipboard!", Toast.LENGTH_SHORT).show()
                                    },
                                    modifier = Modifier.testTag("copy_worksheet_${sheet.id}")
                                ) {
                                    Icon(imageVector = Icons.Default.Edit, contentDescription = "Copy")
                                }

                                IconButton(
                                    onClick = { viewModel.deleteWorksheet(sheet.id) },
                                    modifier = Modifier.testTag("delete_worksheet_${sheet.id}")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Delete",
                                        tint = MaterialTheme.colorScheme.error
                                    )
                                }
                            }
                        }

                        Text(
                            text = sheet.content,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 8
                        )
                    }
                }
            }
        }
    }
}
