package com.android.quizcafe.core.dataStore

import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val authManager: AuthManager
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token: String = runBlocking {
            authManager.getAccessToken()
        }

        val request = chain.request().newBuilder().header("Authorization", "Bearer $token").build()

        return chain.proceed(request)
    }
}