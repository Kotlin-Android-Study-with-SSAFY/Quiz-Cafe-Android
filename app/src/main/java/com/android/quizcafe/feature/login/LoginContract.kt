package com.android.quizcafe.feature.login

import com.android.quizcafe.core.ui.base.BaseContract

data class LoginViewState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val emailErrorMessage: String? = null,
    val passwordErrorMessage: String? = null,
) : BaseContract.ViewState {
    val isLoginEnabled get() = password.isNotBlank() && email.isNotBlank() && emailErrorMessage == null && passwordErrorMessage == null
}

sealed class LoginIntent : BaseContract.ViewIntent {
    data class UpdatedEmail(val email: String) : LoginIntent()
    data class UpdatedPassword(val password: String) : LoginIntent()

    data object ClickLogin : LoginIntent()
    data object ClickSignUp : LoginIntent()

    data object SuccessLogin : LoginIntent()

    data class FailLogin(val errorMessage: String? = null, val targetField: ErrorTargetField) : LoginIntent()
}

sealed class LoginEffect : BaseContract.ViewEffect {
    data class ShowErrorDialog(val message: String) : LoginEffect()
    data object NavigateToHome : LoginEffect()
    data object NavigateToSignUp : LoginEffect()
}

enum class ErrorTargetField {
    EMAIL,
    PASSWORD,
    GENERAL
}
