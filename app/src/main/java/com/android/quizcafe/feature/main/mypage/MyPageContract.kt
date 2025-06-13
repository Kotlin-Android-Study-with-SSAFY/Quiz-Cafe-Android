package com.android.quizcafe.feature.main.mypage

import com.android.quizcafe.core.domain.model.user.response.UserInfo
import com.android.quizcafe.core.ui.base.BaseContract

data class MyPageViewState(
    val nickname: String = "",
    val email: String = "",
    val quizCount: Int = 0,
    val quizBookCount: Int = 0,
    val quizSolvingRecord: Map<String, Int> = emptyMap(),
    val joinDateStr: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null
) : BaseContract.ViewState

sealed class MyPageIntent : BaseContract.ViewIntent {
    data object LoadUserInfo : MyPageIntent()
    data class SuccessLoadUserInfo(val data: UserInfo) : MyPageIntent()
    data class FailLoadUserInfo(val errorMessage: String) : MyPageIntent()

    data object ClickChangeUserInfo : MyPageIntent()
    data object ClickMyCreatedQuizBooks : MyPageIntent()

    data object ClickLogout : MyPageIntent()
    data object ClickWithdrawal : MyPageIntent()

    data object RequestLogout : MyPageIntent()
    data object RequestWithdrawal : MyPageIntent()
    data object ConfirmLogout : MyPageIntent()
    data object CancelLogout : MyPageIntent()
    data object ConfirmWithdrawalFirst : MyPageIntent()
    data object CancelWithdrawalFirst : MyPageIntent()
    data object ConfirmWithdrawalFinal : MyPageIntent()
    data object CancelWithdrawalFinal : MyPageIntent()
}

sealed class MyPageEffect : BaseContract.ViewEffect {
    data object NavigateToChangeUserInfo : MyPageEffect()
    data object NavigateToMyCreatedQuizBooks : MyPageEffect()
    data object NavigateToLogout : MyPageEffect()
    data object NavigateToWithdrawal : MyPageEffect()

    data object ShowLogoutDialog : MyPageEffect()
    data object ShowWithdrawalFirstDialog : MyPageEffect()
    data object ShowWithdrawalFinalDialog : MyPageEffect()

    data class ShowError(val message: String) : MyPageEffect()
}
