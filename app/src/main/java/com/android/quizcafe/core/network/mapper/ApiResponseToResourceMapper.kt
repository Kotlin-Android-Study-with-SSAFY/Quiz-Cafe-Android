package com.android.quizcafe.core.network.mapper

import com.android.quizcafe.core.common.network.HttpStatus
import com.android.quizcafe.core.domain.model.Resource
import com.android.quizcafe.core.network.model.ApiResponse
import com.android.quizcafe.core.network.model.NetworkResult
import com.android.quizcafe.core.network.model.onSuccess
import kotlinx.coroutines.withTimeoutOrNull

suspend fun apiNoResponseToResource(
    call: suspend () -> NetworkResult<ApiResponse<Unit>>
): Resource<Unit> = apiCallToResource(call) {
    Resource.Success(Unit)
}

suspend fun <I : Any, O : Any> apiSingleResponseToResource(
    mapper: (I) -> O,
    call: suspend () -> NetworkResult<ApiResponse<I>>
): Resource<O> = apiCallToResource(call) { data ->
    if (data == null) {
        Resource.Failure(
            errorMessage = DEFAULT_ERROR_MESSAGE,
            code = HttpStatus.NO_CONTENT
        )
    } else {
        Resource.Success(mapper(data))
    }
}

suspend fun <I : Any, O : Any> apiResponseToResource(
    mapper: (I) -> O,
    call: suspend () -> NetworkResult<ApiResponse<List<I>>>
): Resource<List<O>> = apiCallToResource(call) { data ->
    if (data == null) {
        Resource.Failure(
            errorMessage = DEFAULT_ERROR_MESSAGE,
            code = HttpStatus.NO_CONTENT
        )
    } else {
        Resource.Success(data.map(mapper))
    }
}
private suspend fun <T : Any, R> apiCallToResource(
    call: suspend () -> NetworkResult<ApiResponse<T>>,
    onSuccess: (T?) -> Resource<R>
): Resource<R> {
    return withTimeoutOrNull(3_000L) {
        when (val result = call()) {
            is NetworkResult.Success -> onSuccess(result.data.data)
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
