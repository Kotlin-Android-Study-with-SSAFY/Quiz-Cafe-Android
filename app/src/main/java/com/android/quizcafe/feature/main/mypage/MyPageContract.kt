package com.android.quizcafe.feature.main.mypage

import com.android.quizcafe.core.domain.model.user.UserInfo
import com.android.quizcafe.core.ui.base.BaseContract

data class MyPageViewState(
    val nickname: String = "",
    val solvedCount: Int = 0,
    val myQuizSetCount: Int = 0,
    val quizSolvingRecord: Map<String, Int> = emptyMap(),
    val joinDateStr: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
) : BaseContract.ViewState

sealed class MyPageIntent : BaseContract.ViewIntent {
    data object LoadUserInfo : MyPageIntent()
    data class ClickStats(val solvedCount: Int) : MyPageIntent()
    data object ClickAlarm : MyPageIntent()
    data object ClickChangePw : MyPageIntent()
    data object ClickMyQuizSet : MyPageIntent()

    data class SuccessLoadUserInfo(val data: UserInfo) : MyPageIntent()
    data class FailLoadUserInfo(val errorMessage: String) : MyPageIntent()
}

sealed class MyPageEffect : BaseContract.ViewEffect {
    data object NavigateToAlarm : MyPageEffect()
    data object NavigateToChangePw : MyPageEffect()
    data object NavigateToMyQuizSet : MyPageEffect()
    data class ShowError(val message: String) : MyPageEffect()
}
