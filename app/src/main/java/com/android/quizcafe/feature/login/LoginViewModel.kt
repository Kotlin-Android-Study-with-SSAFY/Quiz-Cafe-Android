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
                            Log.d("login", "login Success")
                            sendIntent(LoginIntent.SuccessLogin)
                        }

                        is Resource.Loading -> Log.d("login", "login Loading")
                        is Resource.Failure -> {
                            Log.d("login", "login Fail ${it.errorMessage}")
                            sendIntent(
                                when (it.code) {
                                    400 -> {
                                        LoginIntent.FailLogin("입력하신 정보가 올바르지 않습니다.", ErrorTargetField.PASSWORD)
                                    }

                                    404 -> {
                                        LoginIntent.FailLogin("존재하지 않는 이메일 입니다.", ErrorTargetField.EMAIL)
                                    }

                                    401 -> {
                                        LoginIntent.FailLogin("비밀번호가 올바르지 않습니다.", ErrorTargetField.PASSWORD)
                                    }

                                    else -> {
                                        LoginIntent.FailLogin("다시 시도해 주세요.", ErrorTargetField.PASSWORD)
                                    }
                                }
                            )
                        }
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
            is LoginIntent.UpdatedEmail -> currentState.copy(
                email = intent.email,
                emailErrorMessage = null,
                passwordErrorMessage = null
            )
            is LoginIntent.UpdatedPassword -> currentState.copy(
                password = intent.password,
                passwordErrorMessage = null
            )

            LoginIntent.ClickLogin -> currentState.copy(isLoading = true)
            LoginIntent.ClickSignUp -> currentState.copy(isLoading = false)

            LoginIntent.SuccessLogin -> currentState.copy(isLoading = false)

            is LoginIntent.FailLogin -> currentState.copy(
                isLoading = false,
                emailErrorMessage = if (intent.targetField == ErrorTargetField.EMAIL) intent.errorMessage else null,
                passwordErrorMessage = if (intent.targetField == ErrorTargetField.PASSWORD) intent.errorMessage else null
            )
        }
    }
}
