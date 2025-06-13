package com.android.quizcafe.feature.quiz.solvingResult

import com.android.quizcafe.core.domain.model.solving.QuizBookSolving
import com.android.quizcafe.core.ui.base.BaseContract

data class QuizBookSolvingResultUiState(
    val isLoading: Boolean = true,
    val quizBookId: String? = null,
    val quizBookSolving: QuizBookSolving? = null,
    val errorMessage: String? = null
) : BaseContract.UiState

sealed class QuizBookSolvingResultIntent : BaseContract.ViewIntent {

    data object StartLoading : QuizBookSolvingResultIntent()

    data class InitWithQuizBookGradeId(val quizBookGradeServerId: Long) : QuizBookSolvingResultIntent()

    data class SuccessLoadData(
        val quizBookSolving: QuizBookSolving,
    ) : QuizBookSolvingResultIntent()

    data object ClickFinish : QuizBookSolvingResultIntent()

    data class Error(val errorMessage: String? = null) : QuizBookSolvingResultIntent()
}

sealed class QuizBookSolvingResultEffect : BaseContract.ViewEffect {
    data class ShowError(val message: String) : QuizBookSolvingResultEffect()
    data object NavigateToMain : QuizBookSolvingResultEffect()
}
