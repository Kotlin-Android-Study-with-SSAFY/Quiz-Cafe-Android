package com.android.quizcafe.core.network.mapper

import com.android.quizcafe.core.common.network.HttpStatus
import com.android.quizcafe.core.domain.model.Resource
import com.android.quizcafe.core.network.model.ApiResponse
import com.android.quizcafe.core.network.model.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withTimeoutOrNull

fun <T : Any> emptyApiResponseToResourceFlow(
    call: suspend () -> NetworkResult<ApiResponse<T>>
): Flow<Resource<Unit>> = resourceFlowFromNetworkResult(call) {
    Resource.Success(Unit)
}

fun <T : Any, R : Any> apiResponseToResourceFlow(
    mapper: (T) -> R,
    call: suspend () -> NetworkResult<ApiResponse<T>>
): Flow<Resource<R>> = resourceFlowFromNetworkResult(call) { data ->
    if (data == null) {
        Resource.Failure(DEFAULT_ERROR_MESSAGE, HttpStatus.NO_CONTENT)
    } else {
        Resource.Success(mapper(data))
    }
}

fun <T : Any, R : Any> apiResponseListToResourceFlow(
    mapper: (T) -> R,
    call: suspend () -> NetworkResult<ApiResponse<List<T>>>
): Flow<Resource<List<R>>> = resourceFlowFromNetworkResult(call) { data ->
    if (data == null) {
        Resource.Failure(DEFAULT_ERROR_MESSAGE, HttpStatus.NO_CONTENT)
    } else {
        Resource.Success(data.map(mapper))
    }
}

private fun <T : Any, R : Any> resourceFlowFromNetworkResult(
    call: suspend () -> NetworkResult<ApiResponse<T>>,
    onSuccess: suspend (T?) -> Resource<R>
): Flow<Resource<R>> = flow {
    emit(Resource.Loading)

    val result = withTimeoutOrNull(3_000L) {
        call()
    } ?: return@flow emit(Resource.Failure("요청 시간이 초과되었습니다.", HttpStatus.REQUEST_TIMEOUT))

    when (result) {
        is NetworkResult.Success -> emit(onSuccess(result.data.data))
        is NetworkResult.Error -> emit(Resource.Failure(result.message ?: DEFAULT_ERROR_MESSAGE, result.code))
        is NetworkResult.Exception -> emit(handleNetworkException(result.e))
    }
}.flowOn(Dispatchers.IO)
