package com.android.quizcafe.feature.quiz.solve.viewmodel

import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface IQuizSolveViewModel {
    val state: StateFlow<QuizSolveUiState>
    val effect: SharedFlow<QuizSolveEffect>
    fun sendIntent(intent: QuizSolveIntent)
}
