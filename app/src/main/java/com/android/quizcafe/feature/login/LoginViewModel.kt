package com.android.quizcafe.feature.login

import android.util.Log
import com.android.quizcafe.core.domain.model.Resource
import com.android.quizcafe.core.domain.model.auth.request.LoginRequest
import com.android.quizcafe.core.domain.usecase.auth.LoginUseCase
import com.android.quizcafe.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
) : BaseViewModel<LoginUiState, LoginIntent, LoginEffect>(
    initialState = LoginUiState()
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

            LoginIntent.SuccessLogin -> {
                emitEffect(LoginEffect.NavigateToHome)
            }

            else -> Unit
        }
    }

    override fun reduce(state: LoginUiState, intent: LoginIntent): LoginUiState {
        return when (intent) {
            is LoginIntent.UpdatedEmail -> state.copy(email = intent.email, errorMessage = null)
                .recalculate()

            is LoginIntent.UpdatedPassword -> state.copy(
                password = intent.password,
                errorMessage = null
            ).recalculate()

            LoginIntent.ClickLogin -> state.copy(isLoading = true, errorMessage = null)
            LoginIntent.ClickSignUp -> state.copy(isLoading = false)

            LoginIntent.SuccessLogin -> state.copy(isLoading = false)

            is LoginIntent.FailLogin -> state.copy(isLoading = false, errorMessage = "로그인에 실패했습니다.")
        }
    }

    private fun LoginUiState.recalculate(): LoginUiState {
        val isLoginEnabled = password.isNotBlank() && email.isNotBlank()
        return this.copy(isLoginEnabled = isLoginEnabled)
    }
}
