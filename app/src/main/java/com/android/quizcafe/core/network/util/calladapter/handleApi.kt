package com.android.quizcafe.core.network.util.calladapter

import com.android.quizcafe.core.common.network.HttpStatus
import com.android.quizcafe.core.network.model.NetworkResult
import org.json.JSONObject
import retrofit2.HttpException
import retrofit2.Response

fun <T : Any> handleApi(
    execute: () -> Response<T>
): NetworkResult<T> {
    return try {
        val response = execute()
        val body = response.body()
        if (response.isSuccessful && body != null) {
            NetworkResult.Success(body)
        }else if(response.isSuccessful && body == null){
            // TODO : 런타임에 타입 체크해서 만약 T가 Unit이라면 Success 아니면 Error로 가야함 -> 아니면 서버에서 204로 확실히 본문 없다는걸 알려주면 되긴함
            if(Unit::class.java.isAssignableFrom((body as? Any)?.javaClass ?: Unit::class.java)){
                @Suppress("UNCHECKED_CAST")
                NetworkResult.Success(Unit as T)
            }else{
                NetworkResult.Error(
                    code = HttpStatus.INTERNAL_SERVER_ERROR.code,                                             // TODO : 이때 code 다르게 반환할 지 고민
                    message = "Server returned invalid null body"
                )
            }
        }else{
            val responseJson = JSONObject(response.errorBody()?.string()!!)
            val errMsg = "${responseJson.getString("status")} ${responseJson.getString("error")}"
            NetworkResult.Error(code = response.code(), message = errMsg)
        }
    } catch (e: HttpException) {
        NetworkResult.Error(code = e.code(), message = e.message())
    } catch (e: Throwable) {
        NetworkResult.Exception(e)
    }
}
