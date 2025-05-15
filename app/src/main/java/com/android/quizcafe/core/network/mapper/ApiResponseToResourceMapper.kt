package com.android.quizcafe.core.network.mapper

import com.android.quizcafe.core.common.network.HttpStatus
import com.android.quizcafe.core.domain.model.Resource
import com.android.quizcafe.core.network.model.ApiResponse
import com.android.quizcafe.core.network.model.NetworkResult
import kotlinx.coroutines.withTimeoutOrNull

suspend fun emptyApiResponseToResource(
    call: suspend () -> NetworkResult<ApiResponse<Unit>>
): Resource<Unit> = resourceFromNetworkResult(call) {
    Resource.Success(Unit)
}

suspend fun <T : Any, R : Any> apiResponseToResource(
    mapper: (T) -> R,
    call: suspend () -> NetworkResult<ApiResponse<T>>
): Resource<R> = resourceFromNetworkResult(call) { data ->
    if (data == null) {
        Resource.Failure(
            errorMessage = DEFAULT_ERROR_MESSAGE,
            code = HttpStatus.NO_CONTENT
        )
    } else {
        Resource.Success(mapper(data))
    }
}

suspend fun <T : Any, R : Any> apiResponseListToResource(
    mapper: (T) -> R,
    call: suspend () -> NetworkResult<ApiResponse<List<T>>>
): Resource<List<R>> = resourceFromNetworkResult(call) { data ->
    if (data == null) {
        Resource.Failure(
            errorMessage = DEFAULT_ERROR_MESSAGE,
            code = HttpStatus.NO_CONTENT
        )
    } else {
        Resource.Success(data.map(mapper))
    }
}
private suspend fun <T : Any, R : Any> resourceFromNetworkResult(
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
