package com.android.quizcafe.core.network.model

import com.android.quizcafe.core.common.network.HttpStatus

/**
 * API 호출 결과를 표현하는 sealed class.
 * - Success: 성공적으로 데이터를 받아온 경우
 * - Error: 서버로부터 에러 코드와 메시지를 받은 경우
 * - Exception: 네트워크 오류 등 예외가 발생한 경우
 */
sealed class NetworkResult<T : Any> {
    class Success<T : Any>(val data: T) : NetworkResult<T>()
    class Error<T : Any>(val code: Int, val message: String?) : NetworkResult<T>()
    class Exception<T : Any>(val e: Throwable) : NetworkResult<T>()
}

suspend fun <T : Any> NetworkResult<T>.onSuccess(
    executable: suspend (T) -> Unit
): NetworkResult<T> = apply {
    if (this is NetworkResult.Success<T>) {
        executable(data)
    }
}

suspend fun <T : Any> NetworkResult<T>.onError(
    executable: suspend (code: Int, message: String?) -> Unit
): NetworkResult<T> = apply {
    if (this is NetworkResult.Error<T>) {
        executable(code, message)
    }
}

suspend fun <T : Any> NetworkResult<T>.onException(
    executable: suspend (e: Throwable) -> Unit
): NetworkResult<T> = apply {
    if (this is NetworkResult.Exception<T>) {
        executable(e)
    }
}

suspend fun <T : Any> NetworkResult<T>.onErrorOrException(
    executable: suspend (code: Int, message: String?) -> Unit
): NetworkResult<T> = apply {
    if (this is NetworkResult.Error<T>) {
        executable(code, message)
    } else if (this is NetworkResult.Exception<T>) {
        executable(HttpStatus.UNKNOWN, e.message)
    }
}

fun <T : Any> NetworkResult<T>.log(tag: String = "NetworkResult"): String {
    return when (this) {
        is NetworkResult.Success -> ("[$tag] Success: data=$data")
        is NetworkResult.Error -> ("[$tag] Error: code=$code, message=$message")
        is NetworkResult.Exception -> ("[$tag] Exception: ${e::class.simpleName}: ${e.message}")
    }
}
