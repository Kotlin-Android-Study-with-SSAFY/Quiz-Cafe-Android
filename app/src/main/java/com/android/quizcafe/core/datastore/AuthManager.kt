package com.android.quizcafe.core.datastore


import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

import kotlinx.coroutines.launch
import javax.inject.Inject


class AuthManager @Inject constructor(
    private val authDataStore: AuthDataStore
) {
    @Volatile
    private var cachedToken: String? = null

    init {
        CoroutineScope(SupervisorJob() + Dispatchers.IO).launch {
            authDataStore.accessTokenFlow.collect { token ->
                cachedToken = token
            }
        }
    }
    fun getToken(): String? = cachedToken

    suspend fun saveAccessToken(token: String) {
        authDataStore.saveAccessToken(token)
        cachedToken = token
    }

    suspend fun deleteAccessToken() {
        authDataStore.deleteAccessToken()
        cachedToken = null
    }
}