package com.android.quizcafe.feature.quiz.solve.viewmodel

import com.android.quizcafe.core.ui.base.BaseContract

sealed class QuizSolveIntent : BaseContract.ViewIntent {
    data class SelectOption(val option: String) : QuizSolveIntent()
    data class UpdatedSubjectiveAnswer(val answer: String) : QuizSolveIntent()
    data object SubmitAnswer : QuizSolveIntent()
    data object SubmitNext   : QuizSolveIntent()
    data object ShowExplanation : QuizSolveIntent()
    data object OnBackClick : QuizSolveIntent()
    data object TickTime : QuizSolveIntent()
}
