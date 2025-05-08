package com.android.quizcafe.di

import com.android.quizcafe.core.data.di.ServiceModule
import com.android.quizcafe.core.data.remote.service.AuthService
import com.android.quizcafe.core.network.util.calladapter.NetworkResultCallAdapterFactory
import com.android.quizcafe.core.network.util.convertor.NullOnEmptyConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
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
    fun provideAuthService(@Named("default") retrofit: Retrofit) : AuthService{
        return retrofit.create(AuthService::class.java)
    }

}