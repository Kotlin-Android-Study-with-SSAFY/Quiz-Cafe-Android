package com.android.quizcafe.core.network.util.calladapter
import com.android.quizcafe.core.network.model.ApiResult
import okhttp3.Request
import okio.Timeout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ApiResultCall<T : Any>(
    private val call: Call<T>
) : Call<ApiResult<T>> {

    override fun enqueue(callback: Callback<ApiResult<T>>) {
        call.enqueue(object : Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {
                val apiResult = handleApi { response }
                callback.onResponse(this@ApiResultCall, Response.success(apiResult))
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                val apiResult = ApiResult.Exception<T>(t)
                callback.onResponse(this@ApiResultCall, Response.success(apiResult))
            }
        })
    }

    override fun execute(): Response<ApiResult<T>> = throw NotImplementedError()
    override fun clone(): Call<ApiResult<T>> = ApiResultCall(call.clone())
    override fun request(): Request = call.request()
    override fun timeout(): Timeout = call.timeout()
    override fun isExecuted(): Boolean = call.isExecuted
    override fun isCanceled(): Boolean = call.isCanceled
    override fun cancel() { call.cancel() }
}