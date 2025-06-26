package com.android.quizcafe.feature.main.mypage

import android.util.Log
import com.android.quizcafe.core.domain.model.Resource
import com.android.quizcafe.core.domain.usecase.auth.LogoutUseCase
import com.android.quizcafe.core.domain.usecase.user.DeleteUserUseCase
import com.android.quizcafe.core.domain.usecase.user.GetUserInfoUseCase
import com.android.quizcafe.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val getUserInfoUseCase: GetUserInfoUseCase,
    private val deleteUserUseCase: DeleteUserUseCase,
    private val logoutUseCase: LogoutUseCase
) : BaseViewModel<MyPageUiState, MyPageIntent, MyPageEffect>(
    initialState = MyPageUiState()
) {
    override suspend fun handleIntent(intent: MyPageIntent) {
        when (intent) {
            is MyPageIntent.LoadUserInfo -> getUserInfo()
            is MyPageIntent.ClickUpdateUserInfo -> emitEffect(MyPageEffect.ShowUserInfoDialog)
            is MyPageIntent.ClickUpdateNickname -> emitEffect(MyPageEffect.NavigateToUpdateUserInfo(0))
            is MyPageIntent.ClickUpdatePassword -> emitEffect(MyPageEffect.NavigateToUpdateUserInfo(1))

            is MyPageIntent.ClickMyCreatedQuizBooks -> emitEffect(MyPageEffect.NavigateToMyCreatedQuizBooks)
            is MyPageIntent.FailLoadUserInfo -> emitEffect(MyPageEffect.ShowError(intent.errorMessage))

            is MyPageIntent.ClickLogout -> emitEffect(MyPageEffect.ShowLogoutDialog)
            is MyPageIntent.ConfirmLogout -> logout()

            is MyPageIntent.CancelLogout -> Unit

            is MyPageIntent.ClickWithdrawal -> emitEffect(MyPageEffect.ShowWithdrawalFirstDialog)
            is MyPageIntent.ConfirmWithdrawalFirst -> emitEffect(MyPageEffect.ShowWithdrawalFinalDialog)
            is MyPageIntent.ConfirmWithdrawalFinal -> deleteUser()

            is MyPageIntent.CancelWithdrawalFirst,
            is MyPageIntent.CancelWithdrawalFinal -> Unit

            else -> Unit
        }
    }

    override fun reduce(currentState: MyPageUiState, intent: MyPageIntent): MyPageUiState {
        return when (intent) {
            is MyPageIntent.LoadUserInfo -> currentState.copy(isLoading = true, errorMessage = null)
            is MyPageIntent.SuccessLoadUserInfo -> currentState.copy(
                nickname = intent.data.nickname,
                quizCount = intent.data.quizCount,
                quizBookCount = intent.data.quizBookCount,
                quizSolvings = intent.data.quizCountByDate,
                joinDateStr = intent.data.joinDateStr,
                isLoading = false,
                errorMessage = null
            )

            is MyPageIntent.FailLoadUserInfo -> currentState.copy(
                isLoading = false,
                errorMessage = intent.errorMessage
            )

            else -> currentState
        }
    }

    private suspend fun MyPageViewModel.logout() {
        logoutUseCase().collect {
            when (it) {
                is Resource.Success -> {
                    Log.d("myPage", "logout Success")
                }

                is Resource.Loading -> {
                    Log.d("myPage", "Loading")
                }

                is Resource.Failure -> {
                    Log.d("myPage", "logout Fail")
                    emitEffect(MyPageEffect.ShowError("로그 아웃에 실패했습니다."))
                }
            }
        }
    }

    private suspend fun MyPageViewModel.getUserInfo() {
        getUserInfoUseCase().collect {
            when (it) {
                is Resource.Success -> {
                    Log.d("myPage", "Update UserInfo Success")
                    sendIntent(MyPageIntent.SuccessLoadUserInfo(it.data))
                }

                is Resource.Loading -> {
                    Log.d("myPage", "Loading")
                }

                is Resource.Failure -> {
                    Log.d("myPage", "Update UserInfo Fail")
                    sendIntent(MyPageIntent.FailLoadUserInfo(it.errorMessage))
                }
            }
        }
    }

    private suspend fun MyPageViewModel.deleteUser() {
        deleteUserUseCase().collect {
            when (it) {
                is Resource.Success -> {
                    Log.d("myPage", "deleteUser Success")
                }

                is Resource.Loading -> {
                    Log.d("myPage", "Loading")
                }

                is Resource.Failure -> {
                    it.printError()
                    Log.d("myPage", "deleteUser Fail")
                    emitEffect(MyPageEffect.ShowError("회원 탈퇴에 실패했습니다."))
                }
            }
        }
    }
}
