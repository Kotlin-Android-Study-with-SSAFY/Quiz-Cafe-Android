package com.android.quizcafe.feature.quizbooklist

import com.android.quizcafe.core.domain.model.quizbook.response.QuizBook
import com.android.quizcafe.core.ui.base.BaseContract

data class QuizBookListViewState(
    val category: String = "",
    val quizBooks: List<QuizBook> = emptyList(),
    val filteredQuizBooks: List<QuizBook> = emptyList(),
    val currentFilters: FilterState = FilterState(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
) : BaseContract.ViewState

sealed class QuizBookListIntent : BaseContract.ViewIntent {
    data object LoadQuizBooks : QuizBookListIntent()
    data class ClickQuizBook(val quizBookId: Long) : QuizBookListIntent()

    data class UpdateCategory(val category: String) : QuizBookListIntent()
    data class UpdateFilterOptions(val filterState: FilterState) : QuizBookListIntent()

    data class SuccessGetQuizBooks(val data: List<QuizBook>) : QuizBookListIntent()
    data class FailGetQuizBooks(val errorMessage: String? = null) : QuizBookListIntent()
}

sealed class QuizBookListEffect : BaseContract.ViewEffect {
    data class ShowError(val message: String) : QuizBookListEffect()
    data object NavigateToCategory : QuizBookListEffect()
    data class NavigateToQuizBookDetail(val quizBookId: Long) : QuizBookListEffect()
}
