package com.android.quizcafe.core.network

import android.util.Log
import com.android.quizcafe.core.data.remote.service.AuthService
import com.android.quizcafe.core.datastore.AuthManager
import com.android.quizcafe.core.datastore.LogoutReason
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject

// TODO: 일정 횟수 이상 재시도 제한 로직 추가
class TokenAuthenticator @Inject constructor(
    private val authManager: AuthManager,
    private val authService: AuthService
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        try {
            Log.d("TokenAuthenticator", "401 응답으로 인한 authenticate 호출")
            val currentAccessToken = authManager.getAccessToken() ?: throw Exception("accessToken is null")
            val currentRefreshToken = authManager.getRefreshToken() ?: throw Exception("refreshToken is null")
            Log.d("TokenAuthenticator", "현재 accessToken : $currentAccessToken\n현재 refreshToken : $currentRefreshToken")

            synchronized(this) {
                if (currentAccessToken != response.request.header("Authorization")?.removePrefix("Bearer ")) {
                    Log.d("TokenAuthenticator", "토큰이 이미 갱신되었으므로 요청 재시도")
                    return response.request.createRequestWithRenewedToken(currentAccessToken)
                }
                val newTokenResponse = authService.renewToken(currentRefreshToken)
                if (!newTokenResponse.isSuccessful) throw Exception("토큰 갱신 실패")

                val newTokenData = newTokenResponse.body() ?: throw Exception("토큰 갱신 응답 에러")
                authManager.saveToken(newTokenData.accessToken, newTokenData.refreshToken)
                Log.d("TokenAuthenticator", "갱신 accessToken : $currentAccessToken\n갱신 refreshToken : $currentRefreshToken")

                return response.request.createRequestWithRenewedToken(newTokenData.accessToken)
            }
        } catch (e: Exception) {
            authManager.logout(LogoutReason.SessionExpired("세션이 만료되었습니다 : ${e.message}"))
            return null
        }
    }

    private fun Request.createRequestWithRenewedToken(renewedToken: String): Request = newBuilder()
        .header("Authorization", "Bearer $renewedToken")
        .build()
}
