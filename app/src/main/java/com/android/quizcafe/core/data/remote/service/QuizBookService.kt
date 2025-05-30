package com.android.quizcafe.core.data.remote.service

import com.android.quizcafe.core.data.model.quizbook.response.CategoryResponseDto
import com.android.quizcafe.core.network.model.ApiResponse
import com.android.quizcafe.core.network.model.NetworkResult
import retrofit2.http.GET

interface QuizBookService {

    // TODO: 카테고리 요청 api 수정 시 request 추가하기
    @GET("quiz-book/category")
    suspend fun getCategories(
//        @Body request: QuizBookRequest
    ): NetworkResult<ApiResponse<List<CategoryResponseDto>>>
}
