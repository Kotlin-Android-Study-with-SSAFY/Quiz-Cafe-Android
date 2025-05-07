package com.android.quizcafe.usecase

import com.android.quizcafe.core.domain.model.Resource
import com.android.quizcafe.core.domain.model.auth.request.SendCodeRequest
import com.android.quizcafe.core.domain.usecase.auth.SendCodeUseCase
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okio.buffer
import okio.source
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class AuthUseCaseTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @BindValue
    val mockWebServer: MockWebServer = MockWebServer()

    @Inject
    lateinit var sendCodeUseCase: SendCodeUseCase

    @Before
    fun setUp() {
        mockWebServer.start(port = 8080)
        hiltRule.inject()
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    private fun MockResponse.setJsonBody(fileName: String): MockResponse {
        val inputStream = javaClass.classLoader!!.getResourceAsStream(fileName)
            ?: throw IllegalArgumentException("File not found: $fileName")
        val source = inputStream.source().buffer()
        setBody(source.readString(Charsets.UTF_8))
        return this
    }

    @Test
    fun sendCode_returns_Resource_Success() = runTest {
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
    fun sendVCode_returns_Resource_Failure() = runTest {
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
}
