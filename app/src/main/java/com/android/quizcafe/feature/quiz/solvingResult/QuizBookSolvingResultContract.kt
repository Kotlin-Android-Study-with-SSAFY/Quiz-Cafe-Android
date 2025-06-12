package com.android.quizcafe.feature.quiz.solvingResult

import com.android.quizcafe.core.domain.model.quiz.Quiz
import com.android.quizcafe.core.domain.model.solving.QuizBookGrade
import com.android.quizcafe.core.ui.base.BaseContract


data class QuizBookSolvingResultUiState(
    val isLoading: Boolean = true,
    val quizBookId: String? = null,
    val quizBookSolvingResult: QuizBookGrade? = null,
    val quizzes: List<Quiz> = emptyList(),
    val errorMessage: String? = null
) : BaseContract.UiState

sealed class QuizBookSolvingResultIntent : BaseContract.ViewIntent {

    data object StartLoading : QuizBookSolvingResultIntent()

    data class InitWithQuizBookGradeId(val quizBookGradeLocalId: Long) : QuizBookSolvingResultIntent()

    data class SuccessLoadData(
        val quizBookGrade: QuizBookGrade,
        val quizzes: List<Quiz>
    ) : QuizBookSolvingResultIntent()

    data object ClickFinish : QuizBookSolvingResultIntent()

    data class Error(val errorMessage : String? = null) : QuizBookSolvingResultIntent()
}


sealed class QuizBookSolvingResultEffect : BaseContract.ViewEffect {
    data class ShowError(val message: String) : QuizBookSolvingResultEffect()
    data object NavigateToMain : QuizBookSolvingResultEffect()
}
