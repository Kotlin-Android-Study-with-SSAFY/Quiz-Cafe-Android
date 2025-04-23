package com.android.quizcafe.core.network.model

/**
 * API 호출 결과를 표현하는 sealed class.
 * - Success: 성공적으로 데이터를 받아온 경우
 * - Error: 서버로부터 에러 코드와 메시지를 받은 경우
 * - Exception: 네트워크 오류 등 예외가 발생한 경우
 */
sealed class ApiResult<T : Any> {
    class Success<T: Any>(val data: T) : ApiResult<T>()
    class Error<T: Any>(val code: Int, val message: String?) : ApiResult<T>()
    class Exception<T: Any>(val e: Throwable) : ApiResult<T>()
}

suspend fun <T : Any> ApiResult<T>.onSuccess(
    executable: suspend (T) -> Unit
): ApiResult<T> = apply {
    if (this is ApiResult.Success<T>) {
        executable(data)
    }
}

suspend fun <T : Any> ApiResult<T>.onError(
    executable: suspend (code: Int, message: String?) -> Unit
): ApiResult<T> = apply {
    if (this is ApiResult.Error<T>) {
        executable(code, message)
    }
}

suspend fun <T : Any> ApiResult<T>.onException(
    executable: suspend (e: Throwable) -> Unit
): ApiResult<T> = apply {
    if (this is ApiResult.Exception<T>) {
        executable(e)
    }
}

suspend fun <T : Any> ApiResult<T>.onErrorOrException(
    executable: suspend (code: Int, message: String?) -> Unit
): ApiResult<T> = apply {
    if (this is ApiResult.Error<T>) {
        executable(code, message)
    }else if (this is ApiResult.Exception<T>) {
        // TODO : 이 부분 어떻게 할 것인지 얘기해봐야함
        executable(999, e.message)
    }
}
