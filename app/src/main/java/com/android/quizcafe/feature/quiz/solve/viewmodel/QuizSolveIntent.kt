package com.android.quizcafe.feature.quiz.solve.viewmodel

sealed class QuizSolveIntent {
    data class SelectOXOption(val option: String): QuizSolveIntent()
    data class UpdatedSubjectiveAnswer(val answer: String) : QuizSolveIntent()
    object SubmitAnswer: QuizSolveIntent()
    object OnBackClick : QuizSolveIntent()
}
