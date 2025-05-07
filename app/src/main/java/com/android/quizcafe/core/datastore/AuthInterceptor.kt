package com.android.quizcafe.core.datastore

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val authManager: AuthManager
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = authManager.getToken()

        if (token == null) {
            Log.d("AuthInterceptor", "Token is null. Authorization header not added.")
        }

        val request = chain.request().newBuilder()
            .apply {
                if (!token.isNullOrEmpty()) {
                    addHeader("Authorization", "Bearer $token")
                }
            }
            .build()
        return chain.proceed(request)
    }
}
