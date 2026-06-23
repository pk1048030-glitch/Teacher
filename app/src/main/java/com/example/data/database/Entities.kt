package com.example.data.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "lesson_plans")
data class LessonPlan(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val classLevel: String, // Nursery, LKG, UKG, Class 1, etc.
    val subject: String,    // English, Hindi, Mathematics, Science, etc.
    val title: String,
    val duration: String = "45 mins",
    val objectives: String, // Bullet separated list
    val methodology: String,// Direct Instruction, Play-way, experiential, etc.
    val activity: String,   // Classroom activity
    val assessment: String, // Simple verbal questions
    val translatedTitle: String = "",
    val translatedObjectives: String = ""
)

@Entity(tableName = "quiz_attempts")
data class QuizAttempt(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val topic: String,
    val score: Int,
    val maxScore: Int,
    val dateMillis: Long = System.currentTimeMillis()
)

@Entity(tableName = "roadmap_progress")
data class RoadmapProgress(
    @PrimaryKey val dateStr: String, // YYYY-MM-DD
    val hindiMinutes: Int = 0,
    val englishMinutes: Int = 0,
    val mathMinutes: Int = 0,
    val gkMinutes: Int = 0,
    val reasoningMinutes: Int = 0
)

@Entity(tableName = "teacher_notes")
data class TeacherNote(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val content: String,
    val category: String = "General",
    val dateMillis: Long = System.currentTimeMillis()
)

@Entity(tableName = "student_reports")
data class StudentReport(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val studentName: String,
    val className: String,
    val strengths: String,
    val weaknesses: String,
    val behaviourGrade: String = "A",
    val academicGrade: String = "A",
    val personalizedFeedback: String,
    val dateMillis: Long = System.currentTimeMillis()
)

@Entity(tableName = "custom_worksheets")
data class CustomWorksheet(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val content: String, // Text markdown / structured string
    val grade: String,
    val subject: String,
    val isAiGenerated: Boolean = false,
    val dateMillis: Long = System.currentTimeMillis()
)
