package com.android.quizcafe.feature.login


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
class LoginViewModel @Inject constructor(): ViewModel() {

    private val _state = MutableStateFlow(LoginViewState())
    val state: StateFlow<LoginViewState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<LoginEffect>()
    val effect: SharedFlow<LoginEffect> = _effect.asSharedFlow()

    fun onIntent(intent: LoginIntent) {
        _state.value = reduce(_state.value, intent)

        when(intent) {
            LoginIntent.ClickSignUp -> {
                viewModelScope.launch {
                    _effect.emit(LoginEffect.NavigateToSignUp)
                }
            }

            LoginIntent.ClickLogin -> {
                viewModelScope.launch {
                    // TODO: 로그인 요청
                    onIntent(LoginIntent.SuccessLogin)
//                    onIntent(LoginIntent.FailLogin)
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
            is LoginIntent.UpdatedEmail -> state.copy(email = intent.email, errorMessage = null).recalculate()
            is LoginIntent.UpdatedPassword -> state.copy(password = intent.password, errorMessage = null).recalculate()

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
