
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    // TODO : AuthManager 들어가야함
): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val authRequestBuilder = chain.request().newBuilder()
        val userToken = ""  // TODO : AuthManager로부터 Token 받아오기
        authRequestBuilder.addHeader("auth", "$userToken")
//        authRequestBuilder.addHeader("auth", "asdasdasd")
        val response = chain.proceed(authRequestBuilder.build())
        if(response.header("Authorization") != null){
            val newAccessToken = response.header("Authorization")!!
        }
        return response
    }
}