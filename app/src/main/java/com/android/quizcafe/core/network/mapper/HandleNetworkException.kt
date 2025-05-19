package com.android.quizcafe.core.network.mapper

import com.android.quizcafe.core.common.network.HttpStatus
import com.android.quizcafe.core.domain.model.Resource
import java.net.ConnectException
import java.net.SocketTimeoutException

internal fun handleNetworkException(e: Throwable): Resource.Failure {
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
