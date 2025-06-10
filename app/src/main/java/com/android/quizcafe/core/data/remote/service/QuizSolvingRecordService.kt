package com.android.quizcafe.core.data.remote.service

import com.android.quizcafe.core.data.model.quizsolvingrecord.response.QuizSolvingRecordResponseDto
import com.android.quizcafe.core.network.model.ApiResponse
import com.android.quizcafe.core.network.model.NetworkResult
import retrofit2.http.GET

interface QuizSolvingRecordService {

    @GET("quiz-book-solving")
    suspend fun getQuizSolvingRecords(): NetworkResult<ApiResponse<List<QuizSolvingRecordResponseDto>>>
}
