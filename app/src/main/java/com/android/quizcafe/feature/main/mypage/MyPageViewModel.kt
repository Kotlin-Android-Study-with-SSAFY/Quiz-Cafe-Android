package com.android.quizcafe.feature.main.mypage

import android.util.Log
import com.android.quizcafe.core.domain.model.Resource
import com.android.quizcafe.core.domain.usecase.user.GetUserInfoUseCase
import com.android.quizcafe.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val getUserInfoUseCase: GetUserInfoUseCase

) : BaseViewModel<MyPageViewState, MyPageIntent, MyPageEffect>(
    initialState = MyPageViewState()
) {
    override suspend fun handleIntent(intent: MyPageIntent) {
        when (intent) {
            is MyPageIntent.LoadUserInfo -> getUserInfo()
            is MyPageIntent.ClickAlarm -> emitEffect(MyPageEffect.NavigateToAlarm)
            is MyPageIntent.ClickChangePw -> emitEffect(MyPageEffect.NavigateToChangePw)
            is MyPageIntent.ClickMyQuizSet -> emitEffect(MyPageEffect.NavigateToMyQuizSet)
            is MyPageIntent.FailLoadUserInfo -> emitEffect(MyPageEffect.ShowError(intent.errorMessage))
            else -> Unit
        }
    }

    override fun reduce(currentState: MyPageViewState, intent: MyPageIntent): MyPageViewState {
        return when (intent) {
            is MyPageIntent.LoadUserInfo -> currentState.copy(isLoading = true, errorMessage = null)
            is MyPageIntent.SuccessLoadUserInfo -> currentState.copy(
                nickname = intent.data.nickname,
                isLoading = false,
                errorMessage = null
            )

            is MyPageIntent.FailLoadUserInfo -> currentState.copy(isLoading = false, errorMessage = intent.errorMessage)
            else -> currentState
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
}
