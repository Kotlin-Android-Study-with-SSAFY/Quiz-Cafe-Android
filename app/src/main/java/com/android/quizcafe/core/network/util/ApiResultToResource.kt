
import com.android.quizcafe.core.domain.model.Resource
import com.android.quizcafe.core.network.model.ApiResult
import com.android.quizcafe.core.network.model.onError
import com.android.quizcafe.core.network.model.onException
import com.android.quizcafe.core.network.model.onSuccess
import com.android.quizcafe.core.network.util.handleApiResultErrorCode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withTimeoutOrNull
import java.net.ConnectException

suspend fun <T : Any> apiResultToResource(call: suspend () -> ApiResult<T>): Resource<T> {
    return withTimeoutOrNull(30000L) {
        when (val result = call()) {
            is ApiResult.Success -> Resource.Success(result.data)
            is ApiResult.Error -> {
                handleApiResultErrorCode(code = result.code, message = result.message)
            }
            is ApiResult.Exception -> {
                when(result.e){
                    is ConnectException -> Resource.Failure("네트워크에 연결되어 있지 않습니다.", 499)
                    else -> Resource.Failure(result.e.message.toString(), 499)
                }
            }
        }
    } ?: Resource.Failure("Timeout! Please try again.", 408)
}

fun <T : Any> apiResultToResourceFlow(call: suspend () -> ApiResult<T>): Flow<Resource<T>> = flow {
    emit(Resource.Loading)
    withTimeoutOrNull(30000L) {
        call()
            .onSuccess {
                emit(Resource.Success(it))
            }
            .onError { code, message ->
                emit(handleApiResultErrorCode(code = code, message = message))
            }
            .onException {
                when(it){
                    is ConnectException -> emit(Resource.Failure("네트워크에 연결되어 있지 않습니다.", 499))
                    else -> emit(Resource.Failure(it.message ?: it.toString(), 499))
                }
            }
    } ?: emit(Resource.Failure("Timeout! Please try again.", 408))
}.flowOn(Dispatchers.IO)
