package com.android.quizcafe.core.data.remote.service

import com.android.quizcafe.core.data.model.quizbook.response.QuizBookResponseDto
import com.android.quizcafe.core.data.model.user.request.ChangePasswordRequestDto
import com.android.quizcafe.core.data.model.user.response.UserInfoResponseDto
import com.android.quizcafe.core.network.model.ApiResponse
import com.android.quizcafe.core.network.model.NetworkResult
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Path

interface UserService {

    @GET("/user")
    suspend fun getUserInfo(): NetworkResult<ApiResponse<UserInfoResponseDto>>

    @DELETE("/user")
    suspend fun deleteUser(): NetworkResult<ApiResponse<Unit>>

    @PATCH("/user")
    suspend fun updateUser(
        @Path("nickname") niciName: String
    ): NetworkResult<ApiResponse<Unit>>

    @PATCH("/user/password")
    suspend fun changePassword(
        @Body request: ChangePasswordRequestDto
    ): NetworkResult<ApiResponse<Unit>>

    @GET("/user/quiz-book")
    suspend fun getMyQuizBooks(): NetworkResult<ApiResponse<List<QuizBookResponseDto>>>
}
