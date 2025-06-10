package com.android.quizcafe.feature.main.quiz

import androidx.compose.ui.graphics.Color
import com.android.quizcafe.core.domain.model.quizsolvingrecord.response.QuizSolvingRecord
import com.android.quizcafe.core.ui.base.BaseContract

data class ModeItem(
    val titleResId: Int,
    val descResId: Int,
    val backgroundColor: Color,
    val iconResId: Int
)

data class QuizViewState(
    val quizSolvingRecords: List<QuizSolvingRecord> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
) : BaseContract.ViewState

sealed class QuizIntent : BaseContract.ViewIntent {
    data object FetchRecord : QuizIntent()
    data object LoadingFetchRecord : QuizIntent()
    data class SuccessFetchRecord(val quizSolvingRecords: List<QuizSolvingRecord>) : QuizIntent()
    data class FailFetchRecord(val errorMessage: String? = null) : QuizIntent()

    data class ClickQuizCard(val quizType: String) : QuizIntent()
}

sealed class QuizEffect : BaseContract.ViewEffect {
    data class ShowErrorDialog(val message: String) : QuizEffect()

    data class NavigateToCategory(val quizType: String) : QuizEffect()
}
