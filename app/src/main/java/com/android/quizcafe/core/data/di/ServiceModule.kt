package com.android.quizcafe.core.data.di

import com.android.quizcafe.core.data.remote.service.AuthService
import com.android.quizcafe.core.data.remote.service.QuizBookService
import com.android.quizcafe.core.data.remote.service.UserService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ServiceModule {

    @Provides
    @Singleton
    fun provideAuthService(
        @Named("default") retrofit: Retrofit
    ): AuthService = retrofit.create(AuthService::class.java)

    @Provides
    @Singleton
    fun provideQuizBookService(
        @Named("token") retrofit: Retrofit
    ): QuizBookService = retrofit.create(QuizBookService::class.java)

    @Provides
    @Singleton
    fun provideUserService(
        @Named("token") retrofit: Retrofit
    ) : UserService = retrofit.create(UserService::java.)
}
