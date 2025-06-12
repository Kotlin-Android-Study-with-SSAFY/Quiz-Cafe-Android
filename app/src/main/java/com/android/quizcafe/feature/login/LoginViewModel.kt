package com.android.quizcafe.feature.login

import android.util.Log
import com.android.quizcafe.core.domain.model.Resource
import com.android.quizcafe.core.domain.model.auth.request.LoginRequest
import com.android.quizcafe.core.domain.usecase.auth.GoogleLoginUseCase
import com.android.quizcafe.core.domain.usecase.auth.LoginUseCase
import com.android.quizcafe.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val googleLoginUseCase: GoogleLoginUseCase
) : BaseViewModel<LoginViewState, LoginIntent, LoginEffect>(
    initialState = LoginViewState()
) {

    override suspend fun handleIntent(intent: LoginIntent) {
        when (intent) {
            LoginIntent.ClickSignUp -> {
                emitEffect(LoginEffect.NavigateToSignUp)
            }

            LoginIntent.ClickLogin -> {
                loginUseCase(
                    LoginRequest(
                        email = state.value.email,
                        password = state.value.password
                    )
                ).collect {
                    when (it) {
                        is Resource.Success -> {
                            Log.d("signup", "SendCode Success")
                            sendIntent(LoginIntent.SuccessLogin)
                        }
                        is Resource.Loading -> Log.d("signup", "Loading")
                        is Resource.Failure -> Log.d("signup", "SendCode Fail")
                    }
                }
            }

            is LoginIntent.GoogleLogin -> {
                googleLoginUseCase(intent.idToken).collect { result ->
                    when(result) {
                        is Resource.Success -> {
                            Log.d("googleLogin", "111")
                            sendIntent(LoginIntent.SuccessLogin)
                        }
                        is Resource.Failure -> {
                            Log.d("googleLogin", "handleIntent: ${result.errorMessage}")
                            emitEffect(LoginEffect.ShowErrorDialog("구글 로그인 실패"))
                        }
                        else -> Unit
                    }
                }
            }

            LoginIntent.SuccessLogin -> {
                emitEffect(LoginEffect.NavigateToHome)
            }

            else -> Unit
        }
    }

    override fun reduce(currentState: LoginViewState, intent: LoginIntent): LoginViewState {
        return when (intent) {
            is LoginIntent.UpdatedEmail -> currentState.copy(email = intent.email, errorMessage = null)
                .recalculate()

            is LoginIntent.UpdatedPassword -> currentState.copy(
                password = intent.password,
                errorMessage = null
            ).recalculate()

            LoginIntent.ClickLogin -> currentState.copy(isLoading = true, errorMessage = null)
            LoginIntent.ClickSignUp -> currentState.copy(isLoading = false)

            LoginIntent.SuccessLogin -> currentState.copy(isLoading = false)

            is LoginIntent.GoogleLogin -> currentState
            is LoginIntent.FailLogin -> currentState.copy(isLoading = false, errorMessage = "로그인에 실패했습니다.")
        }
    }

    private fun LoginViewState.recalculate(): LoginViewState {
        val isLoginEnabled = password.isNotBlank() && email.isNotBlank()
        return this.copy(isLoginEnabled = isLoginEnabled)
    }
}
