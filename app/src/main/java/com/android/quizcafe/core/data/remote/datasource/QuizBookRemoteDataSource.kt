package com.android.quizcafe.core.data.remote.datasource

import com.android.quizcafe.core.data.model.quizbook.request.CategoryRequestDto
import com.android.quizcafe.core.data.model.quizbook.request.QuizBookDetailRequestDto
import com.android.quizcafe.core.data.model.quizbook.response.QuizBookCategoryResponseDto
import com.android.quizcafe.core.data.model.quizbook.response.QuizBookDetailResponseDto
import com.android.quizcafe.core.data.model.quizbook.request.QuizBookRequestDto
import com.android.quizcafe.core.data.model.quizbook.response.QuizBookResponseDto
import com.android.quizcafe.core.data.remote.service.QuizBookService
import com.android.quizcafe.core.network.model.ApiResponse
import com.android.quizcafe.core.network.model.NetworkResult
import javax.inject.Inject

class QuizBookRemoteDataSource @Inject constructor(
    private val quizBookService: QuizBookService
) {
    // TODO: 카테고리 요청 api 수정 시 request 추가하기
    suspend fun getCategoriesByType(request: CategoryRequestDto): NetworkResult<ApiResponse<List<QuizBookCategoryResponseDto>>> =
        quizBookService.getCategories()

    suspend fun getAllCategories() : NetworkResult<ApiResponse<QuizBookCategoryResponseDto>> =
        quizBookService.getAllCategories()
    suspend fun getQuizBookDetail(request: QuizBookDetailRequestDto): NetworkResult<ApiResponse<QuizBookDetailResponseDto>> =
        quizBookService.getQuizBookDetail(request.quizBookId)

    suspend fun getQuizBooksByCategory(request: QuizBookRequestDto): NetworkResult<ApiResponse<List<QuizBookResponseDto>>> =
        quizBookService.getQuizBooks(request.category)

    suspend fun markQuizBook(quizBookId: Long): NetworkResult<Unit> =
        quizBookService.markQuizBook(quizBookId)

    suspend fun unmarkQuizBook(quizBookId: Long): NetworkResult<Unit> =
        quizBookService.unmarkQuizBook(quizBookId)
}
