package com.android.quizcafe.core.domain.repository

import com.android.quizcafe.core.domain.model.Resource
import com.android.quizcafe.core.domain.model.auth.request.ResetPasswordRequest
import com.android.quizcafe.core.domain.model.auth.request.SendCodeRequest
import com.android.quizcafe.core.domain.model.auth.request.LoginRequest
import com.android.quizcafe.core.domain.model.auth.request.SignUpRequest
import com.android.quizcafe.core.domain.model.auth.request.VerifyCodeRequest
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    suspend fun sendCode(request: SendCodeRequest): Flow<Resource<Unit>>

    suspend fun verifyCode(request: VerifyCodeRequest): Flow<Resource<Unit>>

    suspend fun signUp(request: SignUpRequest): Flow<Resource<Unit>>

    suspend fun login(request: LoginRequest): Flow<Resource<Unit>>

    suspend fun resetPassword(request: ResetPasswordRequest): Flow<Resource<Unit>>
}
