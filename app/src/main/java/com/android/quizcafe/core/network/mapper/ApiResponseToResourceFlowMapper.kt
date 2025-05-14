package com.android.quizcafe.core.network.mapper

import com.android.quizcafe.core.common.network.HttpStatus
import com.android.quizcafe.core.domain.model.Resource
import com.android.quizcafe.core.network.model.ApiResponse
import com.android.quizcafe.core.network.model.NetworkResult
import com.android.quizcafe.core.network.model.onSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withTimeoutOrNull

fun <T : Any> apiNoResponseToResourceFlow(
    call: suspend () -> NetworkResult<ApiResponse<T>>
): Flow<Resource<Unit>> = apiCallToResourceFlow(call) {
    Resource.Success(Unit)
}

fun <I : Any, O : Any> apiSingleResponseToResourceFlow(
    mapper: (I) -> O,
    call: suspend () -> NetworkResult<ApiResponse<I>>
): Flow<Resource<O>> = apiCallToResourceFlow(call) { data ->
    if (data == null) {
        Resource.Failure(DEFAULT_ERROR_MESSAGE, HttpStatus.NO_CONTENT)
    } else {
        Resource.Success(mapper(data))
    }
}

fun <I : Any, O : Any> apiResponseToResourceFlow(
    mapper: (I) -> O,
    call: suspend () -> NetworkResult<ApiResponse<List<I>>>
): Flow<Resource<List<O>>> = apiCallToResourceFlow(call) { data ->
    if (data == null) {
        Resource.Failure(DEFAULT_ERROR_MESSAGE, HttpStatus.NO_CONTENT)
    } else {
        Resource.Success(data.map(mapper))
    }
}

private fun <T : Any, R> apiCallToResourceFlow(
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
