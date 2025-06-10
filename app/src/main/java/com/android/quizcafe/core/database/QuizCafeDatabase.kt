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
import com.android.quizcafe.core.database.converter.QuizBookGradeIdConverter
import com.android.quizcafe.core.database.converter.QuizBookIdConverter
import com.android.quizcafe.core.database.converter.QuizIdConverter

@Database(
    entities = [
        QuizEntity::class,
        QuizBookEntity::class
    ],
    version = 1
)
@TypeConverters(
    DurationConverter::class,
    QuizIdConverter::class,
    QuizBookIdConverter::class,
    QuizBookGradeIdConverter::class,
)
internal abstract class QuizCafeDatabase : RoomDatabase(){
    abstract fun quizDao(): QuizDao
    abstract fun quizBookDao(): QuizBookDao
    abstract fun quizGradingRecordDao(): QuizGradeDao
    abstract fun quizBookGradingRecordDao(): QuizBookGradeDao
}
