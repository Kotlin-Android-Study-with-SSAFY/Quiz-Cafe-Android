package com.android.quizcafe.core.domain.usecase.quizsolvingrecord

import com.android.quizcafe.core.domain.model.Resource
import com.android.quizcafe.core.domain.model.quizsolvingrecord.response.QuizSolvingRecord
import com.android.quizcafe.core.domain.repository.QuizSolvingRecordRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetQuizRecordUseCase @Inject constructor(
    private val quizSolvingRecordRepository: QuizSolvingRecordRepository
) {
    // TODO : 서버 배포되면 아래 코드로 UseCase 연결
    // = quizSolvingRecordRepository.getQuizSolvingRecords()
    operator fun invoke(): Flow<Resource<List<QuizSolvingRecord>>> = flow {
        // mock data 생성
        val mockList = listOf(
            QuizSolvingRecord(
                id = 1,
                userId = 1,
                quizBookId = 1,
                version = 1,
                level = "EASY",
                category = "운영체제",
                title = "성준이의 운영체제",
                description = "운영체제 기초 퀴즈",
                totalQuizzes = 20,
                correctCount = 16,
                completedAt = "2025-06-09T05:14:05.986Z",
                quizzes = emptyList()
            ),
            QuizSolvingRecord(
                id = 2,
                userId = 2,
                quizBookId = 2,
                version = 1,
                level = "MEDIUM",
                category = "네트워크",
                title = "성민이의 네트워크",
                description = "네트워크 개념 퀴즈",
                totalQuizzes = 20,
                correctCount = 18,
                completedAt = "2025-06-08T16:00:00.000Z",
                quizzes = emptyList()
            ),
            QuizSolvingRecord(
                id = 3,
                userId = 3,
                quizBookId = 3,
                version = 1,
                level = "HARD",
                category = "안드로이드",
                title = "재용이의 안드로이드",
                description = "안드로이드 중급 퀴즈",
                totalQuizzes = 20,
                correctCount = 19,
                completedAt = "2025-06-07T09:30:00.000Z",
                quizzes = emptyList()
            )
        )
        emit(Resource.Success(mockList))
    }
}
