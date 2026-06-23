package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import com.example.ui.screens.*
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.AcademyViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // Responsive Edge to edge layout
        setContent {
            MyApplicationTheme {
                val viewModel: AcademyViewModel = viewModel()
                MainAppHub(viewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppHub(viewModel: AcademyViewModel) {
    val isHindi by viewModel.isHindi.collectAsStateWithLifecycle()
    val currentTab by viewModel.currentTab.collectAsStateWithLifecycle()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = if (isHindi) "टीचर बेस अकैडमी" else "Teacher Base Academy",
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Text(
                            text = if (isHindi) "शिशु व प्राथमिक शिक्षक सहायक" else "Primary Teacher Companion",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                actions = {
                    // Bilingual Toggle (Hindi / English)
                    IconButton(
                        onClick = { viewModel.isHindi.value = !isHindi },
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .testTag("bilingual_toggle_btn")
                    ) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.tertiaryContainer,
                            tonalElevation = 2.dp
                        ) {
                            Box(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = if (isHindi) "ENG" else "हिंदी",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = MaterialTheme.colorScheme.onTertiaryContainer
                                )
                            }
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
                ),
                modifier = Modifier.testTag("app_top_bar")
            )
        },
        bottomBar = {
            // Material 3 Responsive bottom navigation
            NavigationBar(
                modifier = Modifier
                    .windowInsetsPadding(WindowInsets.navigationBars)
                    .testTag("app_bottom_bar")
            ) {
                // Navigation items with robust, core material icons
                val navItems = listOf(
                    Triple("dashboard", Icons.Default.Home, if (isHindi) "होम" else "Home"),
                    Triple("training", Icons.Default.List, if (isHindi) "प्रशिक्षण" else "Training"),
                    Triple("curriculum", Icons.Default.Info, if (isHindi) "पाठ्यक्रम" else "Syllabus"),
                    Triple("foundations", Icons.Default.Star, if (isHindi) "आधार" else "Foundations"),
                    Triple("addons", Icons.Default.Person, if (isHindi) "नैतिकता" else "Moral"),
                    Triple("materials", Icons.Default.Info, if (isHindi) "सामग्री" else "Library"),
                    Triple("ai_assistant", Icons.Default.Settings, if (isHindi) "एआई सहायक" else "AI Tutor")
                )

                navItems.forEach { item ->
                    NavigationBarItem(
                        selected = currentTab == item.first,
                        onClick = { viewModel.currentTab.value = item.first },
                        icon = { Icon(imageVector = item.second, contentDescription = item.third) },
                        label = { Text(item.third, fontSize = 9.sp, fontWeight = FontWeight.Bold) },
                        modifier = Modifier.testTag("nav_btn_${item.first}")
                    )
                }
            }
        }
    ) { innerPadding ->
        // Responsive screen transitions and spacing
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            AnimatedContent(
                targetState = currentTab,
                transitionSpec = {
                    fadeIn() togetherWith fadeOut()
                },
                label = "ScreenTransition"
            ) { targetTab ->
                when (targetTab) {
                    "dashboard" -> DashboardScreen(viewModel = viewModel)
                    "training" -> TeacherTrainingScreen(viewModel = viewModel)
                    "curriculum" -> CurriculumScreen(viewModel = viewModel)
                    "foundations" -> FoundationsScreen(viewModel = viewModel)
                    "addons" -> AddonsScreen(viewModel = viewModel)
                    "materials" -> MaterialLibraryScreen(viewModel = viewModel)
                    "ai_assistant" -> AIAssistantScreen(viewModel = viewModel)
                    else -> DashboardScreen(viewModel = viewModel)
                }
            }
        }
    }
}
