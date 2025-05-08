package com.android.quizcafe.core.network.di

import com.android.quizcafe.BuildConfig
import com.android.quizcafe.core.datastore.AuthInterceptor
import com.android.quizcafe.core.datastore.AuthManager
import com.android.quizcafe.core.network.util.calladapter.NetworkResultCallAdapterFactory
import com.android.quizcafe.core.network.util.convertor.NullOnEmptyConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private val baseUrl = BuildConfig.BASE_URL.toHttpUrl()

    @Singleton
    @Provides
    @Named("default")
    fun provideDefaultRetrofit(
        @Named("default") okHttpClient: OkHttpClient
    ): Retrofit {
        val contentType = "application/json".toMediaType()
        val json = Json { ignoreUnknownKeys = true }
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(NullOnEmptyConverterFactory)
            .addConverterFactory(json.asConverterFactory(contentType))
            .addCallAdapterFactory(NetworkResultCallAdapterFactory.create())
            .build()
    }

    /**
     * TODO : authenticated Retrofit 구현
     * - authInterceptor 달기
     */
    @Provides
    @Singleton
    fun provideAuthInterceptor(
        authManager: AuthManager
    ): Interceptor = AuthInterceptor(authManager)

    @Provides
    @Singleton
    @Named("default")
    fun provideDefaultOkHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor().apply {
            this.setLevel(HttpLoggingInterceptor.Level.BODY)
        }
        return OkHttpClient.Builder()
            .connectTimeout(5000, TimeUnit.MILLISECONDS)
            .addInterceptor(logging)
            .build()
    }

    @Provides
    @Singleton
    @Named("token")
    fun provideTokenOkHttpClient(
        authInterceptor: Interceptor
    ): OkHttpClient {
        val logging = HttpLoggingInterceptor().apply {
            this.setLevel(HttpLoggingInterceptor.Level.BODY)
        }
        return OkHttpClient.Builder()
            .connectTimeout(5000, TimeUnit.MILLISECONDS)
            .addInterceptor(authInterceptor)
            .addInterceptor(logging)
            .build()
    }



    @Provides
    @Singleton
    @Named("token")
    fun provideTokenRetrofit(
        okHttpClient: OkHttpClient
    ): Retrofit {
        val contentType = "application/json".toMediaType()
        val json = Json { ignoreUnknownKeys = true }
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(NullOnEmptyConverterFactory)
            .addConverterFactory(json.asConverterFactory(contentType))
            .addCallAdapterFactory(NetworkResultCallAdapterFactory.create())
            .build()
    }
    // TODO: Retrofit 객체 - Dagger.Lazy를 활용한 지연 초기화로 최적화하기
}
