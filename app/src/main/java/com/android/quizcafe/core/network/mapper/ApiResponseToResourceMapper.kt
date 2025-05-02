package com.android.quizcafe.core.network.mapper

import com.android.quizcafe.core.common.network.HttpStatus
import com.android.quizcafe.core.domain.model.Resource
import com.android.quizcafe.core.network.model.ApiResponse
import com.android.quizcafe.core.network.model.NetworkResult
import com.android.quizcafe.core.network.model.onError
import com.android.quizcafe.core.network.model.onException
import com.android.quizcafe.core.network.model.onSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withTimeoutOrNull
import java.net.ConnectException
import java.net.SocketTimeoutException



suspend fun <T : Any> apiResponseToResource(call : suspend () -> NetworkResult<ApiResponse<T>>): Resource<T> {
    return withTimeoutOrNull(30_000L) {
        when (val result = call()) {
            is NetworkResult.Success -> Resource.Success(result.data.data)
            is NetworkResult.Error -> Resource.Failure(
                errorMessage = result.message ?: DEFAULT_ERROR_MESSAGE,
                code = result.code
            )
            is NetworkResult.Exception -> handleNetworkException(result.e)
        }
    } ?: Resource.Failure(
        errorMessage = "요청 시간이 초과되었습니다.",
        code = HttpStatus.REQUEST_TIMEOUT
    )
}

fun <T : Any> apiResponseToResourceFlow(call : suspend() -> NetworkResult<ApiResponse<T>>): Flow<Resource<T>> = flow {
    emit(Resource.Loading)
    withTimeoutOrNull(30_000L) {
        call()
            .onSuccess {
                emit( Resource.Success(it.data))}
            .onError { code, message ->
                emit(Resource.Failure(
                    errorMessage = message ?: DEFAULT_ERROR_MESSAGE,
                    code = code
                ))
            }
            .onException { e->  emit(handleNetworkException(e)) }
    } ?: emit(
        Resource.Failure(
            errorMessage = "요청 시간이 초과되었습니다.",
            code = HttpStatus.REQUEST_TIMEOUT
        )
    )
}.flowOn(Dispatchers.IO)


fun handleNetworkException(e: Throwable): Resource.Failure {
    return when (e) {
        is ConnectException -> Resource.Failure(
            errorMessage = "네트워크 연결을 확인해주세요.",
            code = HttpStatus.NETWORK_DISCONNECTED
        )
        is SocketTimeoutException -> Resource.Failure(
            errorMessage = "서버 응답이 지연되었습니다.",
            code = HttpStatus.REQUEST_TIMEOUT
        )
        else -> Resource.Failure(
            errorMessage = e.message ?: DEFAULT_ERROR_MESSAGE,
            code = HttpStatus.UNKNOWN
        )
    }
}