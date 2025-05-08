package com.android.quizcafe.feature.login

data class LoginViewState(
    val email: String = "",
    val password: String = "",
    val isLoginEnabled: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)

sealed class LoginIntent {
    data class UpdatedEmail(val email: String) : LoginIntent()
    data class UpdatedPassword(val password: String) : LoginIntent()

    data object ClickLogin : LoginIntent()
    data object ClickSignUp : LoginIntent()

    data object SuccessLogin : LoginIntent()

    data class FailLogin(val errorMessage: String? = null) : LoginIntent()
}

sealed class LoginEffect {
    data class ShowErrorDialog(val message: String) : LoginEffect()
    data object NavigateToHome : LoginEffect()
    data object NavigateToSignUp : LoginEffect()
}
