package com.android.quizcafe.core.network.util.calladapter
import com.android.quizcafe.core.network.model.NetworkResult
import retrofit2.Call
import retrofit2.CallAdapter
import java.lang.reflect.Type

class NetworkResultCallAdapter(
    private val resultType: Type
) : CallAdapter<Type, Call<NetworkResult<Type>>> {

    override fun responseType(): Type = resultType

    override fun adapt(call: Call<Type>): Call<NetworkResult<Type>> {
        return NetworkResultCall(call, resultType)
    }
}
