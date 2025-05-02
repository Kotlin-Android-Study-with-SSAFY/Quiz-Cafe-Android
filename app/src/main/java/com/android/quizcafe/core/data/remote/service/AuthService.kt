package com.android.quizcafe.core.data.remote.service

import com.android.quizcafe.core.data.model.auth.SignUpRequestDto
import com.android.quizcafe.core.data.model.auth.VerifyCodeRequestDto
import com.android.quizcafe.core.domain.model.auth.SignUpRequest
import com.android.quizcafe.core.network.model.ApiResponse
import com.android.quizcafe.core.network.model.NetworkResult
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthService {

    @POST("auth/verification-code/send")
    suspend fun sendVerificationCode(
        @Query("email") email : String
    ) : NetworkResult<ApiResponse<Unit>>

    @POST("auth/verification-code/verify")
    suspend fun checkVerificationCode(
        @Body request : VerifyCodeRequestDto
    ) : NetworkResult<ApiResponse<Unit>>


    @POST("auth/sign-up")
    suspend fun signUp(
        @Body request: SignUpRequestDto
    ) : NetworkResult<ApiResponse<Unit>>


}