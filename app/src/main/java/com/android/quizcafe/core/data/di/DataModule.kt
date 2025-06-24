package com.android.quizcafe.core.data.di

import com.android.quizcafe.core.data.repository.AuthRepositoryImpl
import com.android.quizcafe.core.data.repository.QuizBookRepositoryImpl
import com.android.quizcafe.core.data.repository.QuizBookSolvingRepositoryImpl
import com.android.quizcafe.core.data.repository.QuizRepositoryImpl
import com.android.quizcafe.core.domain.repository.AuthRepository
import com.android.quizcafe.core.domain.repository.QuizBookRepository
import com.android.quizcafe.core.domain.repository.QuizBookSolvingRepository
import com.android.quizcafe.core.domain.repository.QuizRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal interface DataModule {

    @Singleton
    @Binds
    fun bindAuthRepository(
        authRepository: AuthRepositoryImpl
    ): AuthRepository

    @Singleton
    @Binds
    fun bindQuizBookRepository(
        quizBookRepository: QuizBookRepositoryImpl
    ): QuizBookRepository

    @Singleton
    @Binds
    fun bindQuizRepository(
        quizRepository: QuizRepositoryImpl
    ): QuizRepository

    @Singleton
    @Binds
    fun bindQuizBookSolvingRepository(
        quizBookSolvingRepository: QuizBookSolvingRepositoryImpl
    ): QuizBookSolvingRepository
}
