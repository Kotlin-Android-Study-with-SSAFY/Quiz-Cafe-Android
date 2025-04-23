package com.android.quizcafe.core.network.util.calladapter

import com.android.quizcafe.core.network.model.ApiResult
import org.json.JSONObject
import retrofit2.HttpException
import retrofit2.Response

fun <T : Any> handleApi(
    execute: () -> Response<T>
): ApiResult<T> {
    return try {
        val response = execute()
        val body = response.body()
        if (response.isSuccessful && body != null) {
            ApiResult.Success(body)
        }else if(response.isSuccessful && body == null){
            // TODO : 런타임에 타입 체크해서 만약 T가 Unit이라면 Success 아니면 Error로 가야함 -> 아니면 서버에서 204로 확실히 본문 없다는걸 알려주면 되긴함
            if(Unit::class.java.isAssignableFrom((body as? Any)?.javaClass ?: Unit::class.java)){
                @Suppress("UNCHECKED_CAST")
                ApiResult.Success(Unit as T)
            }else{
                ApiResult.Error(
                    code = 500,                                             // TODO : 이때 code 다르게 반환할 지 고민
                    message = "Server returned invalid null body"
                )
            }
        }else{
            val responseJson = JSONObject(response.errorBody()?.string()!!)
            val errMsg = "${responseJson.getString("status")} ${responseJson.getString("error")}"
            ApiResult.Error(code = response.code(), message = errMsg)
        }
    } catch (e: HttpException) {
        ApiResult.Error(code = e.code(), message = e.message())
    } catch (e: Throwable) {
        ApiResult.Exception(e)
    }
}
