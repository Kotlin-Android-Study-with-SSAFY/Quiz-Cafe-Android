package com.android.quizcafe.core.data.remote.service

import com.android.quizcafe.core.data.model.quiz.QuizDto
import com.android.quizcafe.core.network.model.ApiResponse
import com.android.quizcafe.core.network.model.NetworkResult
import retrofit2.http.GET
import retrofit2.http.Query

interface QuizService {

    @GET("quiz")
    suspend fun getQuizListByBookId(
        @Query("quizBookId")quizBookId: Long
    ): NetworkResult<ApiResponse<List<QuizDto>>>
}
