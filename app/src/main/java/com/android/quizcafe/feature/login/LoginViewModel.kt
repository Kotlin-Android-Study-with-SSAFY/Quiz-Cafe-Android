package com.android.quizcafe.feature.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.quizcafe.core.domain.model.Resource
import com.android.quizcafe.core.domain.model.auth.request.LoginRequest
import com.android.quizcafe.core.domain.usecase.auth.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(LoginViewState())
    val state: StateFlow<LoginViewState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<LoginEffect>()
    val effect: SharedFlow<LoginEffect> = _effect.asSharedFlow()

    fun onIntent(intent: LoginIntent) {
        _state.value = reduce(_state.value, intent)

        when (intent) {
            LoginIntent.ClickSignUp -> {
                viewModelScope.launch {
                    _effect.emit(LoginEffect.NavigateToSignUp)
                }
            }

            LoginIntent.ClickLogin -> {
                viewModelScope.launch {
                    loginUseCase(
                        LoginRequest(
                            email = state.value.email,
                            password = state.value.password
                        )
                    ).collect {
                        when (it) {
                            is Resource.Success -> {
                                Log.d("signup", "SendCode Success")
                                onIntent(LoginIntent.SuccessLogin)
                            }
                            is Resource.Loading -> Log.d("signup", "Loading")
                            is Resource.Failure -> Log.d("signup", "SendCode Fail")
                        }
                    }
                }
            }

            LoginIntent.SuccessLogin -> {
                viewModelScope.launch {
                    _effect.emit(LoginEffect.NavigateToHome)
                }
            }

            else -> Unit
        }
    }

    private fun reduce(state: LoginViewState, intent: LoginIntent): LoginViewState {
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

    private fun LoginViewState.recalculate(): LoginViewState {
        val isLoginEnabled = password.isNotBlank() && email.isNotBlank()
        return this.copy(isLoginEnabled = isLoginEnabled)
    }
}
