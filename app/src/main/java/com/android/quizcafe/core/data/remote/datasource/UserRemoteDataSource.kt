package com.android.quizcafe.core.data.remote.datasource

import com.android.quizcafe.core.data.model.quizbook.response.QuizBookResponseDto
import com.android.quizcafe.core.data.model.user.request.ResetPasswordRequestDto
import com.android.quizcafe.core.data.model.user.response.UserInfoResponseDto
import com.android.quizcafe.core.data.remote.service.UserService
import com.android.quizcafe.core.network.model.ApiResponse
import com.android.quizcafe.core.network.model.NetworkResult
import javax.inject.Inject

class UserRemoteDataSource @Inject constructor(
    private val userService: UserService
) {

    suspend fun getUserInfo(): NetworkResult<ApiResponse<UserInfoResponseDto>> =
        userService.getUserInfo()

    suspend fun deleteUser(): NetworkResult<Unit> = userService.deleteUser()

    suspend fun updateUserNickName(nickName: String): NetworkResult<Unit> =
        userService.updateUserNickName(nickName = nickName)

    suspend fun resetPassword(request: ResetPasswordRequestDto): NetworkResult<Unit> =
        userService.resetPassword(request = request)

    suspend fun getMyQuizBooks(): NetworkResult<ApiResponse<List<QuizBookResponseDto>>> =
        userService.getMyQuizBooks()
}
