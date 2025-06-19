package com.android.quizcafe.feature.main.mypage

import android.util.Log
import com.android.quizcafe.core.domain.model.Resource
import com.android.quizcafe.core.domain.usecase.auth.LogoutUseCase
import com.android.quizcafe.core.domain.usecase.user.DeleteUserUseCase
import com.android.quizcafe.core.domain.usecase.user.GetUserInfoUseCase
import com.android.quizcafe.core.domain.usecase.user.UpdatePasswordUseCase
import com.android.quizcafe.core.domain.usecase.user.UpdateUserNickNameUseCase
import com.android.quizcafe.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val getUserInfoUseCase: GetUserInfoUseCase,
    private val deleteUserUseCase: DeleteUserUseCase,
    private val updatePasswordUseCase: UpdatePasswordUseCase,
    private val updateUserNickNameUseCase: UpdateUserNickNameUseCase,
    private val logoutUseCase: LogoutUseCase

) : BaseViewModel<MyPageViewState, MyPageIntent, MyPageEffect>(
    initialState = MyPageViewState()
) {
    override suspend fun handleIntent(intent: MyPageIntent) {
        when (intent) {
            is MyPageIntent.LoadUserInfo -> getUserInfo()
            is MyPageIntent.ClickChangeUserInfo -> emitEffect(MyPageEffect.ShowUserInfoDialog)
            is MyPageIntent.ClickMyCreatedQuizBooks -> emitEffect(MyPageEffect.NavigateToMyCreatedQuizBooks)
            is MyPageIntent.FailLoadUserInfo -> emitEffect(MyPageEffect.ShowToast(intent.errorMessage))

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

    override fun reduce(currentState: MyPageViewState, intent: MyPageIntent): MyPageViewState {
        return when (intent) {
            is MyPageIntent.LoadUserInfo -> currentState.copy(isLoading = true, errorMessage = null)
            is MyPageIntent.SuccessLoadUserInfo -> currentState.copy(
                nickname = intent.data.nickname,
                quizCount = intent.data.quizCount,
                quizBookCount = intent.data.quizBookCount,
                quizSolvingRecord = intent.data.quizSolvingRecord,
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
                    emitEffect(MyPageEffect.ShowToast("성공적으로 로그아웃하였습니다."))
                }

                is Resource.Loading -> {
                    Log.d("myPage", "Loading")
                }

                is Resource.Failure -> {
                    Log.d("myPage", "logout Fail")
                    emitEffect(MyPageEffect.ShowToast("로그 아웃에 실패했습니다."))
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
                    emitEffect(MyPageEffect.ShowToast("성공적으로 회원탈퇴하였습니다."))
                }

                is Resource.Loading -> {
                    Log.d("myPage", "Loading")
                }

                is Resource.Failure -> {
                    it.printError()
                    Log.d("myPage", "deleteUser Fail")
                    emitEffect(MyPageEffect.ShowToast("회원 탈퇴에 실패했습니다."))
                }
            }
        }
    }
}
