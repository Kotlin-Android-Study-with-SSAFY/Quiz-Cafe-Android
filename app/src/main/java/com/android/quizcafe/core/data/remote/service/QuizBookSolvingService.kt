package com.android.quizcafe.core.data.remote.service

import com.android.quizcafe.core.data.model.solving.request.QuizBookSolvingRequestDto
import com.android.quizcafe.core.data.model.solving.response.QuizBookSolvingResponseDto
import com.android.quizcafe.core.network.model.ApiResponse
import com.android.quizcafe.core.network.model.NetworkResult
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface QuizBookSolvingService {

    @GET("/quiz-book-solving")
    suspend fun getAllQuizBookSolvingByUser(): NetworkResult<ApiResponse<List<QuizBookSolvingResponseDto>>>

    @POST("/quiz-book-solving")
    suspend fun createQuizBookSolving(
        @Body request: QuizBookSolvingRequestDto
    ): NetworkResult<ApiResponse<Long>>

    @GET("/quiz-book-solving")
    suspend fun getQuizBookSolving(
        @Query("id") quizBookSolvingId: Long
    ): NetworkResult<ApiResponse<QuizBookSolvingResponseDto>>
}
