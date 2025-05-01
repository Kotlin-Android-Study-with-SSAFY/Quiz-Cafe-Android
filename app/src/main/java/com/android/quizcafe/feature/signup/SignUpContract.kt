package com.android.quizcafe.feature.signup

class SignUpContract {

    data class SignUpViewState(
        val email: String = "",
        val verificationCode: String = "",
        val password: String = "",
        val passwordConfirm: String = "",

        val emailErrorMessage: String? = null,
        val passwordErrorMessage: String? = null,
        val passwordConfirmErrorMessage: String? = null,
        val verificationCodeErrorMessage: String? = null,

        val isCodeSent: Boolean = false,
        val isVerificationCodeValid: Boolean = false,
        val remainingSeconds: Int = 180,

        val isNextEnabled: Boolean = false,
        val isSignUpEnabled: Boolean = false,
        val isLoading: Boolean = false,
        val isSuccessVerification: Boolean = false,

        val errorMessage: String? = null
    )

    sealed class SignUpIntent {
        data class UpdatedEmail(val email: String) : SignUpIntent()
        data class UpdatedVerificationCode(val code: String) : SignUpIntent()
        data class UpdatedPassword(val password: String) : SignUpIntent()
        data class UpdatedPasswordConfirm(val password: String) : SignUpIntent()

        object ClickSendCode : SignUpIntent()
        object ClickVerifyCode : SignUpIntent()
        object ClickSignUp : SignUpIntent()

        object SuccessSendCode : SignUpIntent()
        object SuccessCodeVerification : SignUpIntent()
        object SuccessSignUp : SignUpIntent()

        data class FailSendCode(val errorMessage: String? = null) : SignUpIntent()
        data class FailCodeVerification(val errorMessage: String? = null) : SignUpIntent()
        data class FailSignUp(val errorMessage: String? = null) : SignUpIntent()
    }

    sealed class SignUpEffect {
        data class ShowErrorDialog(val message: String) : SignUpEffect()
        object NavigateToLoginScreen : SignUpEffect()
        object NavigateToPasswordInput : SignUpEffect()
    }
}