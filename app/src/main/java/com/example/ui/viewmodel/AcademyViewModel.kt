package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.database.*
import com.example.data.network.RetrofitClient
import com.example.data.repository.AcademyRepository
import com.example.data.repository.CurriculumTopic
import com.example.data.repository.StudyClass
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class AcademyViewModel(application: Application) : AndroidViewModel(application) {

    private val database = TeacherBaseDatabase.getInstance(application)
    private val repository = AcademyRepository(database.dao)

    // --- Core UI State Variables ---
    val isHindi = MutableStateFlow(false) // Toggle Hindi or English bilingual view
    val searchQuery = MutableStateFlow("")
    val currentTab = MutableStateFlow("dashboard") // dashboard, training, curriculum, foundations, addons, materials, ai_assistant
    val activeClassLevel = MutableStateFlow("Nursery") // Active selected standard

    // --- Interactive Drawing Canvas State ---
    val canvasLines = MutableStateFlow<List<Line>>(emptyList())

    // --- Room Reactive Flows ---
    val allNotes: StateFlow<List<TeacherNote>> = repository.allNotes
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allLessonPlans: StateFlow<List<LessonPlan>> = repository.allLessonPlans
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allStudentReports: StateFlow<List<StudentReport>> = repository.allStudentReports
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allWorksheets: StateFlow<List<CustomWorksheet>> = repository.allWorksheets
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allQuizAttempts: StateFlow<List<QuizAttempt>> = repository.allQuizAttempts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Daily Learning Roadmap State
    private val _todayProgress = MutableStateFlow<RoadmapProgress?>(null)
    val todayProgress: StateFlow<RoadmapProgress?> = _todayProgress.asStateFlow()

    // --- AI assistant parameters & logs ---
    val aiResponseText = MutableStateFlow("")
    val isAiGenerating = MutableStateFlow(false)

    // --- Fallback & Static Educational Content getters ---
    val curriculumSyllabus: List<StudyClass> = repository.curriculumSyllabus
    val trainingModules: List<CurriculumTopic> = repository.teacherTrainingModules
    val hindiLessons: List<CurriculumTopic> = repository.hindiFoundationLessons
    val englishLessons: List<CurriculumTopic> = repository.englishFoundationLessons
    val mathLessons: List<CurriculumTopic> = repository.mathFoundationLessons
    val gkLessons: List<CurriculumTopic> = repository.gkSyllabus
    val reasoningLessons: List<CurriculumTopic> = repository.reasoningLessons
    val moralStories: List<CurriculumTopic> = repository.storiesCollection

    init {
        loadTodayProgress()
    }

    // --- Search Logic Across All Assets ---
    val filteredSearchResults: StateFlow<Map<String, List<CurriculumTopic>>> = combine(
        searchQuery,
        isHindi
    ) { query, hindiMode ->
        if (query.trim().isEmpty()) {
            return@combine emptyMap()
        }

        val lowercaseQuery = query.lowercase(Locale.ROOT)
        val result = mutableMapOf<String, List<CurriculumTopic>>()

        // Helper to check match
        fun matches(topic: CurriculumTopic): Boolean {
            return topic.titleEnglish.lowercase(Locale.ROOT).contains(lowercaseQuery) ||
                   topic.titleHindi.contains(lowercaseQuery) ||
                   topic.contentEnglish.lowercase(Locale.ROOT).contains(lowercaseQuery) ||
                   topic.contentHindi.contains(lowercaseQuery)
        }

        // Search Training
        val matchingTraining = trainingModules.filter { matches(it) }
        if (matchingTraining.isNotEmpty()) result["Teacher Training"] = matchingTraining

        // Search Foundations
        val matchingHindi = hindiLessons.filter { matches(it) }
        if (matchingHindi.isNotEmpty()) result["Hindi Foundation"] = matchingHindi

        val matchingEnglish = englishLessons.filter { matches(it) }
        if (matchingEnglish.isNotEmpty()) result["English Foundation"] = matchingEnglish

        val matchingMath = mathLessons.filter { matches(it) }
        if (matchingMath.isNotEmpty()) result["Mathematics Foundation"] = matchingMath

        // Search Addons
        val matchingGk = gkLessons.filter { matches(it) }
        if (matchingGk.isNotEmpty()) result["General Knowledge"] = matchingGk

        val matchingReasoning = reasoningLessons.filter { matches(it) }
        if (matchingReasoning.isNotEmpty()) result["Reasoning Section"] = matchingReasoning

        val matchingStories = moralStories.filter { matches(it) }
        if (matchingStories.isNotEmpty()) result["Moral Story Library"] = matchingStories

        result
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyMap())


    // --- Roadmap Planner Functions ---
    private fun getTodayDateString(): String {
        return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
    }

    fun loadTodayProgress() {
        viewModelScope.launch(Dispatchers.IO) {
            val dateStr = getTodayDateString()
            var prog = repository.getProgressForDate(dateStr)
            if (prog == null) {
                prog = RoadmapProgress(dateStr)
                repository.saveProgress(prog)
            }
            _todayProgress.value = prog
        }
    }

    fun updateProgress(subject: String, minutes: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val current = _todayProgress.value ?: RoadmapProgress(getTodayDateString())
            val updated = when (subject) {
                "Hindi" -> current.copy(hindiMinutes = (current.hindiMinutes + minutes).coerceAtMost(30))
                "English" -> current.copy(englishMinutes = (current.englishMinutes + minutes).coerceAtMost(30))
                "Mathematics" -> current.copy(mathMinutes = (current.mathMinutes + minutes).coerceAtMost(30))
                "GK" -> current.copy(gkMinutes = (current.gkMinutes + minutes).coerceAtMost(15))
                "Reasoning" -> current.copy(reasoningMinutes = (current.reasoningMinutes + minutes).coerceAtMost(15))
                else -> current
            }
            repository.saveProgress(updated)
            _todayProgress.value = updated
        }
    }


    // --- Notes Management ---
    fun addNote(title: String, content: String, category: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val newNote = TeacherNote(title = title, content = content, category = category)
            repository.insertNote(newNote)
        }
    }

    fun deleteNote(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteNoteById(id)
        }
    }


    // --- Lesson Plan Customizer (Local Database) ---
    fun addLessonPlan(
        classLevel: String,
        subject: String,
        title: String,
        objectives: String,
        methodology: String,
        activity: String,
        assessment: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val plan = LessonPlan(
                classLevel = classLevel,
                subject = subject,
                title = title,
                objectives = objectives,
                methodology = methodology,
                activity = activity,
                assessment = assessment
            )
            repository.insertLessonPlan(plan)
        }
    }

    fun deleteLessonPlan(plan: LessonPlan) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteLessonPlan(plan)
        }
    }


    // --- Student Progress & Analytics Report Cards ---
    fun addStudentReport(
        name: String,
        className: String,
        strengths: String,
        weaknesses: String,
        academicGrade: String,
        behaviourGrade: String,
        feedback: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val report = StudentReport(
                studentName = name,
                className = className,
                strengths = strengths,
                weaknesses = weaknesses,
                academicGrade = academicGrade,
                behaviourGrade = behaviourGrade,
                personalizedFeedback = feedback
            )
            repository.insertStudentReport(report)
        }
    }

    fun deleteStudentReport(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteStudentReportById(id)
        }
    }


    // --- Custom Printable Worksheets & Materials ---
    fun addWorksheet(title: String, content: String, grade: String, subject: String, isAi: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            val sheet = CustomWorksheet(
                title = title,
                content = content,
                grade = grade,
                subject = subject,
                isAiGenerated = isAi
            )
            repository.insertWorksheet(sheet)
        }
    }

    fun deleteWorksheet(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteWorksheetById(id)
        }
    }


    // --- AI Generation Assistant (Section 10) ---
    fun generateAiContent(promptType: String, grade: String, subject: String, customTopic: String) {
        viewModelScope.launch(Dispatchers.IO) {
            isAiGenerating.value = true
            aiResponseText.value = "Generative AI is crafting your materials... / एआई आपकी सामग्री तैयार कर रहा है..."

            val systemInstruction = "You are a professional teacher trainer and educational content builder. " +
                    "Generate extremely thorough, bilingual (Hindi & English), structured educational content " +
                    "that is easy to follow for primary school teacher guides. " +
                    "Format properly using bold headers, bullet items, and localized examples."

            val prompt = when (promptType) {
                "Lesson Plan" -> "Generate a detailed lesson plan for Grade/Standard: $grade, Subject: $subject, Topic: $customTopic. Include warmups, objectives, direct activities, playground learning, and quiz evaluations."
                "Worksheet" -> "Create a printable, copyable questions worksheet for Class: $grade, Subject: $subject on topic: $customTopic. Provide 5 clear fill-in-the-blanks, 5 match-the-pair, and 3 high-order thinking verbal questions with localized Hindi-English names."
                "Quizzes" -> "Generate a playful interactive quiz containing 5 multiple choice questions with answer keys on $subject, Topic: $customTopic, suitable for the educational stage of $grade."
                "Activities" -> "Generate 3 play-way fun kinetic activities or physical classroom games to teach Class: $grade, Subject: $subject, Topic: $customTopic. List materials needed, and rules."
                "Feedback" -> "Draft professional, compassionate report card feedback comments, both in English and sweet Hindi translations, for a student in Class $grade struggling in $subject ($customTopic) but behaving exceptionally well (Grade A)."
                else -> "Generate general primary syllabus guidelines for Class: $grade, Subject: $subject, Topic: $customTopic."
            }

            val result = RetrofitClient.getGeminiResponse(prompt, systemInstruction)
            aiResponseText.value = result
            isAiGenerating.value = false
        }
    }

    fun saveGeneratedAsWorksheet(title: String, grade: String, subject: String) {
        val body = aiResponseText.value
        if (body.isNotEmpty() && !body.startsWith("Error") && !body.startsWith("Generative")) {
            addWorksheet(title, body, grade, subject, isAi = true)
        }
    }

    fun saveQuizScore(topic: String, score: Int, max: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertQuizAttempt(QuizAttempt(topic = topic, score = score, maxScore = max))
        }
    }

    // Canvas actions
    fun clearCanvas() {
        canvasLines.value = emptyList()
    }
}

// Canvas structures
data class Line(
    val startX: Float,
    val startY: Float,
    val endX: Float,
    val endY: Float,
    val colorHex: String = "#FF6B6B"
)
