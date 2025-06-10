package com.android.quizcafe.core.data.remote.datasource

import com.android.quizcafe.core.data.model.solving.request.QuizBookSolvingRequestDto
import com.android.quizcafe.core.data.model.solving.response.QuizBookSolvingResponse
import com.android.quizcafe.core.data.remote.service.QuizBookService
import com.android.quizcafe.core.data.remote.service.QuizBookSolvingService
import com.android.quizcafe.core.network.model.ApiResponse
import com.android.quizcafe.core.network.model.NetworkResult
import javax.inject.Inject

class QuizBookSolvingRemoteDataSource @Inject constructor(
    private val quizBookSolvingService: QuizBookSolvingService
) {

    suspend fun getAllQuizBookSolvingByUser(): NetworkResult<ApiResponse<List<QuizBookSolvingResponse>>> =
        quizBookSolvingService.getAllQuizBookSolvingByUser()

    suspend fun solveQuizBook(
        request: QuizBookSolvingRequestDto
    ): NetworkResult<ApiResponse<Long>> =
        quizBookSolvingService.createQuizBookSolving(request)
}
