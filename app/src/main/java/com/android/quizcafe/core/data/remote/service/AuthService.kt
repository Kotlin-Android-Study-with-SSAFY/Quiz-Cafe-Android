package com.android.quizcafe.core.data.remote.service

import com.android.quizcafe.core.data.model.auth.request.SignInRequestDto
import com.android.quizcafe.core.data.model.auth.request.SignUpRequestDto
import com.android.quizcafe.core.data.model.auth.request.VerifyCodeRequestDto
import com.android.quizcafe.core.data.model.auth.response.SignInResponseDto
import com.android.quizcafe.core.network.model.ApiResponse
import com.android.quizcafe.core.network.model.NetworkResult
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthService {

    @POST("auth/verification-code/send")
    suspend fun sendCode(
        @Query("email") email : String
    ) : NetworkResult<ApiResponse<Unit>>

    @POST("auth/verification-code/verify")
    suspend fun verifyCode(
        @Body request : VerifyCodeRequestDto
    ) : NetworkResult<ApiResponse<Unit>>


    @POST("auth/sign-up")
    suspend fun signUp(
        @Body request: SignUpRequestDto
    ) : NetworkResult<ApiResponse<Unit>>

    @POST("auth/sign-in")
    suspend fun signIn(
        @Body request: SignInRequestDto
    ) : NetworkResult<ApiResponse<SignInResponseDto>>

    suspend fun resetPassword(
        @Query("email") email : String
    ) : NetworkResult<ApiResponse<Unit>>
}