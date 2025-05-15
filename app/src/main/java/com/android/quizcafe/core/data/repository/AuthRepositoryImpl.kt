package com.android.quizcafe.core.data.repository

import com.android.quizcafe.core.common.network.HttpStatus
import com.android.quizcafe.core.data.model.auth.request.toDto
import com.android.quizcafe.core.data.remote.datasource.AuthRemoteDataSource
import com.android.quizcafe.core.datastore.AuthManager
import com.android.quizcafe.core.domain.model.Resource
import com.android.quizcafe.core.domain.model.auth.request.LoginRequest
import com.android.quizcafe.core.domain.model.auth.request.ResetPasswordRequest
import com.android.quizcafe.core.domain.model.auth.request.SendCodeRequest
import com.android.quizcafe.core.domain.model.auth.request.SignUpRequest
import com.android.quizcafe.core.domain.model.auth.request.VerifyCodeRequest
import com.android.quizcafe.core.domain.repository.AuthRepository
import com.android.quizcafe.core.network.mapper.DEFAULT_ERROR_MESSAGE
import com.android.quizcafe.core.network.mapper.emptyApiResponseToResourceFlow
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
    private val remoteDataSource: AuthRemoteDataSource,
    private val authManager: AuthManager
) : AuthRepository {

    override fun sendCode(request: SendCodeRequest): Flow<Resource<Unit>> =
        emptyApiResponseToResourceFlow { remoteDataSource.sendCode(request.toDto()) }

    override fun verifyCode(request: VerifyCodeRequest): Flow<Resource<Unit>> =
        emptyApiResponseToResourceFlow { remoteDataSource.verifyCode(request.toDto()) }

    override fun signUp(request: SignUpRequest): Flow<Resource<Unit>> =
        emptyApiResponseToResourceFlow { remoteDataSource.signUp(request.toDto()) }

    override fun login(request: LoginRequest): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading)
        withTimeoutOrNull(3_000L) {
            remoteDataSource.login(request.toDto())
                .onSuccess { response ->
                    response.data?.let { it ->
                        emit(Resource.Success(Unit))
                        authManager.saveAccessToken(it.accessToken)
                    } ?: emit(Resource.Failure(errorMessage = "LoginResponse data is null", HttpStatus.INTERNAL_SERVER_ERROR))
                }
                .onError { code, message ->
                    emit(
                        Resource.Failure(
                            errorMessage = message ?: DEFAULT_ERROR_MESSAGE,
                            code = code
                        )
                    )
                }
                .onException { e -> emit(handleNetworkException(e)) }
        } ?: emit(
            Resource.Failure(
                errorMessage = "요청 시간이 초과되었습니다.",
                code = HttpStatus.REQUEST_TIMEOUT
            )
        )
    }.flowOn(Dispatchers.IO)

    override fun resetPassword(request: ResetPasswordRequest): Flow<Resource<Unit>> =
        emptyApiResponseToResourceFlow { remoteDataSource.resetPassword(request.toDto()) }
}
