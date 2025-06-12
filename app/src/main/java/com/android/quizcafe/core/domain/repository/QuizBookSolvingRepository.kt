package com.android.quizcafe.core.domain.repository

import com.android.quizcafe.core.domain.model.Resource
import com.android.quizcafe.core.domain.model.quiz.QuizGrade
import com.android.quizcafe.core.domain.model.solving.QuizBookSolving
import com.android.quizcafe.core.domain.model.solving.QuizBookGrade
import com.android.quizcafe.core.domain.model.value.QuizBookGradeLocalId
import com.android.quizcafe.core.domain.model.value.QuizBookGradeServerId
import com.android.quizcafe.core.domain.model.value.QuizBookId
import kotlinx.coroutines.flow.Flow

interface QuizBookSolvingRepository{

    fun createEmptyQuizBookGrade(id : QuizBookId) : Flow<Resource<QuizBookGradeLocalId>>

    // 퀴즈북 풀이 로컬 기록 가져오기
    fun getQuizBookGrade(id : QuizBookGradeLocalId) : Flow<Resource<QuizBookGrade>>

    // 퀴즈북 풀이 완료 기록 가져오기 (서버)
    fun getQuizBookSolving (id : QuizBookGradeServerId) : Flow<Resource<QuizBookSolving>>

    // 사용자의 퀴즈북 풀이 전부 가져오기
    fun getAllQuizBookSolving() : Flow<Resource<List<QuizBookSolving>>>

    // 퀴즈 1개 풀이 기록 저장
    fun upsertQuizGrade(quizGrade: QuizGrade) : Flow<Resource<Unit>>

    // 퀴즈북 풀이 완료 -> 기록 저장 API 호출
    fun solveQuizBook(quizBookGradeLocalId: QuizBookGradeLocalId) : Flow<Resource<QuizBookGradeServerId>>
}
