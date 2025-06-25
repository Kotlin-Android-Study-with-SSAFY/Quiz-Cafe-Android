package com.android.quizcafe.feature.main.updateuserinfo

import com.android.quizcafe.core.domain.model.Resource
import com.android.quizcafe.core.domain.usecase.user.UpdateNicknameUseCase
import com.android.quizcafe.core.domain.usecase.user.UpdatePasswordUseCase
import com.android.quizcafe.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UpdateUserInfoViewModel @Inject constructor(
    private val updateNicknameUseCase: UpdateNicknameUseCase,
    private val updatePasswordUseCase: UpdatePasswordUseCase
) : BaseViewModel<UpdateUserInfoUiState, UpdateUserInfoIntent, UpdateUserInfoEffect>(
    initialState = UpdateUserInfoUiState()
) {
    override suspend fun handleIntent(intent: UpdateUserInfoIntent) {
        when (intent) {
            is UpdateUserInfoIntent.ConfirmNickname -> {
                updateNicknameUseCase(state.value.nickname).collect { result ->
                    when (result) {
                        is Resource.Success -> {
                            emitEffect(UpdateUserInfoEffect.ShowToast("닉네임이 변경되었습니다."))
                            emitEffect(UpdateUserInfoEffect.NavigateBack)
                        }
                        is Resource.Failure -> {
                            emitEffect(UpdateUserInfoEffect.ShowToast(result.errorMessage))
                        }
                        else -> Unit
                    }
                }
            }

            is UpdateUserInfoIntent.ConfirmPassword -> {
                updatePasswordUseCase(
                    oldPassword = state.value.currentPassword,
                    newPassword = state.value.newPassword
                ).collect { result ->
                    when (result) {
                        is Resource.Success -> {
                            emitEffect(UpdateUserInfoEffect.ShowToast("비밀번호가 변경되었습니다."))
                            emitEffect(UpdateUserInfoEffect.NavigateBack)
                        }
                        is Resource.Failure -> {
                            emitEffect(UpdateUserInfoEffect.ShowToast(result.errorMessage))
                        }
                        else -> Unit
                    }
                }
            }

            else -> Unit
        }
    }

    override fun reduce(
        currentState: UpdateUserInfoUiState,
        intent: UpdateUserInfoIntent
    ): UpdateUserInfoUiState {
        return when (intent) {
            is UpdateUserInfoIntent.UpdatedNickname -> {
                currentState.copy(nickname = intent.nickname)
                    .recalculate()
            }

            is UpdateUserInfoIntent.UpdatedCurrentPassword -> {
                currentState.copy(currentPassword = intent.password)
                    .recalculate()
            }

            is UpdateUserInfoIntent.UpdatedNewPassword -> {
                currentState.copy(newPassword = intent.password)
                    .recalculate()
            }

            is UpdateUserInfoIntent.UpdatedConfirmPassword -> {
                currentState.copy(confirmPassword = intent.password)
                    .recalculate()
            }

            else -> currentState
        }
    }

    private fun UpdateUserInfoUiState.recalculate(): UpdateUserInfoUiState {
        val passwordRegex =
            Regex("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#\$%^&*])[A-Za-z\\d!@#\$%^&*]{8,20}$")
        val nicknameRegex = Regex("^[a-zA-Z가-힣]{1,10}$")

        val isCurrentValid = currentPassword.isNotBlank() && currentPassword.matches(passwordRegex)
        val isNewValid = newPassword.isNotBlank() && newPassword.matches(passwordRegex)
        val isConfirmValid = confirmPassword == newPassword && confirmPassword.isNotBlank()
        val isNicknameValid = nickname.isNotBlank() && nickname.matches(nicknameRegex)

        return this.copy(
            currentPasswordError = if (currentPassword.isNotBlank() && !isCurrentValid) "기존 비밀번호를 입력해주세요." else null,
            newPasswordError = if (newPassword.isNotBlank() && !isNewValid) "비밀번호는 8~20자의 영문, 숫자, 특수문자를 포함해야 합니다." else null,
            confirmPasswordError = if (confirmPassword.isNotBlank() && !isConfirmValid) "비밀번호가 일치하지 않습니다." else null,
            nicknameError = if (nickname.isNotBlank() && !isNicknameValid) "닉네임은 한글 또는 영문 10자 이내로 입력하세요." else null,
            isUpdatePasswordEnabled = isCurrentValid && isNewValid && isConfirmValid,
            isUpdateNicknameEnabled = isNicknameValid
        )
    }
}
