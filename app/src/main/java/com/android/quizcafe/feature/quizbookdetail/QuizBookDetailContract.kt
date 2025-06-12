package com.android.quizcafe.feature.quizbookdetail

import com.android.quizcafe.core.domain.model.quizbook.response.QuizBookDetail
import com.android.quizcafe.core.ui.base.BaseContract

data class QuizBookDetailUiState(
    val quizBookId: Long = 2,
    val quizBookDetail: QuizBookDetail = QuizBookDetail(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
) : BaseContract.UiState

sealed class QuizBookDetailIntent : BaseContract.ViewIntent {
    data object LoadQuizBookDetail : QuizBookDetailIntent()
    data object ClickQuizSolve : QuizBookDetailIntent()
    data object ClickMarkQuizBook : QuizBookDetailIntent()
    data object ClickUnmarkQuizBook : QuizBookDetailIntent()
    data object ClickUser : QuizBookDetailIntent()

    data class UpdateQuizBookId(val quizBookId: Long) : QuizBookDetailIntent()

    data class SuccessGetQuizBookDetail(val data: QuizBookDetail) : QuizBookDetailIntent()
    data object SuccessMarkQuizBook : QuizBookDetailIntent()
    data object SuccessUnmarkQuizBook : QuizBookDetailIntent()
    data class FailGetQuizBookDetail(val errorMessage: String? = null) : QuizBookDetailIntent()
    data class FailUpdateSaveState(val errorMessage: String? = null) : QuizBookDetailIntent()
}

sealed class QuizBookDetailEffect : BaseContract.ViewEffect {
    data class ShowError(val message: String) : QuizBookDetailEffect()
    data object NavigateToQuizBookList : QuizBookDetailEffect()
    data object NavigateToQuizSolve : QuizBookDetailEffect()
    data object NavigateToUserPage : QuizBookDetailEffect()
}
