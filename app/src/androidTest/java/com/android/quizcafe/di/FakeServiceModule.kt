package com.android.quizcafe.di

import com.android.quizcafe.core.data.di.ServiceModule
import com.android.quizcafe.core.data.remote.service.AuthService
import com.android.quizcafe.core.data.remote.service.QuizBookService
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [ServiceModule::class]
)
object FakeServiceModule {

    @Provides
    @Singleton
    fun provideAuthService(@Named("default") retrofit: Retrofit): AuthService {
        return retrofit.create(AuthService::class.java)
    }

    @Provides
    @Singleton
    fun provideQuizBookService(@Named("token") retrofit: Retrofit): QuizBookService {
        return retrofit.create(QuizBookService::class.java)
    }
}
