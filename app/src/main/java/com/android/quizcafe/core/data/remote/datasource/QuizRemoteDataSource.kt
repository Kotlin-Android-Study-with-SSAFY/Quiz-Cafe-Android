package com.android.quizcafe.core.data.remote.datasource

import com.android.quizcafe.core.data.model.quiz.QuizResponseDto
import com.android.quizcafe.core.data.remote.service.QuizService
import com.android.quizcafe.core.network.model.ApiResponse
import com.android.quizcafe.core.network.model.NetworkResult
import javax.inject.Inject

class QuizRemoteDataSource @Inject constructor(
    private val quizService: QuizService
) {
    suspend fun getQuizListByBookId(quizBookId: Long): NetworkResult<ApiResponse<List<QuizResponseDto>>> =
        quizService.getQuizListByBookId(quizBookId)
}
