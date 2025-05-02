package com.android.quizcafe.core.domain.repository

import com.android.quizcafe.core.domain.model.Resource
import com.android.quizcafe.core.domain.model.auth.SendCodeRequest
import com.android.quizcafe.core.domain.model.auth.SignUpRequest
import com.android.quizcafe.core.domain.model.auth.VerifyCodeRequest
import kotlinx.coroutines.flow.Flow


interface AuthRepository {

    suspend fun sendCode(request: SendCodeRequest) : Flow<Resource<Unit>>

    suspend fun verifyCode(request : VerifyCodeRequest) : Flow<Resource<Unit>>

    suspend fun signUp(request : SignUpRequest) : Flow<Resource<Unit>>
}