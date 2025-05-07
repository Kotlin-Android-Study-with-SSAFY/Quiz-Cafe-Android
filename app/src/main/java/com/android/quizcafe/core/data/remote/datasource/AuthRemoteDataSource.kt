package com.android.quizcafe.core.data.remote.datasource

import com.android.quizcafe.core.data.model.auth.request.ResetPasswordRequestDto
import com.android.quizcafe.core.data.model.auth.request.SendCodeRequestDto
import com.android.quizcafe.core.data.model.auth.request.LoginRequestDto
import com.android.quizcafe.core.data.model.auth.request.SignUpRequestDto
import com.android.quizcafe.core.data.model.auth.request.VerifyCodeRequestDto
import com.android.quizcafe.core.data.model.auth.response.LoginResponseDto
import com.android.quizcafe.core.data.remote.service.AuthService
import com.android.quizcafe.core.network.model.ApiResponse
import com.android.quizcafe.core.network.model.NetworkResult
import javax.inject.Inject

class AuthRemoteDataSource @Inject constructor(
    private val authService: AuthService
) {

    suspend fun sendCode(request: SendCodeRequestDto): NetworkResult<ApiResponse<Unit>> =
        authService.sendCode(email = request.email)

    suspend fun verifyCode(request: VerifyCodeRequestDto): NetworkResult<ApiResponse<Unit>> =
        authService.verifyCode(request)

    suspend fun signUp(request: SignUpRequestDto): NetworkResult<ApiResponse<Unit>> =
        authService.signUp(request)

    suspend fun login(request: LoginRequestDto): NetworkResult<ApiResponse<LoginResponseDto>> =
        authService.login(request)

    suspend fun resetPassword(request: ResetPasswordRequestDto): NetworkResult<ApiResponse<Unit>> =
        authService.resetPassword(email = request.email)
}
