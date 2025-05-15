package com.android.quizcafe.core.domain.repository

import com.android.quizcafe.core.domain.model.Resource
import com.android.quizcafe.core.domain.model.auth.request.ResetPasswordRequest
import com.android.quizcafe.core.domain.model.auth.request.SendCodeRequest
import com.android.quizcafe.core.domain.model.auth.request.LoginRequest
import com.android.quizcafe.core.domain.model.auth.request.SignUpRequest
import com.android.quizcafe.core.domain.model.auth.request.VerifyCodeRequest
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    fun sendCode(request: SendCodeRequest): Flow<Resource<Unit>>

    fun verifyCode(request: VerifyCodeRequest): Flow<Resource<Unit>>

    fun signUp(request: SignUpRequest): Flow<Resource<Unit>>

    fun login(request: LoginRequest): Flow<Resource<Unit>>

    fun resetPassword(request: ResetPasswordRequest): Flow<Resource<Unit>>
}
