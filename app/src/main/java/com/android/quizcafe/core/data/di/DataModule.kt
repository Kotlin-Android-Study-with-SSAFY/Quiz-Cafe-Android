package com.android.quizcafe.core.data.di

import com.android.quizcafe.core.data.repository.AuthRepositoryImpl
import com.android.quizcafe.core.domain.repository.AuthRepository
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
}