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

    object ClickLogin : LoginIntent()
    object ClickSignUp : LoginIntent()

    object SuccessLogin : LoginIntent()

    data class FailLogin(val errorMessage: String? = null) : LoginIntent()
}

sealed class LoginEffect {
    data class ShowErrorDialog(val message: String) : LoginEffect()
    object NavigateToHome : LoginEffect()
    object NavigateToSignUp : LoginEffect()
}
