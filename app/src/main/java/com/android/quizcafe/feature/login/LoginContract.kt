package com.android.quizcafe.feature.login

import com.android.quizcafe.core.ui.base.Contract

data class LoginViewState(
    val email: String = "",
    val password: String = "",
    val isLoginEnabled: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
): Contract.ViewState

sealed class LoginIntent: Contract.ViewIntent {
    data class UpdatedEmail(val email: String) : LoginIntent()
    data class UpdatedPassword(val password: String) : LoginIntent()

    data object ClickLogin : LoginIntent()
    data object ClickSignUp : LoginIntent()

    data object SuccessLogin : LoginIntent()

    data class FailLogin(val errorMessage: String? = null) : LoginIntent()
}

sealed class LoginEffect: Contract.ViewEffect {
    data class ShowErrorDialog(val message: String) : LoginEffect()
    data object NavigateToHome : LoginEffect()
    data object NavigateToSignUp : LoginEffect()
}
