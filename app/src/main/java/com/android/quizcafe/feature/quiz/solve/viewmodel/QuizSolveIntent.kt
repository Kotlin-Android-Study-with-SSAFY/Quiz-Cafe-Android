package com.android.quizcafe.feature.quiz.solve.viewmodel

import com.android.quizcafe.core.domain.model.quiz.Quiz
import com.android.quizcafe.core.domain.model.solving.QuizBookGrade
import com.android.quizcafe.core.domain.model.value.QuizBookGradeLocalId
import com.android.quizcafe.core.ui.base.BaseContract

sealed class QuizSolveIntent : BaseContract.ViewIntent {
    data class LoadQuizBookDetail(val quizBookId: Long) : QuizSolveIntent()
    data class SuccessGetQuizBookDetail(val data: List<Quiz>) : QuizSolveIntent()
    data class SuccessGetQuizBookLocalId(val quizBookLocalId: QuizBookGradeLocalId) : QuizSolveIntent()
    data class SuccessGetQuizBookGradeResult(val quizBookGrade: QuizBookGrade) : QuizSolveIntent()
    data class GetQuizBookLocalId(val quizBookId: Long) : QuizSolveIntent()

    data class GetQuizBookGradeResult(val quizBookLocalId: QuizBookGradeLocalId) : QuizSolveIntent()

    data class SelectOption(val option: Long) : QuizSolveIntent()
    data class UpdatedSubjectiveAnswer(val answer: String) : QuizSolveIntent()
    data object SubmitAnswer : QuizSolveIntent()
    data object SubmitNext : QuizSolveIntent()
    data object ShowExplanation : QuizSolveIntent()

    data object OnBackClick : QuizSolveIntent()
    data object TickTime : QuizSolveIntent()
}
