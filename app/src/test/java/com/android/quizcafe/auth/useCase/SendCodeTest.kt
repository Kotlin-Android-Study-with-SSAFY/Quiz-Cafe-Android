package com.android.quizcafe.auth.useCase

import com.android.quizcafe.core.data.remote.datasource.AuthRemoteDataSource
import com.android.quizcafe.core.data.remote.service.AuthService
import com.android.quizcafe.core.data.repository.AuthRepositoryImpl
import com.android.quizcafe.core.domain.model.Resource
import com.android.quizcafe.core.domain.model.auth.request.SendCodeRequest
import com.android.quizcafe.core.domain.repository.AuthRepository
import com.android.quizcafe.core.domain.usecase.auth.SendCodeUseCase
import com.android.quizcafe.core.domain.usecase.auth.SignUpUseCase
import com.android.quizcafe.core.domain.usecase.auth.VerifyCodeUseCase
import com.android.quizcafe.core.network.util.calladapter.NetworkResultCallAdapterFactory
import com.android.quizcafe.core.network.util.convertor.NullOnEmptyConverterFactory
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okio.buffer
import okio.source
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

class AuthUseCasesTest {
    private lateinit var mockWebServer: MockWebServer
    private lateinit var authService: AuthService
    private lateinit var remoteDataSource: AuthRemoteDataSource
    private lateinit var authRepository: AuthRepository
    private lateinit var sendCodeUseCase: SendCodeUseCase
    private lateinit var verifyCodeUseCase: VerifyCodeUseCase
    private lateinit var signUpUseCase: SignUpUseCase


    @Before
    fun setUp() {
        mockWebServer = MockWebServer()
        mockWebServer.start()

        val contentType = "application/json".toMediaType()
        val json = Json { ignoreUnknownKeys = true }

        authService = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(NullOnEmptyConverterFactory)
            .addConverterFactory(json.asConverterFactory(contentType))
            .addCallAdapterFactory(NetworkResultCallAdapterFactory.create())
            .build()
            .create(AuthService::class.java)

        remoteDataSource = AuthRemoteDataSource(authService)
        authRepository = AuthRepositoryImpl(remoteDataSource)
        sendCodeUseCase = SendCodeUseCase(authRepository)
        verifyCodeUseCase = VerifyCodeUseCase(authRepository)
        signUpUseCase = SignUpUseCase(authRepository)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    private fun MockResponse.setJsonBody(fileName: String): MockResponse {
        val inputStream = javaClass.classLoader!!.getResourceAsStream(fileName)
        val source = inputStream.source().buffer()
        setBody(source.readString(Charsets.UTF_8))
        return this
    }

    @Test
    fun `sendVerificationCode returns Resource_Success on success response`() = runTest {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setJsonBody("authUseCasesTest/sendCode/success_response.json")
        )

        val results = mutableListOf<Resource<Unit>>()
        sendCodeUseCase.invoke(SendCodeRequest("test@example.com")).collect(results::add)

        println("result : ${results[1]}")
        assertEquals(2, results.size)
        assertTrue(results[0] is Resource.Loading)
        assertTrue(results[1] is Resource.Success)
        assertEquals(null, (results[1] as Resource.Success).data)
    }

    @Test
    fun `sendVerificationCode returns Resource_Failure on fail response`() = runTest {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(400)
                .setJsonBody("authUseCasesTest/sendCode/fail_response.json")
        )

        val results = mutableListOf<Resource<Unit>>()
        sendCodeUseCase.invoke(SendCodeRequest("invalid-email")).collect(results::add)

        assertTrue(results[0] is Resource.Loading)
        assertTrue(results[1] is Resource.Failure)
        assertEquals(400, (results[1] as Resource.Failure).code)
        assertEquals("유효하지 않은 이메일 형식", (results[1] as Resource.Failure).errorMessage)
    }

    // TODO : 회원가입 Request id값 null로 작동하는지

    // TODO : 인증코드 코드 검증에서 등록되지 않은 email로 요청 보냈을 때 에러 발생 체크
}