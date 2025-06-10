package com.android.quizcafe.core.data.remote.service

import com.android.quizcafe.core.data.model.solving.request.QuizBookSolvingRequestDto
import com.android.quizcafe.core.data.model.solving.response.QuizBookSolvingResponse
import com.android.quizcafe.core.network.model.ApiResponse
import com.android.quizcafe.core.network.model.NetworkResult
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface QuizBookSolvingService {

    @GET("/quiz-book-solving")
    suspend fun getAllQuizBookSolvingByUser(): NetworkResult<ApiResponse<List<QuizBookSolvingResponse>>>

    @POST("/quiz-book-solving")
    suspend fun createQuizBookSolving(
        @Body request: QuizBookSolvingRequestDto
    ): NetworkResult<ApiResponse<Long>>
}
