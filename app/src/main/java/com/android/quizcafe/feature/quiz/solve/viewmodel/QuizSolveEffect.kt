package com.android.quizcafe.feature.quiz.solve.viewmodel

import com.android.quizcafe.core.domain.model.value.QuizBookGradeServerId
import com.android.quizcafe.core.ui.base.BaseContract

sealed class QuizSolveEffect : BaseContract.ViewEffect {
    data class ShowErrorDialog(val message: String) : QuizSolveEffect()
    data object ShowExitDialog : QuizSolveEffect()
    data object NavigateToBack : QuizSolveEffect()
    data class NavigateToQuizBookSolvingResult(val quizBookGradeServerId: QuizBookGradeServerId) : QuizSolveEffect()
}
