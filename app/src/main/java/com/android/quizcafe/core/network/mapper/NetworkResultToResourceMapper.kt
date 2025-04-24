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

suspend fun <T : Any> (suspend () -> NetworkResult<T>).toResource() : Resource<T>{
    return withTimeoutOrNull(30_000L){
        when(val result = this@toResource()){
            is NetworkResult.Success -> Resource.Success(result.data)
            is NetworkResult.Error -> Resource.Failure(errorMessage = HttpStatus.fromCode(result.code).message, code = result.code)
            is NetworkResult.Exception -> {
                when(result.e){
                    is ConnectException -> Resource.Failure(errorMessage = HttpStatus.NETWORK_DISCONNECTED.message, code = HttpStatus.NETWORK_DISCONNECTED.code)
                    else -> Resource.Failure(errorMessage = HttpStatus.UNKNOWN.message, code = HttpStatus.UNKNOWN.code)
                }
            }
        }
    } ?: Resource.Failure(errorMessage = HttpStatus.REQUEST_TIMEOUT.message, code = HttpStatus.REQUEST_TIMEOUT.code)
}

fun <T : Any>(suspend() -> NetworkResult<T>).toResourceFlow() : Flow<Resource<T>> = flow{
    emit(Resource.Loading)
    withTimeoutOrNull(30_000L){
        this@toResourceFlow()
            .onSuccess { emit(Resource.Success(it)) }
            .onError{ code, message ->
                emit(Resource.Failure(errorMessage = HttpStatus.fromCode(code).message, code = code))
            }
            .onException { e ->
                when(e){
                    is ConnectException -> Resource.Failure(errorMessage = HttpStatus.NETWORK_DISCONNECTED.message, code = HttpStatus.NETWORK_DISCONNECTED.code)
                    else -> Resource.Failure(errorMessage = HttpStatus.UNKNOWN.message, code = HttpStatus.UNKNOWN.code)
                }
            }
    } ?: Resource.Failure(errorMessage = HttpStatus.REQUEST_TIMEOUT.message, code = HttpStatus.REQUEST_TIMEOUT.code)
}.flowOn(Dispatchers.IO)

// TODO : 서버에서 온 메세지를 그대로 보낼 것인가? 아니면 항상 code에 mapping된 메세지를 올려보낼 것인가?