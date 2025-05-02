package com.android.quizcafe.core.datastore


import com.android.quizcafe.core.common.network.di.ApplicationScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

import kotlinx.coroutines.launch
import javax.inject.Inject


class AuthManager @Inject constructor(
    private val authDataStore: AuthDataStore,
    @ApplicationScope private val applicationScope: CoroutineScope
) {
    @Volatile
    private var cachedToken: String? = null

    init {
        applicationScope.launch {
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