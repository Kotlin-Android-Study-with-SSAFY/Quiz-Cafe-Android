package com.android.quizcafe.feature.quizbooklist

import com.android.quizcafe.core.domain.model.quizbook.response.QuizBook
import com.android.quizcafe.core.ui.base.BaseContract

data class QuizBookListViewState(
    val category: String = "",
    val quizBooks: List<QuizBook> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
) : BaseContract.ViewState

sealed class QuizBookListIntent : BaseContract.ViewIntent {
    data object LoadQuizBooks : QuizBookListIntent()
    data object ClickQuizBookList : QuizBookListIntent()

    data class SuccessGetQuizBooks(val data: List<QuizBook>) : QuizBookListIntent()
    data class FailGetQuizBooks(val errorMessage: String? = null) : QuizBookListIntent()
}

sealed class QuizBookListEffect : BaseContract.ViewEffect {
    data class ShowError(val message: String) : QuizBookListEffect()
    data object NavigateToCategory : QuizBookListEffect()
    data object NavigateToQuizBookDetail : QuizBookListEffect()
}
