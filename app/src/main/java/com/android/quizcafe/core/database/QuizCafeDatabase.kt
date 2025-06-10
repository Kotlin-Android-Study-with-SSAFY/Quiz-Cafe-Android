package com.android.quizcafe.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.android.quizcafe.core.database.dao.quizBook.QuizBookDao
import com.android.quizcafe.core.database.dao.quiz.QuizDao
import com.android.quizcafe.core.database.dao.quiz.QuizGradeDao
import com.android.quizcafe.core.database.dao.quizBook.QuizBookGradeDao
import com.android.quizcafe.core.database.model.QuizBookEntity
import com.android.quizcafe.core.database.model.quiz.QuizEntity
import com.android.quizcafe.core.database.converter.DurationConverter
import com.android.quizcafe.core.database.model.grading.QuizBookGradeEntity
import com.android.quizcafe.core.database.model.grading.QuizGradeEntity
import com.android.quizcafe.core.database.model.quiz.McqOptionEntity

@Database(
    entities = [
        QuizEntity::class,
        QuizBookEntity::class,
        QuizGradeEntity::class,
        QuizBookGradeEntity::class,
        McqOptionEntity::class
    ],
    version = 1
)
@TypeConverters(
    DurationConverter::class
)
abstract class QuizCafeDatabase : RoomDatabase(){
    abstract fun quizDao(): QuizDao
    abstract fun quizBookDao(): QuizBookDao
    abstract fun quizGradeDao(): QuizGradeDao
    abstract fun quizBookGradeDao(): QuizBookGradeDao
}
