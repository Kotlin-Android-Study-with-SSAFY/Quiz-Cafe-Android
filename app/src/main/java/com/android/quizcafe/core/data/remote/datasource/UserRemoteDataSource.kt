package com.android.quizcafe.core.data.remote.datasource

import com.android.quizcafe.core.data.model.quizbook.response.QuizBookResponseDto
import com.android.quizcafe.core.data.model.user.request.UpdateNicknameRequestDto
import com.android.quizcafe.core.data.model.user.request.UpdatePasswordRequestDto
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

    suspend fun updateUserNickName(request: UpdateNicknameRequestDto): NetworkResult<ApiResponse<Unit>> =
        userService.updateUserNickName(request = request)

    suspend fun getMyQuizBooks(): NetworkResult<ApiResponse<List<QuizBookResponseDto>>> =
        userService.getMyQuizBooks()

    suspend fun updatePassword(request: UpdatePasswordRequestDto): NetworkResult<ApiResponse<Unit>> =
        userService.updatePassword(request = request)
}
