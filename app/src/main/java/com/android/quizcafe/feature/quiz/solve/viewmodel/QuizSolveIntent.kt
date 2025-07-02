package com.android.quizcafe.feature.quiz.solve.viewmodel

import com.android.quizcafe.core.domain.model.quiz.QuizGrade
import com.android.quizcafe.core.domain.model.quizbook.response.QuizBook
import com.android.quizcafe.core.domain.model.solving.QuizBookGrade
import com.android.quizcafe.core.domain.model.value.QuizBookGradeLocalId
import com.android.quizcafe.core.domain.model.value.QuizBookGradeServerId
import com.android.quizcafe.core.ui.base.BaseContract

sealed class QuizSolveIntent : BaseContract.ViewIntent {
    // 초기화 작업
    data class Initialize(val quizBookId: Long) : QuizSolveIntent()
    data class LoadQuizBookSuccess(val quizBook: QuizBook) : QuizSolveIntent()
    data class LoadQuizBookGradeSuccess(val quizBookGrade: QuizBookGrade) : QuizSolveIntent()
    data class SetQuizBookLocalId(val quizBookLocalId: QuizBookGradeLocalId) : QuizSolveIntent()

    // 문제 선택
    data class SelectAnswer(val option: QuizOption) : QuizSolveIntent()
    data class UpdateSubjectiveAnswer(val answer: String) : QuizSolveIntent()

    // 네비게이션
    data object NavigateBack : QuizSolveIntent()
    data object NavigateToNextQuestion : QuizSolveIntent()
    data object NavigateToPreviousQuestion : QuizSolveIntent()
    data object NavigateToResult : QuizSolveIntent()

    // 문제 제출, 풀이
    data object GradeQuizSuccess : QuizSolveIntent()
    data class GetQuizGradeSuccess(val quizGrade: QuizGrade) : QuizSolveIntent()
    data class SubmitQuizBookSuccess(val quizBookGradeServerId: QuizBookGradeServerId) : QuizSolveIntent()
    data class HandleError(val message: String?) : QuizSolveIntent()

    // 타이머
    data object UpdateTimer : QuizSolveIntent()
}
