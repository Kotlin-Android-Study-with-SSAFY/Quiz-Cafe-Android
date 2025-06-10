package com.android.quizcafe.core.domain.model

import com.android.quizcafe.core.common.network.HttpStatus

sealed class Resource<out R> {
    data object Loading : Resource<Nothing>()

    data class Success<out T>(
        val data: T
    ) : Resource<T>()

    data class Failure(
        val errorMessage: String,
        val code: Int,
    ) : Resource<Nothing>() {
        fun printError(): String {
            return "code : $code, msg : $errorMessage"
        }

        companion object {
            val NullData = Failure(
                errorMessage = "ApiResponse data is null",
                code = HttpStatus.INTERNAL_SERVER_ERROR
            )
        }
    }

}
