package com.android.quizcafe.core.data.remote.service

import com.android.quizcafe.core.data.model.auth.request.LoginRequestDto
import com.android.quizcafe.core.data.model.auth.request.SendCodeRequestDto
import com.android.quizcafe.core.data.model.auth.request.SignUpRequestDto
import com.android.quizcafe.core.data.model.auth.request.VerifyCodeRequestDto
import com.android.quizcafe.core.data.model.auth.response.LoginResponseDto
import com.android.quizcafe.core.network.model.ApiResponse
import com.android.quizcafe.core.network.model.NetworkResult
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthService {

    @POST("auth/verification-code/send")
    suspend fun sendCode(
        @Body request : SendCodeRequestDto
    ) : NetworkResult<ApiResponse<Unit>>

    @POST("auth/verification-code/verify")
    suspend fun verifyCode(
        @Body request: VerifyCodeRequestDto
    ): NetworkResult<ApiResponse<Unit>>

    @POST("auth/sign-up")
    suspend fun signUp(
        @Body request: SignUpRequestDto
    ): NetworkResult<ApiResponse<Unit>>

    @POST("auth/sign-in")
    suspend fun login(
        @Body request: LoginRequestDto
    ): NetworkResult<ApiResponse<LoginResponseDto>>

    @POST("auth/password/reset")
    suspend fun resetPassword(
        @Query("email") email: String
    ): NetworkResult<ApiResponse<Unit>>
}
