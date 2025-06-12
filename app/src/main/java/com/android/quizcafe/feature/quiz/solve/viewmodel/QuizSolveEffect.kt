package com.android.quizcafe.feature.quiz.solve.viewmodel

import com.android.quizcafe.core.ui.base.BaseContract

sealed class QuizSolveEffect : BaseContract.ViewEffect {
    data class ShowErrorDialog(val message: String) : QuizSolveEffect()
    data object NavigatePopBack : QuizSolveEffect()
}
