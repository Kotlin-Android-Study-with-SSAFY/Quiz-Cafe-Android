package com.android.quizcafe.core.datastore

import com.android.quizcafe.core.common.network.di.ApplicationScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class AuthManager @Inject constructor(
    private val authDataStore: AuthDataStore,
    @ApplicationScope private val applicationScope: CoroutineScope
) {
    @Volatile
    private var cachedToken: String? = null
    @Volatile
    private var cachedUserEmail: String? = null

    private val _logoutEvent = MutableSharedFlow<LogoutReason>(extraBufferCapacity = 1)
    val logoutEvent: SharedFlow<LogoutReason> = _logoutEvent

    init {
        applicationScope.launch {
            authDataStore.accessTokenFlow.collect { token ->
                cachedToken = token
            launch {
                authDataStore.userEmailFlow.collect { email ->
                    Log.d("Init AuthManager user email", "$email")
                    cachedUserEmail = email
                }
            }
        }
    }
    fun getToken(): String? = cachedToken

    suspend fun saveAccessToken(token: String) {
        authDataStore.saveAccessToken(token)
        cachedToken = token
    fun getUserEmail(): String? = cachedUserEmail
    }

    suspend fun logout(reason: LogoutReason) {
        authDataStore.deleteAccessToken()
        cachedToken = null
    fun saveUserEmail(email: String) {
        Log.d("AuthManager", "save email")
        cachedUserEmail = email

        CoroutineScope(Dispatchers.IO).launch {
            authDataStore.saveUserEmail(email)
        }
    }

    }
}
