package com.android.quizcafe.core.datastore

import android.util.Log
import com.android.quizcafe.core.common.network.di.ApplicationScope
import com.android.quizcafe.core.datastore.di.GoogleAuthManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class AuthManager @Inject constructor(
    private val authDataStore: AuthDataStore,
    private val googleAuthManager: GoogleAuthManager,
    @ApplicationScope private val applicationScope: CoroutineScope
) {
    @Volatile
    private var cachedAccessToken: String? = null

    @Volatile
    private var cachedRefreshToken: String? = null

    @Volatile
    private var cachedUserEmail: String? = null

    private val _logoutEvent = MutableSharedFlow<LogoutReason>(extraBufferCapacity = 1)
    val logoutEvent: SharedFlow<LogoutReason> = _logoutEvent

    init {
        applicationScope.launch {
            launch {
                authDataStore.accessTokenFlow.collect { token ->
                    Log.d("Init AuthManager access token", "$token")
                    cachedAccessToken = token
                }
            }

            launch {
                authDataStore.refreshTokenFlow.collect { token ->
                    Log.d("Init AuthManager refresh token", "$token")
                    cachedRefreshToken = token
                }
            }

            launch {
                authDataStore.userEmailFlow.collect { email ->
                    Log.d("Init AuthManager user email", "$email")
                    cachedUserEmail = email
                }
            }
        }
    }

    fun getAccessToken(): String? = cachedAccessToken

    fun getRefreshToken(): String? = cachedRefreshToken

    fun getUserEmail(): String? = cachedUserEmail

    fun saveToken(accessToken: String, refreshToken: String) {
        Log.d("AuthManager", "save token")
        // 메모리는 즉시 업데이트
        cachedAccessToken = accessToken
        cachedRefreshToken = refreshToken

        CoroutineScope(Dispatchers.IO).launch {
            authDataStore.saveAccessToken(accessToken)
            authDataStore.saveRefreshToken(refreshToken)
        }
    }

    fun saveUserEmail(email: String) {
        Log.d("AuthManager", "save email")
        cachedUserEmail = email

        CoroutineScope(Dispatchers.IO).launch {
            authDataStore.saveUserEmail(email)
        }
    }

    fun logout(reason: LogoutReason) {
        cachedAccessToken = null
        cachedRefreshToken = null

        CoroutineScope(Dispatchers.IO).launch {
            authDataStore.deleteAccessToken()
            authDataStore.deleteRefreshToken()
            authDataStore.deleteUserEmail()

            _logoutEvent.emit(reason)
        }
    }

    suspend fun signInWithGoogle(): String? {
        return googleAuthManager.signInWithGoogle()
    }

    suspend fun googleLogout() {
        googleAuthManager.googleLogout()
    }
}
