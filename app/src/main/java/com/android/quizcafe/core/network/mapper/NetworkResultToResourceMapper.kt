package com.android.quizcafe.core.network.mapper

import com.android.quizcafe.core.common.network.HttpStatus
import com.android.quizcafe.core.domain.model.Resource
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

const val DEFAULT_ERROR_MESSAGE = "default error message"

suspend fun <T : Any> (suspend () -> NetworkResult<T>).toResource() : Resource<T>{
    return withTimeoutOrNull(3_000L){
        when(val result = this@toResource()){
            is NetworkResult.Success -> Resource.Success(result.data)
            is NetworkResult.Error -> Resource.Failure(errorMessage = result.message ?: DEFAULT_ERROR_MESSAGE, code = result.code)
            is NetworkResult.Exception -> {
                when (result.e) {
                    is ConnectException -> Resource.Failure(errorMessage = result.e.message ?: DEFAULT_ERROR_MESSAGE, code = HttpStatus.NETWORK_DISCONNECTED)
                    else -> Resource.Failure(errorMessage = result.e.message ?: DEFAULT_ERROR_MESSAGE, code = HttpStatus.UNKNOWN)
                }
            }
        }
    } ?: Resource.Failure(errorMessage = "time out", code = HttpStatus.REQUEST_TIMEOUT)
}

fun <T : Any> (suspend () -> NetworkResult<T>).toResourceFlow(): Flow<Resource<T>> = flow {
    emit(Resource.Loading)
    withTimeoutOrNull(3_000L){
        this@toResourceFlow()
            .onSuccess { emit(Resource.Success(it)) }
            .onError { code, message ->
                emit(Resource.Failure(errorMessage = message ?: DEFAULT_ERROR_MESSAGE, code = code))
            }
            .onException { e ->
                emit(
                    when (e) {
                        is ConnectException -> Resource.Failure(errorMessage = e.message ?: DEFAULT_ERROR_MESSAGE, code = HttpStatus.NETWORK_DISCONNECTED)
                        else -> Resource.Failure(errorMessage = e.message ?: DEFAULT_ERROR_MESSAGE, code = HttpStatus.UNKNOWN)
                    }
                )
            }
    } ?: Resource.Failure(errorMessage = "time out", code = HttpStatus.REQUEST_TIMEOUT)
}.flowOn(Dispatchers.IO)
