package com.android.quizcafe.feature.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.quizcafe.feature.util.CountdownTimer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow(SignUpViewState())
    val state: StateFlow<SignUpViewState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<SignUpEffect>()
    val effect: SharedFlow<SignUpEffect> = _effect.asSharedFlow()

    private val countdownTimer = CountdownTimer(
        coroutineScope = viewModelScope,
        seconds = 180,
        onTick = { remaining ->
            _state.update { it.copy(remainingSeconds = remaining) }
        }
    )

    fun onIntent(intent: SignUpIntent) {
        _state.value = reduce(_state.value, intent)

        when (intent) {
            SignUpIntent.ClickSignUp -> {
                viewModelScope.launch {
                    // TODO: 회원 가입
                    onIntent(SignUpIntent.SuccessSignUp)
//                    onIntent(SignUpIntent.FailSignUp())
                }
            }

            SignUpIntent.ClickVerifyCode -> {
                viewModelScope.launch {
                    // TODO: 이메일 인증
                    onIntent(SignUpIntent.SuccessCodeVerification)
//                    onIntent(SignUpIntent.FailCodeVerification())
                }
            }

            SignUpIntent.ClickSendCode -> {
                viewModelScope.launch {
                    // TODO: 이메일 인증 코드 요청
                    onIntent(SignUpIntent.SuccessSendCode)
//                    onIntent(SignUpIntent.FailSendCode())
                }
            }

            SignUpIntent.SuccessCodeVerification -> {
                viewModelScope.launch {
                    _effect.emit(SignUpEffect.NavigateToPasswordInput)
                }
            }

            SignUpIntent.SuccessSignUp -> {
                viewModelScope.launch {
                    _effect.emit(SignUpEffect.NavigateToLoginScreen)
                }
            }

            else -> Unit
        }
    }

    private fun reduce(state: SignUpViewState, intent: SignUpIntent): SignUpViewState {
        return when (intent) {
            is SignUpIntent.UpdatedEmail -> state.copy(email = intent.email).recalculate()
            is SignUpIntent.UpdatedVerificationCode -> state.copy(
                verificationCode = intent.code.take(
                    6
                )
            ).recalculate()

            is SignUpIntent.UpdatedPassword -> state.copy(password = intent.password).recalculate()
            is SignUpIntent.UpdatedPasswordConfirm -> state.copy(passwordConfirm = intent.password)
                .recalculate()

            SignUpIntent.ClickVerifyCode -> state.copy(
                isLoading = true,
                isCodeSent = true,
                verificationCodeErrorMessage = null
            )

            SignUpIntent.ClickSignUp -> state.copy(isLoading = true, errorMessage = null)
            SignUpIntent.ClickSendCode -> state.copy(
                isCodeSent = true,
                remainingSeconds = 180,
                isNextEnabled = false,
                errorMessage = null
            )

            is SignUpIntent.FailCodeVerification -> state.copy(
                isLoading = false,
                verificationCodeErrorMessage = "코드가 올바르지 않습니다."
            )

            is SignUpIntent.FailSendCode -> state.copy(
                isLoading = false,
                errorMessage = "오류가 발생했습니다\n다시 시도해 주세요."
            )

            is SignUpIntent.FailSignUp -> state.copy(
                isLoading = false,
                errorMessage = "오류가 발생했습니다\n다시 시도해 주세요."
            )

            SignUpIntent.SuccessCodeVerification -> state.copy(
                isLoading = false,
                isSuccessVerification = true
            )

            SignUpIntent.SuccessSignUp -> state.copy(isLoading = false)
            SignUpIntent.SuccessSendCode -> {
                countdownTimer.start()
                state.copy(isLoading = false, isCodeSent = true)
            }


        }
    }

    private fun SignUpViewState.recalculate(): SignUpViewState {
        val emailRegex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$")
        val passwordRegex =
            Regex("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#\$%^&*])[A-Za-z\\d!@#\$%^&*]{8,20}$")

        val isEmailValid = email.isNotBlank() && email.matches(emailRegex)
        val isCodeValid = verificationCode.length == 6
        val isPasswordValid = password.isNotBlank() && password.matches(passwordRegex)
        val isPasswordConfirmed = passwordConfirm.isNotBlank() && password == passwordConfirm

        return this.copy(
            isNextEnabled = if (isCodeSent) isEmailValid && isCodeValid else isEmailValid,
            isSignUpEnabled = isPasswordValid && isPasswordConfirmed,
            emailErrorMessage = if (!isEmailValid) "이메일 형식이 올바르지 않습니다." else null,
            passwordErrorMessage = if (password.isNotBlank() && !isPasswordValid) "비밀번호는 8~20자의 영문, 숫자, 특수문자를 포함해야 합니다." else null,
            passwordConfirmErrorMessage = if (passwordConfirm.isNotBlank() && !isPasswordConfirmed) "비밀번호가 일치하지 않습니다." else null
        )
    }

    override fun onCleared() {
        super.onCleared()
        countdownTimer.cancel()
    }
}
