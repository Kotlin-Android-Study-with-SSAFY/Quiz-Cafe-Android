package com.android.quizcafe.core.datastore.di

import android.content.Context
import android.util.Log
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import com.android.quizcafe.BuildConfig
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class GoogleAuthManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    suspend fun signInWithGoogle(): String? {
        return try {
            val credentialManager = CredentialManager.create(context)
            val googleIdOption = GetGoogleIdOption.Builder()
                .setServerClientId(BuildConfig.GOOGLE_CLIENT_ID)
                .setFilterByAuthorizedAccounts(false)
                .build()

            val request = GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build()

            val result: GetCredentialResponse = credentialManager.getCredential(
                request = request,
                context = context
            )

            val credential = result.credential
            val googleCredential = GoogleIdTokenCredential.createFrom(credential.data)
            googleCredential.idToken
        } catch (e: Exception) {
            Log.d("googleLogin", "LoginRoute: ${e.message}")
            null
        }
    }

    suspend fun googleLogout() {
        try {
            val credentialManager = CredentialManager.create(context)
            credentialManager.clearCredentialState(ClearCredentialStateRequest())
            Log.d("GoogleAuth", "Credential 상태 초기화 성공")
        } catch (e: Exception) {
            Log.e("GoogleAuth", "Credential 상태 초기화 실패: ${e.message}",)
        }
    }
}
