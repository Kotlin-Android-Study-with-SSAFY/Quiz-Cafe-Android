package com.android.quizcafe.core.network.util.calladapter

import com.android.quizcafe.core.common.network.HttpStatus
import com.android.quizcafe.core.network.model.NetworkResult
import org.json.JSONObject
import retrofit2.HttpException
import retrofit2.Response
import java.lang.reflect.Type

fun <T : Any> handleApi(
    resultType: Type,
    execute: () -> Response<T>
): NetworkResult<T> {
    return try {
        val response = execute()
        val body = response.body()
        if (response.isSuccessful && body != null) {
            NetworkResult.Success(body)
        } else if (response.isSuccessful && body == null) {
            if (resultType == Unit::class) {
                @Suppress("UNCHECKED_CAST")
                NetworkResult.Success(Unit as T)
            } else {
                NetworkResult.Error(
                    code = HttpStatus.INTERNAL_SERVER_ERROR,
                    message = "Server returned invalid null body"
                )
            }
        } else {
            handleErrorResponse(response)
        }
    } catch (e: HttpException) {
        NetworkResult.Error(code = e.code(), message = e.message())
    } catch (e: Throwable) {
        NetworkResult.Exception(e)
    }
}

private fun <T : Any> handleErrorResponse(response: Response<T>): NetworkResult.Error<T> {
    val errorBody = response.errorBody()?.string() ?: throw IllegalStateException("Error body is null code : ${response.code()}")
    val errorMessage = extractErrorMessage(errorBody)
    return NetworkResult.Error(
        code = response.code(),
        message = errorMessage.ifBlank { "errorMessage is empty (code: ${response.code()})" }
    )
}

private fun extractErrorMessage(errorBody: String): String {
    val json = JSONObject(errorBody)
    if (!json.has("message")) {
        throw IllegalStateException("Error body does not contain 'message' field: $errorBody")
    }
    return json.getString("message")
}
