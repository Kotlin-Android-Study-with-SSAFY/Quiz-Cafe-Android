package com.android.quizcafe.core.data.remote.service

import com.android.quizcafe.core.data.model.quizbook.QuizBookCategoryDto
import com.android.quizcafe.core.data.model.quizbook.QuizBookDto
import com.android.quizcafe.core.data.model.quizbook.response.CategoryResponseDto
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
        @Query("category")category : String
    ): NetworkResult<ApiResponse<QuizBookDto>>


    @GET("quiz-book/category")
    suspend fun getCategories(
//        @Body request: QuizBookRequest
    ): NetworkResult<ApiResponse<List<CategoryResponseDto>>>

    suspend fun getAllCategories() : NetworkResult<ApiResponse<QuizBookCategoryDto>>
    @GET("quiz-book/{quizBookId}")
    suspend fun getQuizBookDetail(
        @Path("quizBookId") quizBookId: Long
    ): NetworkResult<ApiResponse<QuizBookDetailResponseDto>>

    @GET("quiz-book")
    suspend fun getQuizBooks(
        @Query("category") request: String
    ): NetworkResult<ApiResponse<List<QuizBookResponseDto>>>

    @POST("quiz-book-bookmark")
    suspend fun saveQuizBook(
        @Query("quizBookId") quizBookId: Long
    ): NetworkResult<Unit>

    @DELETE("quiz-book-bookmark")
    suspend fun unsaveQuizBook(
        @Query("quizBookId") quizBookId: Long
    ): NetworkResult<Unit>
}
