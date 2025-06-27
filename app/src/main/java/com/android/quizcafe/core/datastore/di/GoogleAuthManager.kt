package com.android.quizcafe.core.datastore.di

import android.content.Context
import android.util.Log
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import com.android.quizcafe.BuildConfig
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class GoogleAuthManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val credentialManager: CredentialManager = CredentialManager.create(context)
) {

    companion object {
        private const val TAG = "GoogleAuth"
    }

    private fun buildGoogleIdOption() = GetGoogleIdOption.Builder()
        .setServerClientId(BuildConfig.GOOGLE_CLIENT_ID)
        .setFilterByAuthorizedAccounts(false)
        .build()

    private fun buildGetCredentialRequest() = GetCredentialRequest.Builder()
        .addCredentialOption(buildGoogleIdOption())
        .build()

    /**
     * Google One‐Tap 로그인 시도 후 ID 토큰을 반환합니다.
     * 실패 시 null.
     */
    suspend fun signInWithGoogle(): String? = runCatching {
        credentialManager
            .getCredential(
                request = buildGetCredentialRequest(),
                context = context
            )
            .credential
            .data
            .let(GoogleIdTokenCredential::createFrom)
            .idToken
    }
        .onFailure { Log.d(TAG, "signInWithGoogle failed: ${it.message}") }
        .getOrNull()

    suspend fun googleLogout() {
        runCatching {
            credentialManager.clearCredentialState(
                ClearCredentialStateRequest()
            )
        }
            .onSuccess { Log.d(TAG, "googleLogout: cleared successfully") }
            .onFailure { Log.e(TAG, "googleLogout failed: ${it.message}") }
    }
}
