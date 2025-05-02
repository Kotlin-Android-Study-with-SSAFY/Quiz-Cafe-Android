package com.android.quizcafe.core.domain.model

sealed class Resource<out R> {
    data object Loading: Resource<Nothing>()

    data class Success<out T>(
        val data: T?
    ): Resource<T>()

    data class Failure(
        val errorMessage: String,
        val code: Int,
    ): Resource<Nothing>(){
        fun printError() : String{
            return "code : $code, msg : $errorMessage"
        }
    }
}
