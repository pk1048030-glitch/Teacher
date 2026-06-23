package com.example.data.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TeacherBaseDao {

    // Lesson Plans
    @Query("SELECT * FROM lesson_plans")
    fun getAllLessonPlans(): Flow<List<LessonPlan>>

    @Query("SELECT * FROM lesson_plans WHERE classLevel = :className")
    fun getLessonPlansForClass(className: String): Flow<List<LessonPlan>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLessonPlan(plan: LessonPlan)

    @Delete
    suspend fun deleteLessonPlan(plan: LessonPlan)

    // Quiz Attempts
    @Query("SELECT * FROM quiz_attempts ORDER BY dateMillis DESC")
    fun getAllQuizAttempts(): Flow<List<QuizAttempt>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuizAttempt(attempt: QuizAttempt)

    // Roadmap Progress
    @Query("SELECT * FROM roadmap_progress WHERE dateStr = :dateStr")
    suspend fun getProgressForDate(dateStr: String): RoadmapProgress?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProgress(progress: RoadmapProgress)

    @Query("SELECT * FROM roadmap_progress ORDER BY dateStr DESC LIMIT 30")
    fun getRecentProgress(): Flow<List<RoadmapProgress>>

    // Teacher Notes
    @Query("SELECT * FROM teacher_notes ORDER BY dateMillis DESC")
    fun getAllNotes(): Flow<List<TeacherNote>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: TeacherNote)

    @Query("DELETE FROM teacher_notes WHERE id = :id")
    suspend fun deleteNoteById(id: Int)

    // Student Reports
    @Query("SELECT * FROM student_reports ORDER BY dateMillis DESC")
    fun getAllStudentReports(): Flow<List<StudentReport>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStudentReport(report: StudentReport)

    @Query("DELETE FROM student_reports WHERE id = :id")
    suspend fun deleteStudentReportById(id: Int)

    // Custom Worksheets
    @Query("SELECT * FROM custom_worksheets ORDER BY dateMillis DESC")
    fun getAllWorksheets(): Flow<List<CustomWorksheet>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorksheet(sheet: CustomWorksheet)

    @Query("DELETE FROM custom_worksheets WHERE id = :id")
    suspend fun deleteWorksheetById(id: Int)
}
