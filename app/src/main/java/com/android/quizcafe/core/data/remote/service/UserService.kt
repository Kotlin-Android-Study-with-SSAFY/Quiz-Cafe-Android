package com.android.quizcafe.core.data.remote.service

import com.android.quizcafe.core.data.model.quizbook.response.QuizBookResponseDto
import com.android.quizcafe.core.data.model.user.request.UpdateNicknameRequestDto
import com.android.quizcafe.core.data.model.user.request.UpdatePasswordRequestDto
import com.android.quizcafe.core.data.model.user.response.UserInfoResponseDto
import com.android.quizcafe.core.network.model.ApiResponse
import com.android.quizcafe.core.network.model.NetworkResult
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH

interface UserService {

    @GET("/user")
    suspend fun getUserInfo(): NetworkResult<ApiResponse<UserInfoResponseDto>>

    @DELETE("/user")
    suspend fun deleteUser(): NetworkResult<Unit>

    @PATCH("/user")
    suspend fun updateUserNickName(
        @Body request: UpdateNicknameRequestDto
    ): NetworkResult<ApiResponse<Unit>>

    @GET("/user/quiz-book")
    suspend fun getCreatedQuizBooksByMe(): NetworkResult<ApiResponse<List<QuizBookResponseDto>>>

    @PATCH("/user/password")
    suspend fun updatePassword(
        @Body request: UpdatePasswordRequestDto
    ): NetworkResult<Unit>
}
