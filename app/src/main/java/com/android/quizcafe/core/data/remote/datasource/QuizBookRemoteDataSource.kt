package com.android.quizcafe.core.data.remote.datasource

import com.android.quizcafe.core.data.model.quizbook.request.CategoryRequestDto
import com.android.quizcafe.core.data.model.quizbook.request.QuizBookRequestDto
import com.android.quizcafe.core.data.model.quizbook.response.CategoryResponseDto
import com.android.quizcafe.core.data.model.quizbook.response.QuizBookResponseDto
import com.android.quizcafe.core.data.remote.service.QuizBookService
import com.android.quizcafe.core.network.model.ApiResponse
import com.android.quizcafe.core.network.model.NetworkResult
import javax.inject.Inject

class QuizBookRemoteDataSource @Inject constructor(
    private val quizBookService: QuizBookService
) {
    // TODO: 카테고리 요청 api 수정 시 request 추가하기
    suspend fun getCategoriesByType(request: CategoryRequestDto): NetworkResult<ApiResponse<List<CategoryResponseDto>>> =
        quizBookService.getCategories()

    suspend fun getQuizBooksByCategory(request: QuizBookRequestDto): NetworkResult<ApiResponse<List<QuizBookResponseDto>>> =
        quizBookService.getQuizBooks(request.category)
}
