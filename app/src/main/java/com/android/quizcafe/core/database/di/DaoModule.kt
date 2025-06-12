package com.android.quizcafe.core.database.di

import com.android.quizcafe.core.database.QuizCafeDatabase
import com.android.quizcafe.core.database.dao.quiz.QuizDao
import com.android.quizcafe.core.database.dao.quiz.QuizGradeDao
import com.android.quizcafe.core.database.dao.quizBook.QuizBookDao
import com.android.quizcafe.core.database.dao.quizBook.QuizBookGradeDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal object DaoModule {

    @Provides
    fun provideQuizDao(
        database: QuizCafeDatabase
    ): QuizDao = database.quizDao()

    @Provides
    fun provideQuizBookDao(
        database: QuizCafeDatabase
    ): QuizBookDao = database.quizBookDao()

    @Provides
    fun provideQuizGradingRecordDao(
        database: QuizCafeDatabase
    ): QuizGradeDao = database.quizGradeDao()

    @Provides
    fun provideQuizBookGradingRecordDao(
        database: QuizCafeDatabase
    ): QuizBookGradeDao = database.quizBookGradeDao()
}
