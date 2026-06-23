package com.example.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        LessonPlan::class,
        QuizAttempt::class,
        RoadmapProgress::class,
        TeacherNote::class,
        StudentReport::class,
        CustomWorksheet::class
    ],
    version = 1,
    exportSchema = false
)
abstract class TeacherBaseDatabase : RoomDatabase() {
    abstract val dao: TeacherBaseDao

    companion object {
        @Volatile
        private var INSTANCE: TeacherBaseDatabase? = null

        fun getInstance(context: Context): TeacherBaseDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TeacherBaseDatabase::class.java,
                    "teacher_base_academy_db"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
