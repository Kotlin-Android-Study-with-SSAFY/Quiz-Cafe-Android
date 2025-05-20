package com.android.quizcafe.feature.main.quiz

import androidx.compose.ui.graphics.Color
import com.android.quizcafe.core.ui.base.BaseContract

data class ModeItem(
    val titleResId: Int,
    val descResId: Int,
    val backgroundColor: Color
)

data class QuizHistory(
    val time: String,
    val title: String,
    val result: Int,
    val totalProblems: Int
)

data class QuizViewState(
    val historyList: List<QuizHistory> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
) : BaseContract.ViewState

sealed class QuizIntent : BaseContract.ViewIntent {
    data object FetchHistory : QuizIntent()
    data object LoadingFetchHistory : QuizIntent()
    data class SuccessFetchHistory(val histories: List<QuizHistory>) : QuizIntent()
    data class FailFetchHistory(val errorMessage: String? = null) : QuizIntent()

    data object ClickOxQuiz : QuizIntent()
    data object ClickMultipleChoiceQuiz : QuizIntent()
    data object ClickShortAnswerQuiz : QuizIntent()
    data object ClickCreateQuiz : QuizIntent()
}


sealed class QuizEffect : BaseContract.ViewEffect {
    data class ShowErrorDialog(val message: String) : QuizEffect()

    data object NavigateToOxQuiz : QuizEffect()
    data object NavigateToMultipleChoiceQuiz : QuizEffect()
    data object NavigateToShortAnswerQuiz : QuizEffect()
    data object NavigateToCreateQuiz : QuizEffect()
}
