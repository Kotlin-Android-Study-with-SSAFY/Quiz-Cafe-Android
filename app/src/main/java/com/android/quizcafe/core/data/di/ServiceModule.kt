package com.android.quizcafe.core.data.di

import com.android.quizcafe.core.data.remote.service.AuthService
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

}