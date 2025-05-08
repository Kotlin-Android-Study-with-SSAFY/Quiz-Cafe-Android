package com.android.quizcafe.core.data.repository

import com.android.quizcafe.core.common.network.HttpStatus
import com.android.quizcafe.core.data.model.auth.request.toDto
import com.android.quizcafe.core.data.remote.datasource.AuthRemoteDataSource
import com.android.quizcafe.core.domain.model.Resource
import com.android.quizcafe.core.domain.model.auth.request.ResetPasswordRequest
import com.android.quizcafe.core.domain.model.auth.request.SendCodeRequest
import com.android.quizcafe.core.domain.model.auth.request.LoginRequest
import com.android.quizcafe.core.domain.model.auth.request.SignUpRequest
import com.android.quizcafe.core.domain.model.auth.request.VerifyCodeRequest
import com.android.quizcafe.core.domain.repository.AuthRepository
import com.android.quizcafe.core.network.mapper.DEFAULT_ERROR_MESSAGE
import com.android.quizcafe.core.network.mapper.apiResponseToResourceFlow
import com.android.quizcafe.core.network.mapper.handleNetworkException
import com.android.quizcafe.core.network.model.onError
import com.android.quizcafe.core.network.model.onException
import com.android.quizcafe.core.network.model.onSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withTimeoutOrNull
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val remoteDataSource : AuthRemoteDataSource
) : AuthRepository {

    override suspend fun sendCode(request: SendCodeRequest): Flow<Resource<Unit>> =
        apiResponseToResourceFlow { remoteDataSource.sendCode(request.toDto()) }

    override suspend fun verifyCode(request: VerifyCodeRequest): Flow<Resource<Unit>> =
        apiResponseToResourceFlow { remoteDataSource.verifyCode(request.toDto())}

    override suspend fun signUp(request: SignUpRequest): Flow<Resource<Unit>> =
        apiResponseToResourceFlow { remoteDataSource.signUp(request.toDto()) }


    override suspend fun login(request: LoginRequest): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading)
        withTimeoutOrNull(3_000L) {
            remoteDataSource.login(request.toDto())
                .onSuccess {
                    // TODO : accessToken 저장 로직 구현
                    emit( Resource.Success(null))
                }
                .onError { code, message ->
                    emit(Resource.Failure(
                        errorMessage = message ?: DEFAULT_ERROR_MESSAGE,
                        code = code))
                }
                .onException { e-> emit(handleNetworkException(e)) }
        } ?: emit(Resource.Failure(
            errorMessage = "요청 시간이 초과되었습니다.",
            code = HttpStatus.REQUEST_TIMEOUT))
    }.flowOn(Dispatchers.IO)

    override suspend fun resetPassword(request: ResetPasswordRequest): Flow<Resource<Unit>> =
        apiResponseToResourceFlow { remoteDataSource.resetPassword(request.toDto()) }
}
