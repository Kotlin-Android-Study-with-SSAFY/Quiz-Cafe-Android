package com.android.quizcafe.feature.login

import android.credentials.GetCredentialException
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import com.android.quizcafe.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import kotlinx.coroutines.launch

@Composable
fun LoginRoute(
    navigateToSignUp: () -> Unit,
    navigateToHome: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val credentialManager = remember { CredentialManager.create(context) }
    val state by viewModel.state.collectAsState()


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
                    val googleCredential = try {
                        GoogleIdTokenCredential.createFrom(credential.data)
                    } catch (e: Exception) {
                        Log.e("googleLogin", "파싱 실패: ${e.message}")
                        null
                    }
                    Log.d("googleLogin", "credential class: $googleCredential")
                    if (googleCredential != null) {
                        Log.d("googleLogin", "LoginRoute: ${googleCredential.idToken}")
                        viewModel.sendIntent(LoginIntent.GoogleLogin(googleCredential.idToken))
                    } else {
                        Toast.makeText(context, "idToken 획득 실패", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(context, "Google 로그인 실패: ${e.message}", Toast.LENGTH_SHORT).show()
                    Log.d("googleLogin", "LoginRoute: ${e.message}")
                }
            }
        }
    )
}
