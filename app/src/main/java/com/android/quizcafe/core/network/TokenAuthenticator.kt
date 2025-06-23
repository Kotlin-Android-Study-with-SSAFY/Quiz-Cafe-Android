package com.android.quizcafe.core.network

import com.android.quizcafe.core.datastore.AuthManager
import com.android.quizcafe.core.datastore.LogoutReason
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject

/*
    401에러 응답 시 authenticate 호출
    현재 accessToken이 만료되면 로그인 화면으로 이동

    TODO: refresh token 추가되면 accessToken 갱신 로직 추가 및 refreshToken도 만료일 경우에만 로그아웃 호출하기
 */
class TokenAuthenticator @Inject constructor(
    private val authManager: AuthManager
) : Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {
        runBlocking {
            authManager.logout(LogoutReason.SessionExpired("토큰이 만료되었습니다."))
        }
        return null
    }
}
