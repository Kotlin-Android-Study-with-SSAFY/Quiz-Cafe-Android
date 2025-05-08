package com.android.quizcafe.di

import com.android.quizcafe.core.data.remote.service.AuthService
import com.android.quizcafe.core.network.di.NetworkModule
import com.android.quizcafe.core.network.util.calladapter.NetworkResultCallAdapterFactory
import com.android.quizcafe.core.network.util.convertor.NullOnEmptyConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import javax.inject.Named
import javax.inject.Singleton


@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [NetworkModule::class]
)
object FakeNetworkModule {
    private const val BASE_URL = "http://localhost:8080/"

    @Provides
    @Singleton
    @Named("default")
    fun provideFakeRetrofit(
        @Named("default") okHttpClient: OkHttpClient
    ): Retrofit {
        val contentType = "application/json".toMediaType()
        val json = Json { ignoreUnknownKeys = true }

        return Retrofit.Builder()
            .baseUrl("http://localhost:8080/")
            .client(okHttpClient)
            .addConverterFactory(NullOnEmptyConverterFactory)
            .addConverterFactory(json.asConverterFactory(contentType))
            .addCallAdapterFactory(NetworkResultCallAdapterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    @Named("default")
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY // 테스트에서 HTTP 로깅
            })
            .build()
    }
}