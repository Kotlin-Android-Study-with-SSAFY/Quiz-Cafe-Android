package com.android.quizcafe.core.domain.repository

import com.android.quizcafe.core.data.model.solving.request.QuizBookSolvingRequestDto
import com.android.quizcafe.core.domain.model.Resource
import com.android.quizcafe.core.domain.model.quiz.QuizGrade
import com.android.quizcafe.core.domain.model.solving.QuizBookGrade
import com.android.quizcafe.core.domain.model.value.QuizBookGradeLocalId
import com.android.quizcafe.core.domain.model.value.QuizBookId
import kotlinx.coroutines.flow.Flow

interface QuizBookSolvingRepository{

    fun createEmptyQuizBookGrade(id : QuizBookId) : Flow<Resource<QuizBookGradeLocalId>>

    // 퀴즈북 풀기 기록 가져오기
    fun getQuizBookGrade(id : QuizBookGradeLocalId) : Flow<Resource<QuizBookGrade>>

    // 퀴즈 1개 풀이 기록 저장
    fun upsertQuizGrade(quizGrade: QuizGrade) : Flow<Resource<Unit>>

    // 퀴즈북 풀이 완료 -> 기록 저장 API 호출
    fun solveQuizBook(quizBookGradeLocalId: QuizBookGradeLocalId) : Flow<Resource<Unit>>
}
