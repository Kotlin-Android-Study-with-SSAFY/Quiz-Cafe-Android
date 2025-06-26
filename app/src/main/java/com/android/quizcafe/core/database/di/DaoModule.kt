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
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object DaoModule {

    @Provides
    @Singleton
    fun provideQuizDao(
        database: QuizCafeDatabase
    ): QuizDao = database.quizDao()

    @Provides
    @Singleton
    fun provideQuizBookDao(
        database: QuizCafeDatabase
    ): QuizBookDao = database.quizBookDao()

    @Provides
    @Singleton
    fun provideQuizGradingRecordDao(
        database: QuizCafeDatabase
    ): QuizGradeDao = database.quizGradeDao()

    @Provides
    @Singleton
    fun provideQuizBookGradingRecordDao(
        database: QuizCafeDatabase
    ): QuizBookGradeDao = database.quizBookGradeDao()
}
