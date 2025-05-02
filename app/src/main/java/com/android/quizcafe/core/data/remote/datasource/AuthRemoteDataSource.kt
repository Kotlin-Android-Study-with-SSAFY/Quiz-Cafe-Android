package com.android.quizcafe.core.data.remote.datasource

import com.android.quizcafe.core.data.model.auth.SendCodeRequestDto
import com.android.quizcafe.core.data.model.auth.SignUpRequestDto
import com.android.quizcafe.core.data.model.auth.VerifyCodeRequestDto
import com.android.quizcafe.core.data.remote.service.AuthService
import com.android.quizcafe.core.domain.model.auth.SendCodeRequest
import com.android.quizcafe.core.domain.model.auth.SignUpRequest
import com.android.quizcafe.core.network.model.ApiResponse
import com.android.quizcafe.core.network.model.NetworkResult
import javax.inject.Inject

class AuthRemoteDataSource @Inject constructor(
    private val authService: AuthService
) {

    suspend fun sendVerificationCode(request: SendCodeRequestDto) : NetworkResult<ApiResponse<Unit>> =
        authService.sendVerificationCode(email = request.email)

    suspend fun checkVerificationCode(request : VerifyCodeRequestDto) : NetworkResult<ApiResponse<Unit>> =
        authService.checkVerificationCode(request)

    suspend fun signUp(request : SignUpRequestDto) : NetworkResult<ApiResponse<Unit>> =
        authService.signUp(request)

}