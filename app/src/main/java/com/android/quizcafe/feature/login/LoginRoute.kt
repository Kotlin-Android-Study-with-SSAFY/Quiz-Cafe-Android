package com.android.quizcafe.feature.login

import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import com.android.quizcafe.R
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun LoginRoute(
    navigateToSignUp: () -> Unit,
    navigateToHome: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val credentialManager = remember { CredentialManager.create(context) }
    val state by viewModel.state.collectAsState()

    suspend fun handleGoogleLogin() {
        try {
            val googleIdOption = GetGoogleIdOption.Builder()
                .setServerClientId(context.getString(R.string.default_web_client_id))
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
            viewModel.sendIntent(LoginIntent.GoogleLogin(googleCredential.idToken))

        } catch (e: Exception) {
            Toast.makeText(context, "Google 로그인 실패: ${e.message}", Toast.LENGTH_SHORT).show()
            Log.d("googleLogin", "LoginRoute: ${e.message}")
        }
    }

    fun handleGoogleLogout(
        onComplete: () -> Unit = {}
    ) {
        viewModel.viewModelScope.launch {
            try {
                credentialManager.clearCredentialState(ClearCredentialStateRequest())
                Log.d("googleLogout", "Credential 상태 초기화 성공")
            } catch (e: Exception) {
                Log.e("googleLogout", "Credential 상태 초기화 실패: ${e.message}",)
            }
            withContext(Dispatchers.Main) {
                onComplete()
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                LoginEffect.NavigateToHome -> {
                    navigateToHome()
                    Toast.makeText(
                        context,
                        context.getString(R.string.success_login),
                        Toast.LENGTH_SHORT
                    ).show()
                }

                LoginEffect.NavigateToSignUp -> {
                    navigateToSignUp()
                }

                is LoginEffect.ShowErrorDialog -> {
                    Toast.makeText(
                        context,
                        context.getString(R.string.error_message),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    LoginScreen(
        state = state,
        sendIntent = viewModel::sendIntent,
        onClickGoogleLogin = {
            viewModel.viewModelScope.launch {
                handleGoogleLogin()
            }
        }
    )
}
