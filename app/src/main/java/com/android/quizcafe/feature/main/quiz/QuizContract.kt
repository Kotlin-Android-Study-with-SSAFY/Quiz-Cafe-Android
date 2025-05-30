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

// TODO: viewState, Intent, Effect 전부다 재설정 필요.
data class QuizViewState(
    val historyList: List<QuizHistory> = emptyList()
) : BaseContract.ViewState

sealed class QuizIntent : BaseContract.ViewIntent {
    data class LoadHistory(val histories: List<QuizHistory>) : QuizIntent()
}

sealed class QuizEffect : BaseContract.ViewEffect {
    data class ShowErrorDialog(val message: String) : QuizEffect()
}
