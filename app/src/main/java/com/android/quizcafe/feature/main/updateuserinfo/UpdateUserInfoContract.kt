package com.android.quizcafe.feature.main.updateuserinfo

import com.android.quizcafe.core.ui.base.BaseContract

data class UpdateUserInfoUiState(
    val nickname: String = "",
    val currentPassword: String = "",
    val newPassword: String = "",
    val confirmPassword: String = "",
    val nicknameError: String? = null,
    val currentPasswordError: String? = null,
    val newPasswordError: String? = null,
    val confirmPasswordError: String? = null,
    val isLoading: Boolean = false,
    val isUpdateNicknameEnabled: Boolean = false,
    val isUpdatePasswordEnabled: Boolean = false
) : BaseContract.UiState

sealed class UpdateUserInfoIntent : BaseContract.ViewIntent {
    data class UpdatedNickname(val nickname: String) : UpdateUserInfoIntent()
    data object ConfirmNickname : UpdateUserInfoIntent()

    data class UpdatedCurrentPassword(val password: String) : UpdateUserInfoIntent()
    data class UpdatedNewPassword(val password: String) : UpdateUserInfoIntent()
    data class UpdatedConfirmPassword(val password: String) : UpdateUserInfoIntent()
    data object ConfirmPassword : UpdateUserInfoIntent()
}

sealed class UpdateUserInfoEffect : BaseContract.ViewEffect {
    data class ShowToast(val message: String) : UpdateUserInfoEffect()
    data object NavigateBack : UpdateUserInfoEffect()
}
