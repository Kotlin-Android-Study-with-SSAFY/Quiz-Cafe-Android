package com.android.quizcafe.feature.main.quiz

import androidx.compose.ui.graphics.Color

data class FeatureItem(
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
    val historyList: List<QuizHistory> = emptyList()
)

sealed class QuizIntent {
    data class LoadHistory(val histories: List<QuizHistory>) : QuizIntent()
}

sealed class QuizEffect {
    data class ShowErrorDialog(val message: String) : QuizEffect()
}
