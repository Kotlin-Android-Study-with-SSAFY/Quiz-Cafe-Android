package com.android.quizcafe.core.data.remote.service

import com.android.quizcafe.core.data.model.quizbook.response.QuizBookCategoryResponseDto
import com.android.quizcafe.core.data.model.quizbook.response.QuizBookDetailResponseDto
import com.android.quizcafe.core.network.model.ApiResponse
import com.android.quizcafe.core.network.model.NetworkResult
import retrofit2.http.GET
import retrofit2.http.Path
import com.android.quizcafe.core.data.model.quizbook.response.QuizBookResponseDto
import retrofit2.http.DELETE
import retrofit2.http.POST
import retrofit2.http.Query

interface QuizBookService {

    @GET("quiz-book")
    suspend fun getQuizBooksByCategory(
        @Query("category")category: String
    ): NetworkResult<ApiResponse<QuizBookResponseDto>>

    @GET("quiz-book/category")
    suspend fun getCategories(
//        @Body request: QuizBookRequest
    ): NetworkResult<ApiResponse<List<QuizBookCategoryResponseDto>>>

    suspend fun getAllCategories(): NetworkResult<ApiResponse<QuizBookCategoryResponseDto>>

    @GET("quiz-book/{quizBookId}")
    suspend fun getQuizBookDetail(
        @Path("quizBookId") quizBookId: Long
    ): NetworkResult<ApiResponse<QuizBookDetailResponseDto>>

    @GET("quiz-book")
    suspend fun getQuizBooks(
        @Query("category") request: String
    ): NetworkResult<ApiResponse<List<QuizBookResponseDto>>>

    @POST("quiz-book-bookmark")
    suspend fun markQuizBook(
        @Query("quizBookId") quizBookId: Long
    ): NetworkResult<Unit>

    @DELETE("quiz-book-bookmark")
    suspend fun unmarkQuizBook(
        @Query("quizBookId") quizBookId: Long
    ): NetworkResult<Unit>
}
