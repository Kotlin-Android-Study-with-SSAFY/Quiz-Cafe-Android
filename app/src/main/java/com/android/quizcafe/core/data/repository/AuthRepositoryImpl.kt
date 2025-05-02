package com.android.quizcafe.core.data.repository

import com.android.quizcafe.core.data.model.auth.request.toDto
import com.android.quizcafe.core.data.remote.datasource.AuthRemoteDataSource
import com.android.quizcafe.core.domain.model.Resource
import com.android.quizcafe.core.domain.model.auth.request.SendCodeRequest
import com.android.quizcafe.core.domain.model.auth.request.SignUpRequest
import com.android.quizcafe.core.domain.model.auth.request.VerifyCodeRequest
import com.android.quizcafe.core.domain.repository.AuthRepository
import com.android.quizcafe.core.network.mapper.apiResponseToResourceFlow
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val remoteDataSource : AuthRemoteDataSource
) : AuthRepository {

    override suspend fun sendCode(request: SendCodeRequest): Flow<Resource<Unit>> =
        apiResponseToResourceFlow { remoteDataSource.sendVerificationCode(request.toDto()) }

    override suspend fun verifyCode(request: VerifyCodeRequest): Flow<Resource<Unit>> =
        apiResponseToResourceFlow { remoteDataSource.checkVerificationCode(request.toDto())}

    override suspend fun signUp(request: SignUpRequest): Flow<Resource<Unit>> =
        apiResponseToResourceFlow { remoteDataSource.signUp(request.toDto()) }
}
